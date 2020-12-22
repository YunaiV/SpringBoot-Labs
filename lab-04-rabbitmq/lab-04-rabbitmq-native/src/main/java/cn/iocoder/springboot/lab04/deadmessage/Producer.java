package cn.iocoder.springboot.lab04.deadmessage;

import cn.iocoder.springboot.lab04.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

import java.util.HashMap;
import java.util.Map;

public class Producer {
    public static void main(String[] args) {
        try (Connection con = ConnectionUtil.getConnection();
             final Channel channel = con.createChannel()) {
            //声明一个交换机
            channel.exchangeDeclare("exchange.dlx", "direct", true, false, null);
            channel.exchangeDeclare("exchange.normal", "fanout", true, false, null);


            Map<String, Object> maps = new HashMap<>();
            maps.put("x-message-ttl", 10000); //设置消息过期时间
            maps.put("x-dead-letter-exchange", "exchange.dlx");//设置死信队列对应的交换机
            maps.put("x-dead-letter-routing-key","routing-key");

            //声明队列
            channel.queueDeclare("queue.normal", true, false, false, maps);
            channel.queueBind("queue.normal","exchange.normal","");


            channel.queueDeclare("queue.dlx",true,false,false,null);
            channel.queueBind("queue.dlx","exchange.dlx","routing-key");

            //10 秒 消息过期,消息被丢入到死信队列里
            channel.basicPublish("exchange.normal","rk", MessageProperties.PERSISTENT_TEXT_PLAIN,"dlx".getBytes());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
