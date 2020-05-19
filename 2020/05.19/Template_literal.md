# Template Literal

## 템플릿 리터럴

템플릿 리터럴은 역따옴표(\`)로 묶은 문자열이다. 전에 자바스크립트로 게시글 템플릿(HTML 태그들..)을 만들 때 역 따옴표를 사용했었다. 예시를 보면 다음과 같다. 실제 사용했던 코드의 일부만 가져왔다.

```javascript
const article = `<div class="card widget-feed" data-object="article" data-article-id="{{id}}">
                    <div class="feed-header">
                        <ul class="list-unstyled list-info">
                            <li class="float-right mrg-right-40">
                                <div id="range-icon-{{id}}" class="ti-lock font-size-20"></div>
                            </li>
                        </ul>
                    </div>
                </div>`
```

템플릿 리터럴 안에는 플레이스 홀더를 넣을 수 있다. 플레이스 홀더는 ${...}로 표기한다. 자바스크립트 엔진은 플레이스 홀더 내부의 ...을 표현식으로 간주하여 evaluation 한다.

책의 예시를 살펴보면 다음과 같다.

```javascript
let a = 2;
let b = 3;
console.log(`${a} + ${b} = ${a+b}`); // 2 + 3 = 5
let now = new Date();
console.log(`오늘은 ${now.getMonth() + 1} 월 ${now.getDate()} 일 입니다.`); // 오늘은 5 월 19 일입니다.
```

![Template literal](/2020/assets/img/template_literal.png)

플레이스 홀더는 사용자의 입력 값처럼 실행 시점에 외부에서 주어지는 값을 표현식에 반영하고자 할 때 그것이 들어갈 수 있도록 마련한 장소이다.
