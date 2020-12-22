package cn.iocoder.springboot.lab04.rabbitmqdemo.producer;

import cn.iocoder.springboot.lab04.rabbitmqdemo.message.Demo13Message;
import com.rabbitmq.client.ConfirmCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Demo13Producer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void syncSend(Integer id) {
        // 创建 Demo13Message 消息
        Demo13Message message = new Demo13Message();
        message.setId(id);
        // 同步发送消息
        rabbitTemplate.invoke(new RabbitOperations.OperationsCallback<Object>() {

            @Override
            public Object doInRabbit(RabbitOperations operations) {
                // 同步发送消息
                for (int i = 0; i < 10; i++) {
                    operations.convertAndSend(Demo13Message.EXCHANGE, Demo13Message.ROUTING_KEY, message);
                }
                logger.info("[doInRabbit][发送消息完成]");

                // 等待确认
                operations.waitForConfirms(0); // timeout 参数，如果传递 0 ，表示无限等待
                logger.info("[doInRabbit][等待 Confirm 完成]");
                return null;
            }

        }, new ConfirmCallback() {

            @Override
            public void handle(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("deliveryTag:" + "  multiple:" + multiple);
                logger.info("[handle][Confirm 成功]");
            }

        }, new ConfirmCallback() {

            @Override
            public void handle(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("deliveryTag:" + "  multiple:" + multiple);
                logger.info("[handle][Confirm 失败]");
            }

        });
    }
}
