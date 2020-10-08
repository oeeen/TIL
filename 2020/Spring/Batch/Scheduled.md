# @Scheduled에 관하여

@Scheduled라는 어노테이션이 어떻게 동작하는지 확인하고자 한다. 사용방법은 인터넷에 찾으면 쉽게 나오기 때문에 따로 정리하지 않는다.

이 @Scheduled라는 어노테이션을 사용하기 위해 SpringBoot Application은 @EnableScheduling 어노테이션을 달아줬다. 일단 이것부터 알아보자.

## @EnableScheduling

해당 어노테이션에 달려있는 주석을 해석해보면, 스프링의 스케쥴된 작업을 수행할 수 있게 하고 스프링이 관리하는 빈에 Scheduled라는 어노테이션을 찾을 수 있게 한다고 되어있다.

SchedulingConfiguration 이라는 클래스에서 ScheduledAnnotationBeanPostProcessor를 빈으로 등록한다. ScheduledAnnotationBeanPostProcessor는 Spring의 Scheduled 어노테이션을 동작하게 한다. @EnableScheduling 어노테이션을 사용하면 자동으로 import 된다.

```java
@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class SchedulingConfiguration {

    @Bean(name = TaskManagementConfigUtils.SCHEDULED_ANNOTATION_PROCESSOR_BEAN_NAME)
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public ScheduledAnnotationBeanPostProcessor scheduledAnnotationProcessor() {
        return new ScheduledAnnotationBeanPostProcessor();
    }

}
```

ScheduledAnnotationBeanPostProcessor 부분을 따라가보면, postProcessAfterInitialization이라는 메서드가 있는데 그 메서드는 다음과 같다.

```java
@Override
public Object postProcessAfterInitialization(Object bean, String beanName) {
    if (bean instanceof AopInfrastructureBean || bean instanceof TaskScheduler ||
            bean instanceof ScheduledExecutorService) {
        // Ignore AOP infrastructure such as scoped proxies.
        return bean;
    }

    Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
    if (!this.nonAnnotatedClasses.contains(targetClass) &&
            AnnotationUtils.isCandidateClass(targetClass, Arrays.asList(Scheduled.class, Schedules.class))) {
        Map<Method, Set<Scheduled>> annotatedMethods = MethodIntrospector.selectMethods(targetClass,
                (MethodIntrospector.MetadataLookup<Set<Scheduled>>) method -> {
                    Set<Scheduled> scheduledMethods = AnnotatedElementUtils.getMergedRepeatableAnnotations(
                            method, Scheduled.class, Schedules.class);
                    return (!scheduledMethods.isEmpty() ? scheduledMethods : null);
                });
        if (annotatedMethods.isEmpty()) {
            this.nonAnnotatedClasses.add(targetClass);
            if (logger.isTraceEnabled()) {
                logger.trace("No @Scheduled annotations found on bean class: " + targetClass);
            }
        }
        else {
            // Non-empty set of methods
            annotatedMethods.forEach((method, scheduledMethods) ->
                    scheduledMethods.forEach(scheduled -> processScheduled(scheduled, method, bean)));
            if (logger.isTraceEnabled()) {
                logger.trace(annotatedMethods.size() + " @Scheduled methods processed on bean '" + beanName +
                        "': " + annotatedMethods);
            }
        }
    }
    return bean;
}
```

Scheduled.class, Schedules.class라는 어노테이션이 달려있는 메서드들을 모아서 등록하는 단계인 것으로 예상하여, 디버깅 하면서 내가 만든 ScheduledTask라는 bean이 이 메서드에 들어왔을 때를 살펴봤다. 그래서 annotatedMethods에 Scheduled Method가 들어가는 것을 볼 수 있다.

![AnnotatedMethods](/2020/assets/img/annotatedMethods.png)

그러면 AnnotatedMethods가 비어있지 않기 때문에, else 절에서 해당 메서드들을 processScheduled를 실행시킨다. processScheduled 메서드를 살펴보자

