# AOP (Aspect Oriented Programming)

관점지향 프로그래밍, 핵심 로직으로부터 부가 기능을 분리한다. 분리한 부가 기능을 aspect라는 독특한 형태로 만들어서 설계, 개발한다.

## 타겟(Target)

핵심 기능을 담고 있는 모듈로 타겟은 부가기능을 부여할 대상이 된다.

## 어드바이스(Advice)

어드바이스는 타겟에 제공할 부가기능을 담고 있는 모듈이다.

## 조인포인트(Join Point)

어드바이스가 적용될 수 있는 위치를 말한다. 타겟 객체가 구현한 인터페이스의 모든 메서드는 조인 포인트가 된다.

## 포인트 컷(Pointcut)

어드바이스를 적용할 타겟의 메서드를 선별하는 정규표현식이다. 포인트컷 표현식은 execution으로 시작하고 메서드의 Signature를 비교하는 방법을 주로 이용한다.

## 애스펙트(Aspect)

애스펙트는 AOP의 기본 모듈이다. 애스펙트 = 어드바이스 + 포인트컷

애스펙트는 싱글톤 형태의 객체로 존재한다.

## 어드바이저(Advisor)

어드바이저 = 어드바이스 + 포인트컷

어드바이저는 Spring AOP에서만 사용되는 특별한 용어이다.

## 위빙(Weaving)

위빙은 포인트컷에 의해서 결정된 타겟의 조인 포인트에 부가기능(어드바이스)를 삽입하는 과정을 뜻한다.

위빙은 AOP가 핵심기능(타겟)의 코드에 영향을 주지 않으면서 필요한 부가기능(어드바이스)를 추가할 수 있도록 해주는 핵심적인 처리과정이다.

## 예시

```java

@Component
@Aspect
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restController() {
    }

    @Pointcut("within(@org.springframework.stereotype.Controller *)")
    public void controller() {
    }

    @Pointcut("execution(* *.*(..))")
    protected void allMethod() {
    }

    @Around("(restController() || controller()) && allMethod()")
    public Object doLogging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.debug("{}, Arguments : {}", proceedingJoinPoint.getSignature(), proceedingJoinPoint.getArgs());
        Object retVal = proceedingJoinPoint.proceed();
        log.debug("{}, Return Value : {}", proceedingJoinPoint.getSignature(), retVal);
        return retVal;
    }
}
```
