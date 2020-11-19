# Hadoop

하둡은 대용량 데이터를 적은 비용으로 빠르게 분석할 수 있게 해준다.

여러 개의 컴퓨터를 하나로 묶어(클러스터) 대용량 데이터를 처리하는 기술로, HDFS와 MapReduce 플랫폼으로 구성되어 시작되었으나 이제는 데이터 저장, 처리 같은 하둡 생태계 전반을 포함하는 의미로 확장되었다.

Cluster Manager로 YARN, Mesos, k8s등을 사용할 수 있다.

하둡에서 MapReduce 플랫폼은 결국에 데이터를 Map과 Reduce만으로 원하는 형태로 가공할 수 있는데, 이렇게 추상화가 너무 되어 있었기 때문에 데이터를 쉽게 가공하기 어려웠다. 그래서 아래서 다루는 apache spark와 같은 데이터 처리를 위한 엔진이 나오게 된 것 같다.

## Apache Spark

Apache Spark는 데이터 처리를 위한 엔진이다. 스칼라로 구현되어 있고, 다양한 언어(scala, java, python, R)를 지원한다.

데이터 처리 측면에서 볼 때, 빅데이터라고 해서 다른 것은 없다. 단순히 데이터를 읽어와서 원하는 작업을 수행하고, 그 결과를 저장하거나 외부 프로세스로 전달하면 되는 것이다.

Spark에 대해 조금 찾아보면, RDD(Resilient Distributed Dataset)와 DAG(Directed Acyclic Graph) execution engine에 대해 많이 나온다.

좀 더 찾아보면 RDD는 요즘 안 쓰고 DataFrame, Dataset이 많이 나온다는 것을 찾을 수 있다. 이 내용들에 대해서는 좀 더 찾아보고 아래 내용을 추가한다.

### RDD

### DAG

### DataFrame, Dataset
