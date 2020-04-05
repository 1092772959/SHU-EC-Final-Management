package com.shu.icpc.service;

import com.shu.icpc.Component.MailService;
import com.shu.icpc.dao.*;
import com.shu.icpc.entity.Student;
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

    //services
    @Resource
    protected StudentService studentService;

    @Resource
    protected ArticleDao articleDao;

    @Resource
    protected RedisTemplate redisTemplate;
}
