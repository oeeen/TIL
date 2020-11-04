# MongoDB Write Concern

Write concern은 standalone mongod, replica set 또는 샤드 클러스터에 대한 쓰기 작업을 위해 MongoDB로부터 요청된 인지? 수준을 나타낸다고 보면 된다. Sharded Cluster에서는 mongos 인스턴스는 write concern을 shard에 넘긴다.

MongoDB 4.4부터는 Global Write concern을 지원한다. 명시적으로 write concern을 지정하지 않은 작업은 global write concern을 따라간다.

## Write Concern specification

```json
{ w: <value>, j: <boolean>, wtimeout: <number> }
```

1. w 옵션은 쓰기 작업이 몇 개의 mongod 인스턴스에 전파될 지의 개수에 대한 옵션이다.
2. j 옵션은 디스크 저널에 쓰기 작업이 기록될 지 안될 지에 대한 옵션이다.(boolean)
3. wtimeout 옵션은 쓰기 작업에 대한 타임 아웃이다.

예를 들어, `{ w: 1, j: true, wtimeout: 0 }`은 replica set에서 primary mongod instance까지만 write 작업이 전파되면 client에 응답을 줄 수 있다.

만약 w가 2라면, replica set에서 primary와 secondary중 1개까지의 write 작업이 전파 되어야 한다.
