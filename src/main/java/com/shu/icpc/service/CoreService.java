package com.shu.icpc.service;

import com.shu.icpc.Component.OSSService;
import com.shu.icpc.dao.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

public class CoreService {
    //dao interface
    @Resource
    protected StudentDao studentDao;

    @Resource
    protected SchoolDao schoolDao;

    @Resource
    protected CoachDao coachDao;

    @Resource
    protected AdminDao adminDao;

    @Resource
    protected TeamDao teamDao;

    @Resource
    protected ContestDao contestDao;

    @Resource
    protected RoleDao roleDao;

    @Resource
    protected BillDao billDao;

    @Resource
    protected SoloContestDao soloContestDao;

    @Resource
    protected ArticleDao articleDao;

    @Resource
    protected TeamCredentialDao teamCredentialDao;

    @Resource
    protected SoloCredentialDao soloCredentialDao;

    @Resource
    protected BucketDao bucketDao;

    //services
    @Resource
    protected StudentService studentService;

    @Resource
    protected LoginService loginService;

    @Resource
    protected OSSService ossService;

    @Resource
    protected RabbitTemplate rabbitTemplate;

    @Resource
    protected RedisTemplate redisTemplate;

    //logger
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
}
