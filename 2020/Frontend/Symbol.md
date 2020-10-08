# Symbol

심벌은 Symbol()을 사용해서 생성한다. Symbol()은 호출할 때마다 새로운 값을 만든다.

```javascript
let sym1 = Symbol();
let sym2 = Symbol();
console.log(sym1 == sym2); // false
```

Symbol()에 인수를 전달하면 심벌에 설명을 덧붙일 수 있다.

```javascript
let HEART = Symbol("하트");
console.log(HEART.toString()); // -> Symbol(하트)
```

## 심벌과 문자열 연결하기

Symbol.for()를 활용하여 문자열과 연결된 심벌을 생성할 수 있다.

```javascript
let sym1 = Symbol.for("test");
let sym2 = Symbol.for("test");
console.log(sym1 == sym2); // true
```

심벌과 연결된 문자열은 `Symbol.keyFor()`로 구할 수 있다.

```javascript
let sym1 = Symbol.for("test");
let sym2 = Symbol("test");
console.log(Symbol.keyFor(sym1)); // -> test
console.log(Symbol.keyFor(sym2)); // -> undefined
```
