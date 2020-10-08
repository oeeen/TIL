# Sync vs. Async, Blocking vs. Non-blocking

- Synchronous
  - caller가 callee의 return을 기다리거나 caller가 계속 callee의 작업 완료 여부를 신경씀
- Asynchronous
  - callee에 callback 전달, callee 함수의 작업이 완료되면 callback 실행, caller 함수는 작업 완료를 신경쓰지 않음
- Blocking
  - 호출된 함수가 자신의 작업을 모두 마칠 때까지 제어권을 넘겨주지 않고, 대기하게 만든다.
- Non-blocking
  - 호출된 함수가 바로 리턴해서 호출한 함수에게 제어권을 넘겨주고, 호출한 함수가 다른 일을 할 수 있다.
