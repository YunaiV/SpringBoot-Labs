package cn.iocoder.springboot.lab04.queuepriority;

import cn.iocoder.springboot.lab04.util.ConnectionUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.util.HashMap;
import java.util.Map;

public class Producer {
    public static void main(String[] args) {
        try (Connection con = ConnectionUtil.getConnection();
             Channel channel = con.createChannel()) {
            channel.exchangeDeclare("exchangePriority", "direct", true, false, null);

            Map<String, Object> maps = new HashMap<>();
            maps.put("x-max-priority", 10);

            //声明队列
            channel.queueDeclare("queuePriority", true, false, false, maps);

            //绑定
            channel.queueBind("queuePriority", "exchangePriority", "info");

            //定义消息优先级
//            AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
//            builder.priority(5);
//
//            final AMQP.BasicProperties properties = builder.build();
//
//
//            AMQP.BasicProperties.Builder builder1 = new AMQP.BasicProperties.Builder();
//            builder.priority(7);
//
//            final AMQP.BasicProperties properties1 = builder1.build();

            //发送消息
            for (int i = 0; i < 10; i++) {
                AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
                if (i % 2 != 0)
                    builder.priority(5);
                AMQP.BasicProperties properties = builder.build();
                channel.basicPublish("exchangePriority", "info", properties, ("QueuePriority"+i).getBytes());
            }
//            for (int i = 0; i < 10; i++) {
//                channel.basicPublish("exchangePriority", "info", properties1, "QueuePriority7".getBytes());
//            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
