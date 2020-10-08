# 함수 리터럴(javascript)

javascript에서 함수는 함수 리터럴로도 정의할 수 있다. 예를 들어 거듭 제곱 함수 square를 다음과 같이 선언할 수 있다.

```javascript
let square = function(x) {
    return x * x;
};
```

코드에서 `function(x) {...}` 부분이 함수 리터럴이다. 함수 리터럴은 익명 함수 또는 무명 함수 라고도 부른다. 이렇게 선언한 함수 리터럴은 `square(25)`처럼 사용할 수 있다. 함수 리터럴로 정의한 익명함수는 변수에 할당한 후에 square라는 이름을 갖고, 그 이름으로 호출할 수 있게 된다.

그렇기 때문에 함수 선언전에 사용하려고하면 Uncaught ReferenceError가 발생한다.

```javascript
console.log(square(25)); // Uncaught ReferenceError: square is not defined
let square = function(x) {
    return x * x;
};
square(25); // 625
```

익명함수에도 이름을 붙일 수는 있으나, 함수 안에서만 유효하고 함수 바깥에서는 접근, 호출할 수 없다.

```javascript
let test = function testFunction(x) {
    return x + x;
};
test(5); // 10
testFunction(5); // Uncaught ReferenceError: testFunction is not defined
```

![Anonymous function](/2020/assets/img/anonymous_function_naming.png)
