package com.example.kafkatest.springdemo;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserLogConsumer2 {
    @KafkaListener(id="c3",groupId = "aaa",topics={"test22"})
            //topicPartitions =
           // {@TopicPartition(topic = "test22")})
    //, partitions = { "0" }
    public void consumer1(ConsumerRecord<?,?> consumerRecord){
        System.out.println("分区"+consumerRecord.partition());
        //判断是否为null
        Optional<?> kafkaMessage = Optional.ofNullable(consumerRecord.value());
        if(kafkaMessage.isPresent()){
            //得到Optional实例中的值
            Object message = kafkaMessage.get();
            System.err.println("消费消息dsadsa1:"+message);
        }
    }
}