```java
protected void processScheduled(Scheduled scheduled, Method method, Object bean) {
    try {
        Runnable runnable = createRunnable(bean, method);
        boolean processedSchedule = false;
        String errorMessage =
                "Exactly one of the 'cron', 'fixedDelay(String)', or 'fixedRate(String)' attributes is required";

        Set<ScheduledTask> tasks = new LinkedHashSet<>(4);

        // Determine initial delay
        long initialDelay = scheduled.initialDelay();
        String initialDelayString = scheduled.initialDelayString();
        if (StringUtils.hasText(initialDelayString)) {
            Assert.isTrue(initialDelay < 0, "Specify 'initialDelay' or 'initialDelayString', not both");
            if (this.embeddedValueResolver != null) {
                initialDelayString = this.embeddedValueResolver.resolveStringValue(initialDelayString);
            }
            if (StringUtils.hasLength(initialDelayString)) {
                try {
                    initialDelay = parseDelayAsLong(initialDelayString);
                }
                catch (RuntimeException ex) {
                    throw new IllegalArgumentException(
                            "Invalid initialDelayString value \"" + initialDelayString + "\" - cannot parse into long");
                }
            }
        }

        // Check cron expression
        String cron = scheduled.cron();
        if (StringUtils.hasText(cron)) {
            String zone = scheduled.zone();
            if (this.embeddedValueResolver != null) {
                cron = this.embeddedValueResolver.resolveStringValue(cron);
                zone = this.embeddedValueResolver.resolveStringValue(zone);
            }
            if (StringUtils.hasLength(cron)) {
                Assert.isTrue(initialDelay == -1, "'initialDelay' not supported for cron triggers");
                processedSchedule = true;
                if (!Scheduled.CRON_DISABLED.equals(cron)) {
                    TimeZone timeZone;
                    if (StringUtils.hasText(zone)) {
                        timeZone = StringUtils.parseTimeZoneString(zone);
                    }
                    else {
                        timeZone = TimeZone.getDefault();
                    }
                    tasks.add(this.registrar.scheduleCronTask(new CronTask(runnable, new CronTrigger(cron, timeZone))));
                }
            }
        }

        // At this point we don't need to differentiate between initial delay set or not anymore
        if (initialDelay < 0) {
            initialDelay = 0;
        }

        // Check fixed delay
        long fixedDelay = scheduled.fixedDelay();
        if (fixedDelay >= 0) {
            Assert.isTrue(!processedSchedule, errorMessage);
            processedSchedule = true;
            tasks.add(this.registrar.scheduleFixedDelayTask(new FixedDelayTask(runnable, fixedDelay, initialDelay)));
        }
        String fixedDelayString = scheduled.fixedDelayString();
        if (StringUtils.hasText(fixedDelayString)) {
            if (this.embeddedValueResolver != null) {
                fixedDelayString = this.embeddedValueResolver.resolveStringValue(fixedDelayString);
            }
            if (StringUtils.hasLength(fixedDelayString)) {
                Assert.isTrue(!processedSchedule, errorMessage);
                processedSchedule = true;
                try {
                    fixedDelay = parseDelayAsLong(fixedDelayString);
                }
                catch (RuntimeException ex) {
                    throw new IllegalArgumentException(
                            "Invalid fixedDelayString value \"" + fixedDelayString + "\" - cannot parse into long");
                }
                tasks.add(this.registrar.scheduleFixedDelayTask(new FixedDelayTask(runnable, fixedDelay, initialDelay)));
            }
        }

        // Check fixed rate
        long fixedRate = scheduled.fixedRate();
        if (fixedRate >= 0) {
            Assert.isTrue(!processedSchedule, errorMessage);
            processedSchedule = true;
            tasks.add(this.registrar.scheduleFixedRateTask(new FixedRateTask(runnable, fixedRate, initialDelay)));
        }
        String fixedRateString = scheduled.fixedRateString();
        if (StringUtils.hasText(fixedRateString)) {
            if (this.embeddedValueResolver != null) {
                fixedRateString = this.embeddedValueResolver.resolveStringValue(fixedRateString);
            }
            if (StringUtils.hasLength(fixedRateString)) {
                Assert.isTrue(!processedSchedule, errorMessage);
                processedSchedule = true;
                try {
                    fixedRate = parseDelayAsLong(fixedRateString);
                }
                catch (RuntimeException ex) {
                    throw new IllegalArgumentException(
                            "Invalid fixedRateString value \"" + fixedRateString + "\" - cannot parse into long");
                }
                tasks.add(this.registrar.scheduleFixedRateTask(new FixedRateTask(runnable, fixedRate, initialDelay)));
            }
        }

        // Check whether we had any attribute set
        Assert.isTrue(processedSchedule, errorMessage);

        // Finally register the scheduled tasks
        synchronized (this.scheduledTasks) {
            Set<ScheduledTask> regTasks = this.scheduledTasks.computeIfAbsent(bean, key -> new LinkedHashSet<>(4));
            regTasks.addAll(tasks);
        }
    }
    catch (IllegalArgumentException ex) {
        throw new IllegalStateException(
                "Encountered invalid @Scheduled method '" + method.getName() + "': " + ex.getMessage());
    }
}
```

