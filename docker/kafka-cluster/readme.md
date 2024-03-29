# Start Kafka Cluster


## Landoop 1 node kafka cluster
    ```
    cd docker/kafka-cluster
    docker compose up
    ```
**In case you want to run this kafka cluster on another machine, then change the
ADV_HOST from 127.0.0.1 to ipaddress of the host machine and broker address will change to host_id_address:9092**

#### UI : http://127.0.0.1:33030/
#### Kafka Broker: 127.0.0.1:9092
#### Schema Registry URL: http://127.0.0.1:8081


## Confluent 3 node kafka
<a href="https://docs.confluent.io/platform/current/platform-quickstart.html/" target="new">Confluent Kafka UI Guide</a>
<br><a href="https://developer.confluent.io/quickstart/kafka-docker/" target="new">Confluent Kakfa Quickstart Guide</a>

**With 1 Brokers**
    ```
    docker compose -f docker-compose-confluent.yml up -d
    ```

**With 3 Brokers**
    ```
    docker compose --profile kafka_cluster up -d
    ```