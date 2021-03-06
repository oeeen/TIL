# Cookie 와 Session

## Cookie

쿠키는 서버에서 클라이언트에게 전송하는 데이터다. 브라우저는 이 데이터를 저장해놓고 다음 요청에 담아서 함께 전송한다. 보통 동일한 사용자에게서 들어온 요청인지 확인 할 때 사용한다.

쿠키는 주로 아래 세 가지 목적을 위해 사용한다.

1. 세션 관리(Session management)
    - 서버에 저장해야 할 로그인, 장바구니, 게임 스코어 등의 정보 관리
2. 개인화(Personalization)
    - 사용자 선호, 테마 등의 세팅
3. 트래킹(Tracking)
    - 사용자 행동을 기록하고 분석하는 용도

## Session

일반적으로 세션은 사용자(클라이언트)를 구분하는 용도로 사용된다. 세션은 클라이언트가 로그인 한 시점부터 브라우저를 종료한 시점까지 들어오는 요청을 하나의 상태로 보고 관리하는 것이다. 클라이언트가 접속해 있는 상태를 하나의 세션이라고 할 수 있다. 세션은 서버 측에서 관리를 한다. 보통 서버에서는 클라이언트를 구분하기 위해서 Session ID를 쿠키에 넣어 구분한다.

### Session의 동작 순서

1. 클라이언트가 서버에 처음으로 Request를 보냄 (첫 요청이기 때문에 session id가 존재하지 않음)
2. 서버에서는 session id 쿠키 값이 없는 것을 확인하고 새로 발급해서 응답
3. 이후 클라이언트는 전달받은 session id 값을 매 요청마다 헤더 쿠키에 넣어서 요청
4. 서버는 session id를 확인하여 사용자를 식별
5. 클라이언트가 로그인을 요청하면 서버는 session을 로그인한 사용자 정보로 갱신하고 새로운 session id를 발급하여 응답
6. 이후 클라이언트는 로그인 사용자의 session id 쿠키를 요청과 함께 전달하고 서버에서도 로그인된 사용자로 식별 가능
7. 클라이언트 종료 (브라우저 종료) 시 session id 제거, 서버에서도 세션 제거
