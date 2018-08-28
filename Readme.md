#  Akka-Cluster-CheatSheet

Sammelecke für Minimalbeispiele für Akka-Cluster-Zeug

+ Singleton
+ Sharding
+ CRDTs

### Bauen
`sbt clean compile docker:publishLocal`

### Hochfahren
`docker-compose up`

### Skalieren
In einem anderen Terminal
`docker-compose scale node=2`