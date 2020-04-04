package com.shu.icpc.controller;


import com.shu.icpc.entity.SoloContest;
import com.shu.icpc.utils.Result;
import com.shu.icpc.utils.ResultTool;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;

@RequestMapping("/api")
@Controller
@Validated
public class CommonController extends CoreController{

    @ResponseBody
    @GetMapping("/solo")
    public Result getAllSoloContests(){
        List<SoloContest> res = this.soloContestService.getAll();
        return ResultTool.successGet(res);
    }


    
}
