# 인가

인증 된 사용자에게 어떤 것이 허가되었는지.. 예를 들어 ADMIN 권한을 가진 사람은 어떤 resource에 접근할 수 있는지.

## 시큐리티가 지원하는 권한 계층

1. 웹 계층 (URL 요청에 따른 메뉴, 화면단위의 보안)
2. 서비스 계층 (메소드 같은 기능 단위 보안)
3. 도메인 계층 (Access Control List, 객체 단위의 레벨 보안)

## FilterSecurityInterceptor

마지막에 위치한 필터, 인증된 사용자에 대해 요청의 승인/거부 여부를 결정한다.

1. 인증 객체 없이 자원에 접근 시도할 경우 AuthenticationException 발생
2. 권한 없이 자원에 접근할 경우 AccessDeniedException 발생
3. 권한 처리는 AccessDecisionManager에서 처리한다.

## AccessDecisionManager

인가 여부를 결정하고 접근을 승인/거부 하는 클래스, 여러개의 Voter들을 가질 수 있고 각 Voter로부터 승인/거부/보류를 리턴 받고 판단한다.

### 접근결정 유형

1. AffirmativeBased - Voter 중 하나라도 허가나면 허가
2. ConsensusBased - 다수결로 결정, 동수일 경우 기본은 접근허가, allowIfEqualGrantedDeniedDecisions = false로 하면 접근 거부
3. UnanimousBased - 만장일치로 허가가 나야 접근 허가

각 클래스들의 decide메서드의 구현을 보고 읽기만 해도 어떤 내용인지 이해가 된다.

```java
public class AffirmativeBased extends AbstractAccessDecisionManager {
    public void decide(Authentication authentication, Object object,
            Collection<ConfigAttribute> configAttributes) throws AccessDeniedException {
        int deny = 0;

        for (AccessDecisionVoter voter : getDecisionVoters()) {
            int result = voter.vote(authentication, object, configAttributes);

            if (logger.isDebugEnabled()) {
                logger.debug("Voter: " + voter + ", returned: " + result);
            }

            switch (result) {
            case AccessDecisionVoter.ACCESS_GRANTED:
                return; // 하나라도 승인나면 끝난다.

            case AccessDecisionVoter.ACCESS_DENIED:
                deny++;

                break;

            default:
                break;
            }
        }
...
}

public void decide(Authentication authentication, Object object,
            Collection<ConfigAttribute> configAttributes) throws AccessDeniedException {
        int grant = 0;
        int deny = 0;

        for (AccessDecisionVoter voter : getDecisionVoters()) {
            int result = voter.vote(authentication, object, configAttributes);

        if (logger.isDebugEnabled()) {
            logger.debug("Voter: " + voter + ", returned: " + result);
        }

        switch (result) {
        case AccessDecisionVoter.ACCESS_GRANTED:
            grant++; // 승인

            break;

        case AccessDecisionVoter.ACCESS_DENIED:
            deny++; // 거부

            break;

            default:
                break;
            }
        }

        if (grant > deny) {
            return; // 승인이 더 많을 경우엔 return
        }

        if (deny > grant) { // 거부가 더 많기 때문에 AccessDeniedException 발생
            throw new AccessDeniedException(messages.getMessage(
                    "AbstractAccessDecisionManager.accessDenied", "Access is denied"));
        }

        if ((grant == deny) && (grant != 0)) { // 동수일 경우, allowIfEqualGrantedDeniedDecisions 값에 따라 결정된다.
            if (this.allowIfEqualGrantedDeniedDecisions) {
                return;
            }
            else {
                throw new AccessDeniedException(messages.getMessage(
                        "AbstractAccessDecisionManager.accessDenied", "Access is denied"));
            }
        }

        // To get this far, every AccessDecisionVoter abstained
        checkAllowIfAllAbstainDecisions();
    }
}
public class UnanimousBased extends AbstractAccessDecisionManager {
    public void decide(Authentication authentication, Object object,
            Collection<ConfigAttribute> attributes) throws AccessDeniedException {

        int grant = 0;

        List<ConfigAttribute> singleAttributeList = new ArrayList<>(1);
        singleAttributeList.add(null);

        for (ConfigAttribute attribute : attributes) {
            singleAttributeList.set(0, attribute);

            for (AccessDecisionVoter voter : getDecisionVoters()) {
                int result = voter.vote(authentication, object, singleAttributeList);

                if (logger.isDebugEnabled()) {
                    logger.debug("Voter: " + voter + ", returned: " + result);
                }

                switch (result) {
                case AccessDecisionVoter.ACCESS_GRANTED:
                    grant++;

                    break;

                case AccessDecisionVoter.ACCESS_DENIED: // 하나라도 인가 거부면, AccessDeniedException 발생
                    throw new AccessDeniedException(messages.getMessage(
                            "AbstractAccessDecisionManager.accessDenied",
                            "Access is denied"));

                default:
                    break;
                }
            }
        }

        // To get this far, there were no deny votes
        if (grant > 0) {
            return;
        }

        // To get this far, every AccessDecisionVoter abstained
        checkAllowIfAllAbstainDecisions();
    }
}
```

## AccessDecisionVoter

접근을 허가/거부할 지 판단하는 곳, Authentication, FilterInvocation, ConfigAttributes를 받아서 판단한다.

```java
public interface AccessDecisionVoter<S> {
    // ~ Static fields/initializers
    // =====================================================================================

    int ACCESS_GRANTED = 1;
    int ACCESS_ABSTAIN = 0;
    int ACCESS_DENIED = -1;
```

## 전체 흐름

1. FilterSecurityInterceptor 는 인증 여부를 확인
    - 인증 객체가 없다면, AuthenticationException을 발생시킴
2. 인증 객체가 존재한다면 SecurityMetadataSource에서 필요한 권한 정보를 조회해서 전달
    - 권한 정보가 없으면 권한 심사를 하지 않고 자원에 접근을 허용한다.
3. 권한 정보가 있으면 AccessDecisionManager 클래스에서 인가 여부를 확인하고 각 AccessDecisionVoter 에서 승인/거부 한다.
4. 최종적으로 AccessDecisionManager에서 접근을 허용/거부한다.
