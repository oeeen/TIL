# Elasticsearch

인턴 과제를 진행하면서 Elasticsearch 를 사용할 기회가 많이 있었다. 그러나 NCP 내의 서비스를 사용한다거나, 사내에 간단하게 클러스터를 구성하여 사용할 수 있는 방법을 사용했기 때문에 근본적으로 어떤 기술인지 파악하지는 못한 것 같아서 정리한다.

## 정의

간단하게 말해서 정형, 비정형 데이터 등의 모든 유형의 데이터를 위한 분산형 오픈 소스 검색 및 분석 엔진이다. 텍스트, 숫자, 위치정보 등의 데이터들도 담을 수 있고, 아파치 루씬 기반으로 구축된다. 보통 Elasticsearch, Logstash, Kibana 를 기본적으로 많이 사용한다.

간단하게 말하면 Elasticsearch는 우리가 흔히 접할 수 있는 MySQL과 같은 데이터베이스라고 생각하면 된다. 데이터를 elasticsearch에 전송하면 elasticsearch는 이 데이터를 수집하면서 인덱싱되기전에 구문 분석, 정규화, 강화를 한다고 한다.

Elasticsearch는 기본적으로 key, value storage라고 생각하면 된다. JSON 형태로 데이터를 저장하고 키와 그에 해당하는 값을 서로 연결한다.

Kibana에서는 이 Elasticsearch의 데이터들을 시각화하고, 대시보드를 공유하고, Elastic stack을 관리할 수 있다.

### 인덱스

ES의 인덱스는 서로 관련되어 있는 document의 모음이라고 할 수 있다. RDB에서의 용어로 말하자면 DB라고 생각하면 된다. ES에서는 반전된 인덱스라는 데이터 구조를 사용해서 Full text search를 아주 빠르게 할 수 있도록 한다고 한다.. 아직 근본적인 방식은 모르겠다.

### Logstash

로그스태시는 데이터를 집계, 처리하여 ES로 전송하는데 사용한다. Logstash는 서버 사이드 오픈소스 데이터 처리 파이프라인이다. 이를 사용해서 다양한 소스에서 동시에 데이터를 수집하고 후처리를 하여 ES에서 인덱싱 되도록 할 수 있다.

### Kibana

Kibana는 ES를 위한 시각화, 관리도구다. 히스토그램, 선 그래프, 파이차트, 지도 등 다양한 시각화 방식을 제공한다. 사용해본 결과 어떤 것들을 사용할 수 있는지, 그리고 현재 내 데이터에서 어떤 데이터들을 가지고 어떤 그래프를 그려야 할지를 정하고 사용한다면 매우 강력한 툴이 될 것 같다.

## 장점

1. 빠르다.
    - Full text search가 빠르다.
    - 보통 document가 저장되고, 검색 가능할 때까지 대기 시간이 아주 짧다.
    - 위와 같은 이유로 time critical한 사용사례에 적합하다.
2. 분산처리
    - ES에 저장된 document는 shard라고 하는 여러 다른 컨테이너에 분산 저장되고 샤드는 복제된다.
3. Logstash, Kibana와 같은 환경
    - 쉽게 접근하고 사용할 수 있는 환경을 제공한다.
4. 넓은 생태계
    - 벌써 많은 기업, 개인들이 사용하고 있어서 쉽게 용례를 찾아볼 수 있고 정보를 얻을 수 있는 곳이 많다.

## 내가 사용했던 사례

### 1차 과제

1차 과제에서는 어떤 정보를 저장하는 용도로만 사용했다. RDB로도 충분히 가능하고, 오히려 더 빠르게 만들 수 있었겠지만 사용한 이유는 앞으로의 확장성과 이 데이터를 더 활용할 수 있도록 만들기 위해서 사용했다. ES에 일단 데이터를 쌓아놓는 것만으로도 데이터의 시각화를 더 간단하게 할 수 있고, 데이터를 가공하여 사용하기에 더 편해질 것이라고 생각해서 사용했다.

### 2차 과제

2차 과제에서는 어떤 필터링에 걸린 데이터들에 대한 정보를 모두 저장하기 위해서 ES를 사용했다. 데이터로 들어갈 수 있는 내용이 말 그대로 비정형 JSON 형태였고, 이 데이터들을 모두 저장해두기 위해서 사용했다. RDB를 사용했다면 이 중에서 선별하여 데이터를 저장해야 했을 것이다. 그러나 그렇게 저장했을 때, 추후에 더 데이터가 필요한 경우에 문제가 될 수 있다. 데이터가 더 필요하게 되어 필드를 추가한다고 하더라도 추가 시점 이전의 데이터들에는 데이터가 들어가있지 않기 때문에 이 데이터에 대한 로그를 볼 수는 없는 것이다.

ES에서 데이터들을 수집하여 이 데이터들을 이용하여 시간 단위로 데이터를 시각화 하는데 사용할 수 있었고, 이에 들어가는 리소스를 줄일 수 있었다고 생각한다. RDB를 사용했다면 이 데이터를 활용하여 어떤 그래프를 그리거나 어떤 output을 만들어내기 위해 간단하게라도 프론트페이지를 만들거나 로그를 뿌려주는 추가적인 리소스가 필요했겠지만, ES를 사용하면서 Kibana를 이용하여 간단하게 시각화 할 수 있었다. Kibana에서 제공하는 시각화 도구가 엄청나게 유용하고 강력하기 때문에, 기본적으로 제공하는 이것들만 잘 활용하더라도 데이터를 가공하여 시각적으로 잘 활용할 수 있을 것 같다.
