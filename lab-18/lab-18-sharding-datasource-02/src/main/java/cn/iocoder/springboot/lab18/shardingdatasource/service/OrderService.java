package cn.iocoder.springboot.lab18.shardingdatasource.service;

import cn.iocoder.springboot.lab18.shardingdatasource.dataobject.OrderDO;
import cn.iocoder.springboot.lab18.shardingdatasource.mapper.OrderMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Transactional
    public void add(OrderDO order) {
        // 这里先假模假样的读取一下。读取从库
        OrderDO exists = orderMapper.selectById(1);
        System.out.println(exists);

        // 插入订单
        orderMapper.insert(order);

        // 这里先假模假样的读取一下。读取主库
        exists = orderMapper.selectOne(
                new LambdaQueryWrapper<OrderDO>()
                        .eq(OrderDO::getUserId, order.getUserId())
        );
        System.out.println(exists);
    }

    public OrderDO findById(Integer id) {
        return orderMapper.selectById(id);
    }

}
