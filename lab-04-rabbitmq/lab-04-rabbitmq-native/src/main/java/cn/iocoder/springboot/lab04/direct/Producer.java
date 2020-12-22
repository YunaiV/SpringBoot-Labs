package cn.iocoder.springboot.lab04.direct;

import cn.iocoder.springboot.lab04.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Producer {
    public static void main(String[] args) {
        try (Connection connection = ConnectionUtil.getConnection();
             Channel channel = connection.createChannel()) {
            //1.声明交换机类型
            channel.exchangeDeclare("exchange","direct");

            //2.通过交换机发送消息
            channel.basicPublish("exchange","info",null,"hello world".getBytes());
            channel.basicPublish("exchange","error",null,"hello world".getBytes());
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }
}
