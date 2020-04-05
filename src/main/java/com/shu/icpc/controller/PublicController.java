package com.shu.icpc.controller;

import com.shu.icpc.dao.ContestDao;
import com.shu.icpc.entity.*;
import com.shu.icpc.utils.Constants;
import com.shu.icpc.utils.Result;
import com.shu.icpc.utils.ResultTool;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RequestMapping("/api/user")
@Controller
@Validated
public class PublicController extends CoreController{


    @ResponseBody
    @PostMapping("/adminLogin")
    public Result login(@NotBlank String phone, @NotBlank String pswd){
        return loginService.adminLogin(phone,pswd);
    }


    @ResponseBody
    @PostMapping("/login")
    public Result userLogin(@NotBlank String phone, @NotBlank String pswd){
        return loginService.login(phone, pswd);
    }

    //for internal system invoking; send msg to user if haven't logged in
    @ResponseBody
    @RequestMapping("/unAuthenticate")
    public Result userFail(){
        return ResultTool.resp(Constants.UNAUTHENTICATE);
    }

    @ResponseBody
    @PostMapping("/logout")
    public Result userLogout(){
        return loginService.logout();
    }

    @ResponseBody
    @GetMapping("/exist")
    public Result checkAccountExists(@NotBlank String phone){
        boolean res = signService.checkExists(phone);
        return ResultTool.successGet(res);
    }

    @ResponseBody
    @PostMapping("/student")
    public Result addStudent(@Validated Student student){
        Integer code = signService.studentSignUp(student);
        if(code >= 700){
            return ResultTool.resp(code);
        }
        return ResultTool.resp(code);
    }

    @ResponseBody
    @PostMapping("/coach")
    public Result addCoach(@Validated Coach coach){
        Integer code = signService.coachSignUp(coach);
        if(code >= 700){
            return ResultTool.resp(code);
        }
        return ResultTool.resp(code);
    }

    @ResponseBody
    @PostMapping("/school")
    public Result addSchool(@NotBlank String schoolName,
            @NotBlank String coachName, @NotBlank String phone, @NotBlank String email, @NotBlank String pswd){
        School school = new School(schoolName, coachName, phone);

        Coach coach = new Coach();
        coach.setSchoolName(schoolName);
        coach.setCoachName(coachName);
        coach.setPhone(phone);
        coach.setPswd(pswd);
        coach.setEmail(email);

        int code = signService.schoolSignUp(school, coach);
        if(code >= 700){
            return ResultTool.resp(code);
        }
        return ResultTool.resp(code);
    }

    @ResponseBody
    @PostMapping("/retrieve/code")
    public Result getRetrieveCode(@NotBlank String email){
        if(signService.sendRetrieveEmail(email)){
            return ResultTool.success();
        }else{
            return ResultTool.resp(Constants.FAIL);
        }
    }

    @ResponseBody
    @PostMapping("/retrieve/pswd")
    public Result retrievePassword(@NotBlank String email, @NotBlank String code, @NotBlank String password){
        if(signService.checkAndRetrieve(email, code, password)){
            return ResultTool.success();
        }else{
            return ResultTool.resp(Constants.FAIL);
        }
    }


    @ResponseBody
    @GetMapping("/school")
    public Result getSchool(){
        return ResultTool.successGet(schoolService.getAllIdAndNames());
    }

    /**
     * users who have not logged in have acess to articles
     * @return
     */
    @ResponseBody
    @GetMapping("/article")
    public Result getArticles(Integer status){
        List<Article> res = null;
        if(status == null){
            res = this.articleService.getAll();
        }else{
            res = this.articleService.getByStatus(status);
        }
        return ResultTool.successGet(res);
    }

    @ResponseBody
    @GetMapping("/article/title")
    public Result getArticlesLike(@NotBlank String titleLike){
        List<Article> res = this.articleService.getTitleLike(titleLike);
        return ResultTool.successGet(res);
    }

    /*
    @Resource
    private ContestDao contestDao;

    @ResponseBody
    @PostMapping("/ecNum")
    public Result injectECNum(@NotBlank String schoolName, @NotNull Integer num ){
        School school = schoolService.getByName(schoolName);
        System.out.println(school.getId());
        contestDao.insertQuota(62, school.getId(), num);
        return ResultTool.success();
    }
    */
}
