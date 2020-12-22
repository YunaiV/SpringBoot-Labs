package cn.iocoder.springboot.lab04.noexchange;

import cn.iocoder.springboot.lab04.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;


public class Producer {
    public static void main(String[] args) {
        try (Connection connection = ConnectionUtil.getConnection();
             Channel channel = connection.createChannel()) {
            //声明队列:
            //第二个参数:表示队列的持久化，而不是消息的持久化，也就是说rabbitMQ重启后，队列还在，但消息会丢失
            //第三个参数:表示是否独占，true表示独占，false表示不独占，这里意思是说当前这个的这个队列是否只能由这个通道独占绑定
            //第四个参数：表示消费者消费完成后是否删除这个队列,注意是消费者与rabbitMQ断开连接后才会自动删除
            //第五个参数:额外的参数
            channel.queueDeclare("noexhange", false, false, false, null);
            //直接发送给队列
            for (int i = 0; i < 10; i++) {
                //第二个参数，表示指定发给哪个队列
                //props参数可以设置消息的设置
                channel.basicPublish("", "noexhange", null, ("hello world" + i).getBytes());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
