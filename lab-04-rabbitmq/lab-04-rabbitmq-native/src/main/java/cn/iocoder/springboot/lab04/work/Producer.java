package cn.iocoder.springboot.lab04.work;

import cn.iocoder.springboot.lab04.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Producer {
    public static void main(String[] args) {
       try( Connection connection = ConnectionUtil.getConnection();
            Channel channel = connection.createChannel()){
           //声明一个队列
           channel.queueDeclare("work",true,false,false,null);
           //发送
           for (int i = 0; i < 10; i++) {
               channel.basicPublish("","work",null,("hello world "+i).getBytes());
           }
       }catch (Exception e){
           e.printStackTrace();
       }
        //
    }
}
