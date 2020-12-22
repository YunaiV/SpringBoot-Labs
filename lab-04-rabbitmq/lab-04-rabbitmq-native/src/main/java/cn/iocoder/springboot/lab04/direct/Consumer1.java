package cn.iocoder.springboot.lab04.direct;

import cn.iocoder.springboot.lab04.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

public class Consumer1 {
    public static void main(String[] args)  throws  Exception{
        Connection connection = ConnectionUtil.getConnection();
        final Channel channel = connection.createChannel();

        final String queue = channel.queueDeclare().getQueue(); //声明一个临时队列
        System.out.println("队列的名字:"+queue);

//        channel.exchangeDeclare("exchange","direct");
        //绑定临时队列到交换机
        //这里路由key不能为空,生产者发送消息后，交换机通过指定的路由key去找相应的队列,而消费者只要指定要消费的队列就可以了
        channel.queueBind(queue,"exchange","info");
        channel.queueBind(queue,"exchange","error");
        channel.queueBind(queue,"exchange","warn");

        channel.basicConsume(queue,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                // 打印日志
                System.out.println(String.format("[线程：%s][路由键：%s][消息内容：%s]",
                        Thread.currentThread(), envelope.getRoutingKey(), new String(body)));
            }
        });

    }
}
