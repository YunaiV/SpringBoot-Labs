package cn.iocoder.springboot.lab04.noexchange;

import cn.iocoder.springboot.lab04.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

public class Consumer {
    public static void main(String[] args) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            Channel channel = connection.createChannel();
            //消费哪个队列，一定要跟生产者一样的配置
            channel.queueDeclare("aaa",false,false,false,null);
            channel.basicConsume("noexhange", new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    // 打印日志
                    System.out.println(String.format("[线程：%s][路由键：%s][消息内容：%s]",
                            Thread.currentThread(), envelope.getRoutingKey(), new String(body)));
                    // ack 消息已经消费
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

