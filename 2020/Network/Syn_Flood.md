# Syn flooding

TCP 연결의 취약점을 이용한 공격이다. 기본적인 TCP의 연결 순서는 다음과 같다.

1. SYN (SRC)
2. SYN + ACK (DST)
3. ACK (SRC)

공격을 받는 DST 서버는 SYN + ACK을 보내고 ACK 응답이 올 때까지 무작정 기다리는 것은 아니고 Timeout까지 기다리는데, 기다리는 동안 그 연결에 대한 정보는 backlog queue에 저장한다. 그래서 이 timeout 시간보다 짧은 주기로 SYN 요청을 계속 보내게 되면, backlog queue가 쌓이게 되고 이로 인해 TCP 커넥션을 만들 수 없어 시스템이 마비될 수 있다.

이 상태에서는 서버 측에서는 SYN_RECV 상태의 소켓 상태들을 확인할 수 있다. DST 서버에서 SYN + ACK 송신 후 이 신호에 대해 ACK를 발생시키지 않게 하기 위해 SRC IP를 위조(Spoofing)하여 보낸다.(SYN에서)

공격 시나리오는 다음과 같다.

1. 공격자(A)가 공격 대상(B)에게 SYN 세그먼트를 보냄 (src ip spoofing)
2. B는 src ip를 대상으로 SYN+ACK을 보냄
3. SYN + ACK 을 보낸 목적지는 없는 대상이기 때문에, ACK가 오지 않는다..
4. ACK를 기다리는 timeout 시간까지는 backlog queue에 커넥션에 대한 정보가 쌓여있다.

위 1~4번 과정을 아주 짧은 시간내에 반복하는 것이 SYN Flooding을 이용한 공격이라고 한다.