메서드가 긴데 차근차근 따라가보면 initialDelay 부분은 무슨 의미인지 모르겠지만, 그 이후로는 cron 확인, fixedDelay, fixedRate 확인 후 ScheduledTaskRegistrar에 해당하는 Task로 등록한다. 이렇게 해당 빈에 Scheduled 어노테이션이 달려있는 메서드들을 모두 ScheduledTaskRegistrar에 등록한 후 끝난다. 일단 이런식으로 Scheduled 어노테이션이 달려있는 메서드들을 Task로 등록해서 가지고 있다.

## @Scheduled

@Scheduled 어노테이션을 쓸 메서드가 있는 클래스는 Component로 등록하기 때문에 ComponentScan 타이밍에 빈으로 등록된다.

```java
@Component
public class ScheduledTask {

    @Scheduled(fixedDelay = 1000L)
    public void repeatTask() {
        long currentTime = System.currentTimeMillis();
        Thread currentThread = Thread.currentThread();
        System.out.println("Task 1 - CurrentTime: " + currentTime + " - CurrentThread: " + currentThread.getId());
    }

    @Scheduled(fixedDelay = 2000L)
    public void repeatTask2() {
        long currentTime = System.currentTimeMillis();
        Thread currentThread = Thread.currentThread();
        System.out.println("Task 2 - CurrentTime: " + currentTime + " - CurrentThread: " + currentThread.getId());
    }

    @Scheduled(fixedDelay = 1000L)
    public void repeatTask3() {
        long currentTime = System.currentTimeMillis();
        Thread currentThread = Thread.currentThread();
        System.out.println("Task 3 - CurrentTime: " + currentTime + " - CurrentThread: " + currentThread.getId());
    }

    @Scheduled(fixedDelay = 1000L)
    public void repeatTask4() {
        long currentTime = System.currentTimeMillis();
        Thread currentThread = Thread.currentThread();
        System.out.println("Task 4 - CurrentTime: " + currentTime + " - CurrentThread: " + currentThread.getId());
    }

    @Scheduled(fixedDelay = 1000L)
    public void repeatTask5() {
        long currentTime = System.currentTimeMillis();
        Thread currentThread = Thread.currentThread();
        System.out.println("Task 5 - CurrentTime: " + currentTime + " - CurrentThread: " + currentThread.getId());
    }

    @Scheduled(fixedDelay = 1000L)
    public void repeatTask6() {
        long currentTime = System.currentTimeMillis();
        Thread currentThread = Thread.currentThread();
        System.out.println("Task 6 - CurrentTime: " + currentTime + " - CurrentThread: " + currentThread.getId());
    }
}
```

나는 쓸데없이 이렇게 Scheduled 어노테이션 달린 메서드들을 많이 만들었다. 위에서 task로 등록된 메서드들은 언제, 어디서 실행될까? 이 쪽은 task에 디버깅포인트 잡고 stack trace만 살펴봐도 대충 어떤 흐름인지는 이해할 수 있다. 언제 실행되는지는 찾지 못했다. Async stack trace를 보니, ScheduledThreadPoolExcutor쪽에서 무엇인가를 하는 것 같다.

ScheduledThreadPoolExcutor에 run() 메서드를 보자

```java
public void run() {
    boolean periodic = isPeriodic();
    if (!canRunInCurrentRunState(periodic))
        cancel(false);
    else if (!periodic)
        ScheduledFutureTask.super.run();
    else if (ScheduledFutureTask.super.runAndReset()) {
        setNextRunTime();
        reExecutePeriodic(outerTask);
    }
}
```

Period는 주기인 것 같은데, ScheduledFutureTask 생성자에서 넣어준다. 먼저 실행 중이거나 종료된 상태인지 확인하여, 실행 중이라면 cancel시킨다. 그리고 period를 확인하여 0이면 Task를 한번만 실행시킨다. 주기가 있다면(반복해야 한다면) FutureTask에서 runAndReset한다. runAndReset은 future를 수행하고 future를 초기상태로 초기화한다.(성공적으로 실행하고 리셋했으면 true를 리턴한다) 그 다음과정으로 다음에 실행할 시간을 셋팅하고 주기 실행을 위해 reExecutePeriodic 메서드를 실행한다.

```java
void reExecutePeriodic(RunnableScheduledFuture<?> task) {
    if (canRunInCurrentRunState(true)) {
        super.getQueue().add(task);
        if (!canRunInCurrentRunState(true) && remove(task))
            task.cancel(false);
        else
            ensurePrestart();
    }
}
```

ThreadPoolExecutor에서 work queue를 얻고 그 큐에 task를 넣는다. 그렇게 해서 주기적으로 실행시킨다.
