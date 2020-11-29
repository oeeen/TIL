# SNI (Server Name Indication)

## RFC3546 번역

TLS는 클라이언트가 통신하고자 하는 서버의 이름을 클라이언트에게 알려주지 않는다. 클라이언트는 단일 네트워크 주소위에 여러 개의 가상 서버를 호스팅하는 서버에 보안 연결을 이용해서 이러한 정보들을 받는 것을 원한다.

서버 이름을 받기 위해서 클라이언트는 client hello? 내에 server_name이라는 타입의 확장을 포함해야할 것이다. 이 확장 필드 `extension_data`는 아래와 같은 `ServerNameList`를 포함해야 할 것이다.

```c
struct {
    NameType name_type;
    select (name_type) {
        case host_name: HostName;
    } name;
} ServerName;

enum {
    host_name(0), (255)
} NameType;

opaque HostName<1..2^16-1>;

struct {
    ServerName server_name_list<1..2^16-1>
} ServerNameList;
```

현재 지원되는 유일한 서버네임은 DNS hostname이지만, 이것이 TLS가 DNS에 종속성이 있다는 것을 의미하지는 않고 다른 이름 타입이 미래에 추가될 수 있다.(이 문서를 업데이트하는 rfc에 의해서) TLS는 제공된 서버이름을 불투명? 데이터로 취급하고 이름과 타입을 어플리케이션에 전달할 수 있다.

~~"HostName"에는 클라이언트가 인식하는 서버의 정규화 된 DNS hostname이 포함된다. hostname은 뒤에 따라오는 점 없이 UTF-8 인코딩을 사용하는 byte string으로 표현된다.~~

`server_name` 확장을 포함하는 클라이언트 hello?를 받는 서버는 확장에 포함된 정보를 사용해서 클라이언트에 반환할 적절한 인증서나 보안 정책의 다른 것을 선택할 수 있다. 이 이벤트에서 서버는 서버 hello에서 `server_name` 유형의 확장을 포함해야한다. `extension_data`필드는 비어있어야 한다.

서버가 클라이언트 hello의 확장을 이해했지만, 서버 이름을 인식하지 못한다면 `unrecognized_name` 경고를 보내야 한다.(which MAY be fatal)

## 정리

1. TLS의 확장 표준, 인증서에 사용한다.
2. 위에 나온 단일 네트워크 주소 위에 여러 개의 가상 서버를 호스팅하는 서버들도 인증서를 사용한 HTTPS를 활성화 할 수 있다.
3. 클라이언트가 server_name을 포함하는 요청?을 보내면 서버에서는 클라이언트에게 적절한 인증서를 보낼 수 있다. 이 응답으로 서버 hello에서는 반드시 `server_name`확장을 포함해야한다.

일반적으로 DNS Server에 hostname을 묻고, 그에 대한 응답으로 원하는 Web server로 찾아가게 된다. 이렇게 원하는 web server로 찾아갈 때 맨 처음 암호화 이전에 핸드쉐이크에서 server_name을 포함한 요청을 보내기 때문에, 클라이언트가 어떤 사이트에 요청을 보내는지 알아낼 수 있다. 그래서 TLS 1.3 의 확장인 Encrypted SNI를 발표했고, 클라이언트의 요청에서 server_name을 알아낼 수 없게 했다.

## 참고자료

- [rfc3546 - 3.1. Server Name Indication](https://tools.ietf.org/html/rfc3546#section-3.1)
