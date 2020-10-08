# let과 const

let은 변수, const는 한 번만 할당할 수 있는 상수를 선언한다.

## let

var와 차이점은 let으로 선언한 변수의 유효범위는 블록 안 이라는 점이다.

```javascript
let x = "outer";
{
    let x = "inner x";
    let y = "inner y"
    console.log(x); // inner x
    console.log(y); // inner y
}
console.log(x); // outer
```

또한 동일한 이름을 가진 변수를 let으로 선언하면 문법 오류가 발생한다.(동일한 블록 범위 내에서)

```javascript
let x = "ABCD";
let x = "EFGH"; // Uncaught SyntaxError: Identifier 'x' has already been declared
```

![Uncaught SyntaxError](/2020/assets/img/duplicated_let.png)

## const

const는 블록 유효 범위를 가지면서 한 번만 할당할 수 있는 변수이다. const로 선언한 상수는 let으로 선언한 변수처럼 동작하지만, 반드시 초기화 해야 한다.

```javascript
const c = 5;
c = 2; // Uncaught TypeError: Assignment to constant variable.
```

자바의 final과 비슷한 점은 상수로 선언한 값은 수정할 수 없지만, 값이 객체이거나 배열일 경우에는 프로퍼티 또는 프로퍼티 값을 수정할 수 있다.

```javascript
const origin = {x: 1, y: 2};
origin.x = 3;
console.log(origin); // {x: 3, y: 2}
```

![const object](/2020/assets/img/const_object.png)
