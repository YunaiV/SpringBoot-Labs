package cn.iocoder.springboot.lab04.alternate;

import cn.iocoder.springboot.lab04.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.util.HashMap;
import java.util.Map;

public class Producer {
    public static void main(String[] args) {
        try (Connection connection = ConnectionUtil.getConnection();
             Channel channel = connection.createChannel()) {
            Map<String, Object> maps = new HashMap<>();
            //第二个参数就是备份交换机的名字
            maps.put("alternate-exchange", "myAe");
            //声明交换机:同时指定备份的交换机名字为myAe
            channel.exchangeDeclare("normalExchange", "direct", true, false, maps);
            channel.exchangeDeclare("myAe", "fanout", true, false, null);

            //声明队列
            channel.queueDeclare("normalQueue", true, false, false, null);
            //绑定
            channel.queueBind("normalQueue","normalExchange","normalKey");

            //声明一个未被正确路由的队列
            channel.queueDeclare("unroutedQueue",true,false,false,null);
            //绑定：广播模式不用设置routingKey
            channel.queueBind("unroutedQueue","myAe","");

            //发送数据:不指定mandatory参数
            channel.basicPublish("normalExchange","unrouted",null,"hello world".getBytes());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
