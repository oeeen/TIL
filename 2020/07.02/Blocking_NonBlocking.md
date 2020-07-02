# Blocking vs. Non Blocking

Blocking과 Non Blocking의 관심사는 호출되는 함수(callee)가 바로 리턴하느냐 이다.

1. callee가 바로 리턴하여 자신을 부른 caller 함수에게 제어권을 넘겨주고 caller 함수가 다른 일을 할 수 있게 하는 것이 Non Blocking이다.
2. callee가 함수가 끝날 때까지(자신이 모든 작업을 마칠 때까지) 제어권을 넘겨주지 않고 caller는 대기하는 것이 Blocking이다.
