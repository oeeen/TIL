# 객체의 기초

## 객체 리터럴

자바 스크립트에서는 원시 타입을 제외한 모든 값이 객체이다. 객체는 객체 리터럴과 생성자로 생성할 수 있다.

```javascript
let car = {name: "MyCar", position: 10};
```

여기서 `{...}`부분이 객체 리터럴이고 객체 리터럴을 변수 car에 대입한다. 객체 안의 프로퍼티를 읽거나 쓸 때는 . 나 []를 사용할 수 있다. 만약 객체에 없는 프로퍼티를 읽으려고 하면 undefined를 반환한다.

```javascript
console.log(car.name); // MyCar
console.log(car["position"]); // 10
console.log(car[position]); // Uncaught ReferenceError: position is not defined
console.log(car.price); // undefined
```

in 연산자로 해당 객체에 프로퍼티가 포함되어 있는지 확인할 수 있다.

```javascript
let car = {name: "MyCar", position: 10};
console.log("name" in car); // true
console.log("price" in car); // false
```

그리고 `console.log("toString" in car);` 도 true로 나오는데, car는 Object 객체를 상속 받았기 때문에 toString도 존재한다.

## 생성자

```javascript
function MovedCar(name, position) {
    this.name = name;
    this.position = position;
}
```

`let car = new Car("MyCar", 10);` 이런 코드로 객체를 생성하고 객체의 참조가 변수 car에 할당된다. new 연산자로 객체를 생성할 것이라고 기대하고 만든 함수를 생성자라고 부른다. 위 코드는 다음과 같이 객체 리터럴로 고쳐쓸 수 있다.

```javascript
let car = {};
car.name = "MyCar";
car.position = 10;
```

자바 스크립트에는 클래스가 없다. 생성자 자체가 함수 객체라는 실체이다. 그래서 자바스크립트 생성자로 생성한 객체는 엄밀히 말해 인스턴스가 아니다. 그러나 생성자가 클래스처럼 객체를 생성하는 역할을 하므로 생성자로 생성한 객체로 인스턴스라고 부르는 것이 관례이다.

## 내장 객체 (Built-in Object)

자바스크립트에서 사용할 수 있는 Built-in 생성자는 굉장히 많이 있다.(Object, String, Number, Boolean, Array, Date, Function, RegExp, Error, EvalError, InternalError, RangeError, ReferenceError, SyntaxError, TypeError, URIError, Symbol, Int8Array8, Uint8Array8, Int16Array16, Uint16Array16, Int32Array32, Uint32Array32, Float32Array32, Float64Array64, ArrayBuffer, DataView, Promise, Generator, GeneratorFunction, Proxy, Map, Set, WeakMap, WeakSet)

## 자바스크립트 객체의 분류

자바스크립트 객체는 크게 네이티브 객체, 호스트 객체, 사용자 정의 객체로 나눌 수 있다.

- 네이티브 객체는 위에서 말한 Built-in Object, JSON, Math, Reflect 등
- 호스트 객체는 자바스크립트 실행 환경에 정의된 객체로써 브라우저 객체(Window, Navigator, History, Location 등), DOM에 정의되어 있는 객체, XMLHttpRequest 등
- 사용자 정의 객체는 말 그대로 사용자가 직접 정의한 객체이다.

## 참고자료

- 모던 자바스크립트 입문 - 4장
