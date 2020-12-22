package cn.iocoder.springboot.lab04.direct2;

import cn.iocoder.springboot.lab04.util.ConnectionUtil;
import com.rabbitmq.client.*;

import java.util.concurrent.TimeUnit;

public class Producer {
    public static void main(String[] args) {
        try (final Connection connection = ConnectionUtil.getConnection();
             final Channel channel = connection.createChannel()) {
            //声明一个交换机
            channel.exchangeDeclare("test1", "direct");
            //声明一个队列
            channel.queueDeclare("queue1", false, false, false, null);
            //绑定
            channel.queueBind("queue1", "test1", "info", null);
            channel.queueBind("queue1", "test1", "error", null);
            channel.queueBind("queue1", "test1", "warn", null);
            //发送
            channel.basicPublish("test1", "info", null, "hello world".getBytes());
            //测试mandatory参数
            channel.basicPublish("test1", "", true,
                    MessageProperties.PERSISTENT_TEXT_PLAIN, "测试mandatory参数".getBytes());

            channel.addReturnListener((replyCode, replyText, exchange, routingKey, properties, body) -> {
                String message = new String(body);
                System.out.println("返回的结果:" + message);
            });
            TimeUnit.HOURS.sleep(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
