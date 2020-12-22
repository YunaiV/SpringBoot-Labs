package cn.iocoder.springboot.lab04.work;

import cn.iocoder.springboot.lab04.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

public class Consumer1 {
    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
//        channel.queueDeclare("work", true, false, false, null);
        //接收消息
        //设置消费一次只处理一条
        channel.basicQos(5); //就是说不管怎么说老子至少有5条
        channel.basicConsume("work", false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(String.format("[线程：%s][路由键：%s][消息内容：%s]",
                        Thread.currentThread(), envelope.getRoutingKey(), new String(body)));
                //消息手动确认，能者多劳
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        });
    }
}
