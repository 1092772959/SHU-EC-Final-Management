package com.shu.icpc.controller;

import com.shu.icpc.Component.OSSService;
import com.shu.icpc.service.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import javax.annotation.Resource;

public class CoreController {
    @Resource
    protected StudentService studentService;

    @Resource
    protected LoginService loginService;

    @Resource
    protected CoachService coachService;

    @Resource
    protected SignService signService;

    @Resource
    protected SchoolService schoolService;

    @Resource
    protected TeamService teamService;

    @Resource
    protected ContestService contestService;

    @Resource
    protected SoloContestService soloContestService;

    @Resource
    protected ArticleService articleService;

    @Resource
    protected CredentialService credentialService;

    @Resource
    RabbitTemplate rabbitTemplate;

    @Resource
    protected OSSService ossService;
}
