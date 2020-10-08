# Swagger는 무엇인가

API서버를 개발하면, 그 서버가 어떤 Spec을 가진 데이터를 주고 받는지에 대한 문서작업이 꼭 필요하다. 근데 문서작업은 귀찮고... 시간이 많이 든다. 그래서 나온 API Spec 자동화 툴이다.

> Swagger는 간단한 설정으로 프로젝트에서 지정한 URL들을 HTML로 확인할 수 있게 해준다.

소스코드에 약간의 설정을 해주고 localhost:8080/swagger-ui.html로 접속해보면, API 문서를 확인할 수 있게 된다. 또한 Postman을 사용하는 것처럼 API call들을 테스트 해볼 수도 있다.

Swagger에서는 해당 메서드가 받을 수 있는 데이터 schema도 parameter 화면에 보여주고 있으므로, 그대로 사용하면 된다. **실제 프로젝트에 적용해보고 다시 작성하자.**

## 참고자료

- [이동욱님 블로그](https://jojoldu.tistory.com/31)
