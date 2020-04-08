# 인증

## 토큰 방식 vs. 세션 방식

### 토큰 방식

#### JWT (Json Web Token)

JWT 는 과거에 TIL에 기록한 부분을 복사해왔다.

JSON Web Token은 AAAAAAAA.BBBBBBBBB.CCCCCCC 세 부분으로 나누어진다.

여기서 AAAAAAAA부분은 JOSE(JSON Object Signing and Encryption) 헤더, JWT Claim set, Signature 이다.

이 토큰을 어떻게 만드는지를 알아본다. RFC7519 - 7.1 Createing a JWT 부분을 보면 다음과 같다.

7.1. Creating a JWT

JWT를 만들기 위해서는 다음과 같은 단계가 수행되어야 한다. 각 스텝의 input, output사이에는 아무련 의존성이 없다.(순서가 중요하지 않다.)

1. 원하는 Claim을 포함하는 JWT Claim을 만든다. 공백이 허용되고, 인코딩 전에 명시적으로 정규화 할 필요 없다.
2. JWT Claims Set의 UTF-8 형식으로 바꿔라
3. 원하는 헤더를 포함하는 JOSE Header를 만든다. JWT는 JWS 나 JWE 스펙을 따라야 한다. 공백이 허용되고, 인코딩 전에 명시적으로 정규화 할 필요 없다.
4. JWT가 JWS 냐, JWE에 기반하냐에 따라서 두가지 경우가 있다.
   - JWS 기반이면, JWS Payload로 JWS를 만든다. JWS 스펙에 맞게 생성해야한다.
   - JWE 기반이라면, JWE를 위해 plaintext로 메세지를 만들어서 JWE로 사용한다. JWE 스펙에 맞게 생성한다.
5. 서명 또는 암호화 작업이 수행되면, 메시지를 JWS 또는 JWE로 하고, 해당 단계에서 작성된 새로운 JOSE 헤더에서 “cty”(콘텐츠 유형) 값을 사용하여 3단계로 돌아간다.
   - 그렇지 않으면 결과 JWT를 JWS 나 JWE로 리턴한다.

JWS 스펙에 나와있는 토큰의 예시를 살짝 바꿔본 결과는 다음과 같다.

Header

```json
{
    "typ":"JWT",
    "alg":"HS256"
}
```

이를 Base64로 인코딩 하면 eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9 이 나온다. 이렇게 나온 부분이 AAAAAAAA 부분이다.

Payload

```json
{
  "iss":"martin",
  "exp":1300819380,
  "http://example.com/is_root":true
}
```

이를 Base64로 인코딩 하면 eyJpc3MiOiJtYXJ0aW4iLCJleHAiOjEzMDA4MTkzODAsImh0dHA6Ly9leGFtcGxlLmNvbS9pc19yb290Ijp0cnVlfQ 이 나온다.

그리고 마지막 signature에는 위의 Header.Payload를 우리가 지정한 Secret과 SHA-256으로 암호화 해서 base64로 인코딩 한다.

나는 martin이라는 secret을 사용해서 암호화 하니 mSETIt5sw3WXtHnJoczWfZJ0O6hrF6F7jT7QKW0yRXQ와 같은 결과가 나왔다. (jwt.io사이트를 이용했다.)

그러면 최종적으로 위에서 말한 AAAAAAAA.BBBBBBBBB.CCCCCCC 형식의 JWT Token이 만들어진다.(아래와 같음)

eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9
.
eyJpc3MiOiJtYXJ0aW4iLCJleHAiOjEzMDA4MTkzODAsImh0dHA6Ly9leGFtcGxlLmNvbS9pc19yb290Ijp0cnVlfQ
.
mSETIt5sw3WXtHnJoczWfZJ0O6hrF6F7jT7QKW0yRXQ

#### 토큰 방식 장점

1. 간편하다. 별도의 세션 저장소가 필요 없다.
2. Stateless

#### 토큰 방식 단점

