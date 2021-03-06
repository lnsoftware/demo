package com.example.provider.event;

import org.springframework.amqp.rabbit.connection.ConnectionBlockedEvent;
import org.springframework.amqp.rabbit.connection.ConnectionUnblockedEvent;
import org.springframework.amqp.rabbit.core.BrokerEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author zhangxuepei
 * @since 3.0
 */
@Component
public class RabbitBlockedListener {
    /**
     * 处理ConnectionBlockedEvent监测事件
     * @param connectionBlockedEvent RabbitMq队列阻塞事件
     */
    @EventListener
    public void handleConnectionBlockedEvent(ConnectionBlockedEvent connectionBlockedEvent){
        System.out.println("链接阻塞了, reason:"+connectionBlockedEvent.getReason());
    }
    @EventListener
    public void handleConnectionUnblockedEvent(ConnectionUnblockedEvent connectionBlockedEvent){
        System.out.println("阻塞放开");
    }

    /**
     * 配置监听队列产生事件
     * @param event
     */
    @EventListener(condition = "event.eventType == 'queue.created'")
    public void listener(BrokerEvent event) {
    }
}
