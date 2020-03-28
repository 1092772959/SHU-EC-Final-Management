package com.shu.icpc.controller;

import com.shu.icpc.entity.*;
import com.shu.icpc.utils.Constants;
import com.shu.icpc.utils.Result;
import com.shu.icpc.utils.ResultTool;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiresRoles("admin")
@RequestMapping("/api/admin")
@Controller
@Validated
public class AdminController extends CoreController{

    @ResponseBody
    @PostMapping("/logout")
    public Result logout(){
        return loginService.logout();
    }

    @ResponseBody
    @GetMapping("/schools")
    public Result getAllSchools(){
        List<School> schools = schoolService.getAllSchools();
        return ResultTool.successGet(schools);
    }

    @ResponseBody
    @GetMapping("/coachBySchool")
    public Result getCoachBySchool(@NotEmpty @RequestParam(value="schoolIds") List<Integer> schoolIds){
        List<Coach> coaches = new ArrayList<>();
        for(Integer schoolId : schoolIds){
            coaches.addAll(coachService.getBySchoolId(schoolId));
        }
        return ResultTool.successGet(coaches);
    }

    @ResponseBody
    @GetMapping("studentBySchool")
    public Result getStudentBySchool(@NotEmpty @RequestParam(value="schoolIds") List<Integer> schoolIds){
        List<Student> students = new ArrayList<>();
        for(Integer schoolId : schoolIds){
            students.addAll(studentService.getBySchoolId(schoolId));
        }
        return ResultTool.successGet(students);
    }

    @ResponseBody
    @PostMapping("/passSchool")
    public Result passSchool(@NotEmpty @RequestParam(value="schoolIds") List<Integer> schoolIds){
        for(Integer schoolId: schoolIds){
            schoolService.setCheckStatus(schoolId, Constants.CHECK_STATUS_PASS);
        }
        return ResultTool.success();
    }

    @ResponseBody
    @PostMapping("/rejectSchool")
    public Result rejectSchool(@NotEmpty @RequestParam(value="schoolIds") List<Integer> schoolIds){
        for(Integer schoolId: schoolIds){
            schoolService.setCheckStatus(schoolId, Constants.CHECK_STATUS_REJECTED);
        }
        return ResultTool.success();
    }

    //添加比赛
    @ResponseBody
    @PostMapping("/contest")
    public Result addContest(@Validated Contest contest){
        boolean res = contestService.addContest(contest);
        if(!res){
            return ResultTool.error(Constants.NAME_DUPLICATED);       //名称重复
        }
        return ResultTool.success();
    }

    //查看所有比赛
    @ResponseBody
    @GetMapping("/contest")
    public Result getContest(){
        return ResultTool.successGet(contestService.getAll());
    }

    /**
     * 查看一场比赛的报名队伍
     * @param contestId
     * @return
     */
    @ResponseBody
    @GetMapping("/teamContest")
    public Result getTeamByContest(@NotNull Integer contestId){
        return ResultTool.successGet(teamService.getByContestId(contestId));
    }

    /**
     * 删除比赛-级联删除
     * @param contestId
     * @return
     */
    @ResponseBody
    @PostMapping("/contest/delete")
    public Result deleteContest(@NotNull Integer contestId){
        contestService.deleteContest(contestId);
        return ResultTool.success();
    }


    /**
     * 学校-竞赛名额查看
     */
    @ResponseBody
    @GetMapping("/quota")
    public Result getQuotaByContest(@NotNull Integer contestId, @NotNull Integer schoolId){
        List<Map> res = null;
        if(schoolId <= 0){
            res = contestService.getQuotaByContest(contestId);
        }else{
            res = contestService.getQuotaByContestAndSchool(contestId, schoolId);
        }
        return ResultTool.successGet(res);
    }

    /**
     * 添加 比赛-学校名额
     * @param contestId
     * @param schoolId
     * @param num
     * @return
     */
    @ResponseBody
    @PostMapping("/quota")
    public Result addQuota(Integer contestId, Integer schoolId, Integer num){
        boolean res = contestService.addQuota(contestId, schoolId , num);
        if(res){
            return ResultTool.success();
        }
        return ResultTool.error(Constants.FAIL);
    }

    @ResponseBody
    @PostMapping("/quota/update")
    public Result updateQuota(Integer contestId, Integer schoolId, Integer num){
        int code = contestService.setQuota(contestId, schoolId, num);
        if(code != 1){
            return ResultTool.error(Constants.UPDATE_QUOTA_ERROR);
        }
        return ResultTool.success();
    }

    @ResponseBody
    @GetMapping("/bill")
    public Result getBills() {
        return ResultTool.successGet(schoolService.getAllBills());
    }

    //确认开发票
    @ResponseBody
    @PostMapping("/bill/check")
    public Result checkBills(@NotEmpty @RequestParam(value="billIds") List<Integer> billIds){
        for(Integer i: billIds){
            schoolService.checkBill(i);
        }
        return ResultTool.success();
    }

    @ResponseBody
    @GetMapping("/contest/report")
    public Result getDetailedInformation(@NotNull Integer contestId){
        return ResultTool.successGet(contestService.getDetailedInformation(contestId));
    }

    @ResponseBody
    @PostMapping("/solo")
    public Result addSoloContest(@Validated SoloContest soloContest){
        Integer code = this.soloContestService.addSoloContest(soloContest);
        return ResultTool.success(code);
    }

    //上传证书
    @ResponseBody
    @PostMapping
    public Result uploadCredential(@NotNull MultipartFile file, @NotNull Integer contestId, @NotNull Integer teamId) throws IOException {
        int code = contestService.uploadFile(file, contestId, teamId);
        return ResultTool.success();
    }
}
