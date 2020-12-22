package cn.iocoder.springboot.lab04.work;

import cn.iocoder.springboot.lab04.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

public class Consumer2 {
    public static void main(String[] args) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
//        channel.queueDeclare("work", true, false, false, null);
        channel.basicQos(3); //表示一次只接受3条未确认的消息,换句话说，就是我给你3条数据消费，当你消费完了后，要给我发确认消息
        //如果不发,我就不给你发了
        channel.basicConsume("work", false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(String.format("[线程：%s][路由键：%s][消息内容：%s]",
                        Thread.currentThread(), envelope.getRoutingKey(), new String(body)));
                //手动消息确认
//                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        });
    }
}
