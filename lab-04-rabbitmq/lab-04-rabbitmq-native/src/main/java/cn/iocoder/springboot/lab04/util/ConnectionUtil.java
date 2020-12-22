package cn.iocoder.springboot.lab04.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConnectionUtil {
    private static final String IP_ADDRESS = "192.168.1.121";
    private static final Integer PORT = 5672;
    private static final String USERNAME = "test";
    private static final String PASSWORD = "123456";

    public static Connection getConnection() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(IP_ADDRESS);
        factory.setPort(PORT);
        //设置虚拟主机,可以理解成，一个项目指定一个虚拟主机
        factory.setVirtualHost("/cms");
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        return factory.newConnection();
    }
}
