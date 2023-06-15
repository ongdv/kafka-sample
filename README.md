# Kafka Test

Kafka 단일 노드 실행 및 SpringBoot HTTP 테스트 진행
본 소스코드는 [jjang-a 블로그](https://happy-jjang-a.tistory.com/201)의 포스트를 참고함

## Set Up
1. Kafka 실행
```shell
# Zookeeper, Kafka 실행
docker-compose up -d
```

2. 사전 작업 및 Kafka 테스트
```shell
# kafka 접속
docker exec -it kafka bash

# kafka 버전확인
kafka-topics.sh --version

# 토픽 생성
kafka-topics.sh --create --topic sample_topic_1 --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1

# 토픽 목록 조회
kafka-topics.sh --list --bootstrap-server localhost:9092

# 프로듀스 실행(토픽명: test_topic)
kafka-console-producer.sh --broker-list localhost:9092 --topic test_topic

# 컨슈머 실행
# --from-beginning 옵션은 해당 토픽의 맨 처음 메시지부터 확인 가능
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test_topic

# 토픽 정보 조회
kafka-topics.sh --bootstrap-server localhost:9092 --topic test_topic --describe

# application.properties
# kafka address
spring.kafka.bootstrap-servers=localhost:9092

```
3. 실행 후 테스트
```shell
# 2개의 kafka 터미널을 실행
# 각 터미널에 토픽명 입력
# docker exec -it kafka bash (1)
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic my_topic_1

# docker exec -it kafka bash(2)
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic my_topic_2

# HTTP 요청
curl http://localhost:8080/kafka/publish/mytopic1
curl http://localhost:8080/kafka/publish/mytopic2
```

4. 이슈 사항
블로그 포스트의 경우 Spring 버전 6 미만이라 ProducerController의 ListenableFuture가 Deprecated되기 전이고, 현재 [spring initializer](https://start.spring.io)의 경우 spring이 6버전 이상이므로, 변경해주어야 한다
- 기존 버전
```java
ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(myTopic1.name(), message);
future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
    @Override
    public void onSuccess(SendResult<String, String> result) {
        log.info("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
    }
    @Override
    public void onFailure(Throwable ex) {
        log.info("Unable to send message=[" + message + "] due to : " + ex.getMessage());
    }
});
```
- 6이후의 버전
```java
CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(myTopic2.name(), message);
future.whenComplete((result, error) -> {
    if(error == null){
        log.info("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
    }else{
        log.info("Unable to send message=[" + message + "] due to : " + error.getMessage());
    }
});
```