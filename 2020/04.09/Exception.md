# Checked Exception vs. Unchecked Exception

## Checked Exception

Checked Exception은 반드시 처리 해야하는 Exception(try-catch나 throws를 통해 상위로 던지거나 해야함)

대표적으로 SQLException, IOException이 있음. Transaction rollback하지 않는다.

Checked Exception은 복구 가능한 Exception이므로 try-catch 구문에서 catch에서 복구 과정을 거치기 때문에 transaction을 rollback 할 필요가 없다고 생각하는 것 같다.

## Unchecked Exception

Runtime Exception을 상속한다. 보통 custom exception은 runtime exception을 상속하여 unchecked exception으로 만든다.

회원가입에서 중복 아이디를 입력했을 경우 DuplicatedIdException 같은 예외를 만들어 사용자로부터 재입력을 받도록 처리해야한다. JPA를 사용하지 않고 중복 아이디를 입력했을 때 무작정 Insert할 경우 SQLExeption이 발생할 것이다. SQLExeption이 발생하면 이 Exception을 catch하여 unchecked Exception으로 바꾸어 명확한 예외를 명시해주고, 이에 대한 처리를 해야한다.

JPA를 사용한다면 내부에서 Unchecked Exception으로 변경하여 던지고 있기 때문에, 직접 checked exception을 처리할 필요가 없는 것이다.
