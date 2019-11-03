package com.shu.icpc.service;

import com.shu.icpc.Component.MailService;
import com.shu.icpc.dao.*;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

public class CoreService {
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
    protected RedisTemplate redisTemplate;
}
