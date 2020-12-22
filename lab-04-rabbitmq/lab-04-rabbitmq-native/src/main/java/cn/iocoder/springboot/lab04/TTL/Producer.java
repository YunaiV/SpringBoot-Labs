package cn.iocoder.springboot.lab04.TTL;

import cn.iocoder.springboot.lab04.util.ConnectionUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.util.HashMap;
import java.util.Map;

public class Producer {
    public static void main(String[] args) {
        try (final Connection connection = ConnectionUtil.getConnection();
             final Channel channel = connection.createChannel()){
            channel.exchangeDeclare("ttl","direct",true,false,null);

            Map<String,Object> maps = new HashMap<>();
            maps.put("x-message-ttl",6000);
            maps.put("x-expires",18000); //设置队列的过期时间

            channel.queueDeclare("ttlQueue",true,false,false,maps);

            //绑定
            channel.queueBind("ttlQueue","ttl","info");

            channel.basicPublish("ttl","info",null,"hello world".getBytes());
            //在消息上设置过期时间
            AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
            builder.deliveryMode(2);
            builder.expiration("60000"); //设置过期时间

            final AMQP.BasicProperties properties = builder.build();

            channel.basicPublish("ttl","info",properties,"你在哪里啊....".getBytes());

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
