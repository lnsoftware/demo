package com.example.consumer;

import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * RabbitMQ 启动和停止对应Container
 * https://blog.csdn.net/weixin_42338555/article/details/83860870
 * RabbitMq 配置多个RabbitMQ的连接
 * https://www.jb51.net/article/116944.htm
 */

@SpringBootApplication
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

}
