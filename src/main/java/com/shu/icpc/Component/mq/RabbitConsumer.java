package com.shu.icpc.Component.mq;

import com.shu.icpc.Component.MailService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

@Service
public class RabbitConsumer {

    @Resource
    private RabbitMQConfig config;

    @Resource
    private MailService mailService;

    //消息监听
    @RabbitListener(queues = "shu")
    public void receive(byte[] msg){
        ByteArrayInputStream bais = new ByteArrayInputStream(msg);
        Mail mail = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(bais);
            mail = (Mail)ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        mailService.sendSimpleMail(mail.getEmail(), mail.getTitle(), mail.getContent());
        System.out.println("收到邮件业务："+ mail.getTitle());
    }

    //接收完整Message
    public void reveive2(Message message){
        //消息体的字节数组
        System.out.println(message.getBody());
        //消息体的头信息
        System.out.println(message.getMessageProperties());
    }
}
