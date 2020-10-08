# Web Storage API

Web Storage API는 브라우저에서 쿠키를 사용하는 것보다 훨씬 직관적으로 key/value 데이터를 저장한다.

Storage 객체는 단순 Key-Value Storage이다.

SessionStorage는 페이지의 세션이 유지되는 동안 사용할 수 있다. LocalStorage는 만료 기간이 없다.

Storage 객체는 장바구니나, 글 작성 중간에 일부 저장한다거나 하는 용도로 사용할 수 있다. 서버에 반드시 저장할 필요가 없을 때 사용한다.

```javascript
let myStorage = window.localStorage;
localStorage.setItem('myName', 'martin');
let name = localStorage.getItem('myName');

console.log(myStorage); // myName을 가지고 있는 key-value storage가 나옴

localStorage.removeItem('myName');
localStorage.clear(); // 아이템 전체 삭제
```

## 참고자료

- [MDN](https://developer.mozilla.org/ko/docs/Web/API/Window/localStorage)