1. 한번 발급된 토큰은 만료될 때까지 없앨 수 없다.
2. Access Token의 유효기간을 짧게 하고 Refresh를 계속 할 수 있으나, 비효율 적이다.
3. 암호화 하는 것이 아니기 때문에 중요한 정보들을 그대로 담을 수 없다.

### 세션 방식

기본적으로 HTTP는 stateless한 프로토콜이다. 그래서 사용자 로그인이라거나 상태의 유지가 필요한 작업을 하기 위해 세션과 쿠키를 활용한다.

세션 방식으로 로그인을 처리하는 흐름은 다음과 같다.

1. 사용자가 로그인 요청
2. 서버는 해당 사용자의 요청을 DB에서 확인하고, 새로운 Session ID를 생성하여 세션 저장소(보통은 Redis 서버를 쓰는 것 같다)에 저장한다.
3. Response Header에 Session ID를 포함하여 응답한다.
4. 사용자는 해당 Session ID를 쿠키에 저장한다.
5. 사용자 인증이 필요할 때마다 해당 Session ID를 이용하여 인증한다.
6. 서버는 세션 서버에서 해당 세션의 유효성을 검사하여 이후 처리를 한다.

#### 장점

1. 노출되더라도 쿠키 자체는 아무런 정보도 없다.

#### 단점

1. 쿠키가 탈취되어 SessionId가 노출되어 세션 아이디로 해당 사용자 인 척하는 공격이 발생할 수 있다. (Session Hijacking?)
2. 별도의 세션 저장소가 필요하다. (보통 redis나..)
3. 쿠키는 단일 도메인 및 서브 도메인에서만 작동하도록 설계되어 여러 도메인에서 관리하기 어렵다.

## OAuth 2.0

상세하게 설명하면 너무 길어지니, 내가 개발하는 서버단과 Kakao Login API 서버, 그리고 사용자 입장에서의 로그인 과정 흐름만 파악하여 정리한다.

### 로그인 과정

내가 개발하는 서버를 Martin 이라고 하고, Kakao loign API를 사용, 사용자는 oeeen이라는 사람이라고 해보자.

사전작업으로 일단 Martin이라는 어플리케이션은 kakao api에 앱을 등록해야 한다. 이 등록과정에서 Client Id, Client Secret, Redirect URL을 설정한다.

1. 사용자 oeeen은 Martin app에서 kakao로 login 버튼을 누른다.
2. Martin App은 카카오 서버로 Client ID, Redirect URL을 포함해서 oeeen이라는 사용자를 보낸다. 예를 들어 다음과 같은.. `https://resource.server/?client_id=1&scope=B,C&redirect_uri=https://smjeon.dev/done`
3. 카카오 서버는 Client ID가 있는지 확인하고, Redirect URL이 일치하는지 확인 후, 일치한다면 요청한 권한을 Martin app에 허용 할 것이냐라는 메세지를 보여준다.
4. 허용 한다면, 카카오 서버는 이 정보를 기억해둔다.
5. 이제 카카오 서버는 인증 코드(Authorization code)를 담아서 oeeen 이라는 사용자를 app이 설정해둔 redirect url로 redirect 시킨다.
6. Martin App은 oeeen으로부터 authorization code를 받는다.
7. Martin App은 받은 Authorization code를 가지고, Client Id, Client Secret, Authorization Code를 가지고 카카오 서버에 사용자 oeeen에 대한 Access Token을 요청한다.
8. Martin App은 받은 Access Token을 가지고 카카오 서버에 사용자 oeeen의 권한을 얻을 수 있다.

참고로 카카오 로그인 API의 경우는, 설정 > 고급 > Client Secret에서 생성한 client_secret 코드 active 상태일 경우에는 필수로 설정해야 한다.

추가적으로 refresh_token은 설정된 유효기간 만큼 유효하고, refresh token의 만료가 1달 이내로 남은 시점에서 사용자 토근 갱신 요청을 하면 갱신된 access token과 갱신된 refresh token이 함께 반환된다. 요청은 다음과 같이 한다.
