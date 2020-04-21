package com.shu.icpc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableRabbit       //开启基于注解的RabbitMQ
@EnableCaching                        //此处开启
@MapperScan("com.shu.icpc.dao")         //扫描mybatis bean
@SpringBootApplication
public class IcpcApplication {

    public static void main(String[] args) {
        SpringApplication.run(IcpcApplication.class, args);
    }

}
