package cn.iocoder.springboot.labs.lab10.springdatarediswithjedis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PipelineTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void test01() {
        RedisCallback<Object> redisCallback = connection -> {
            // set 写入
            for (int i = 0; i < 3; i++) {
                connection.set(String.format("yunai:%d", i).getBytes(), "shuai".getBytes());
            }

            // get
            for (int i = 0; i < 3; i++) {
                connection.get(String.format("yunai:%d", i).getBytes());
            }

            // 返回 null 即可
            return null;
        };


//        List<Object> results = stringRedisTemplate.executePipelined((RedisCallback<Object>) connection -> {
//            // set 写入
//            for (int i = 0; i < 3; i++) {
//                connection.set(String.format("yunai:%d", i).getBytes(), "shuai".getBytes());
//            }
//
//            // get
//            for (int i = 0; i < 3; i++) {
//                connection.get(String.format("yunai:%d", i).getBytes());
//            }
//
//            // 返回 null 即可
//            return null;
//        });
        System.out.println(redisCallback.hashCode());
        List<Object> results = stringRedisTemplate.executePipelined(redisCallback);

        // 打印结果
        System.out.println(results);
    }

}
