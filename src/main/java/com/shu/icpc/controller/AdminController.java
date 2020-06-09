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


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

@RequiresRoles("admin")
@RequestMapping("/api/admin")
@Controller
@Validated
public class AdminController extends CoreController {

    @ResponseBody
    @PostMapping("/logout")
    public Result logout() {
        return loginService.logout();
    }

    @ResponseBody
    @GetMapping("/schools")
    public Result getAllSchools() {
        List<School> schools = schoolService.getAllSchools();
        return ResultTool.successGet(schools);
    }

    @ResponseBody
    @GetMapping("/coachBySchool")
    public Result getCoachBySchool(@NotEmpty @RequestParam(value = "schoolIds") List<Integer> schoolIds) {
        List<Coach> coaches = new ArrayList<>();
        for (Integer schoolId : schoolIds) {
            coaches.addAll(coachService.getBySchoolId(schoolId));
        }
        return ResultTool.successGet(coaches);
    }

    @ResponseBody
    @GetMapping("studentBySchool")
    public Result getStudentBySchool(@NotEmpty @RequestParam(value = "schoolIds") List<Integer> schoolIds) {
        List<Student> students = new ArrayList<>();
        for (Integer schoolId : schoolIds) {
            students.addAll(studentService.getBySchoolId(schoolId));
        }
        return ResultTool.successGet(students);
    }

    @ResponseBody
    @PostMapping("/passSchool")
    public Result passSchool(@NotEmpty @RequestParam(value = "schoolIds") List<Integer> schoolIds) {
        for (Integer schoolId : schoolIds) {
            schoolService.setCheckStatus(schoolId, Constants.CHECK_STATUS_PASS);
        }
        return ResultTool.success();
    }

    @ResponseBody
    @PostMapping("/rejectSchool")
    public Result rejectSchool(@NotEmpty @RequestParam(value = "schoolIds") List<Integer> schoolIds) {
        for (Integer schoolId : schoolIds) {
            schoolService.setCheckStatus(schoolId, Constants.CHECK_STATUS_REJECTED);
        }
        return ResultTool.success();
    }

    //添加比赛
    @ResponseBody
    @PostMapping("/contest")
    public Result addContest(@Validated Contest contest) {
        boolean res = contestService.addContest(contest);
        if (!res) {
            return ResultTool.resp(Constants.NAME_DUPLICATED);       //名称重复
        }
        return ResultTool.success();
    }

    //查看所有比赛
    @ResponseBody
    @GetMapping("/contest")
    public Result getContest() {
        return ResultTool.successGet(contestService.getAll());
    }

    @ResponseBody
    @PostMapping("/contest/update")
    public Result updateContest(@Validated Contest contest){
        int code = contestService.setContest(contest);
        return ResultTool.resp(code, contest);
    }


    @ResponseBody
    @PostMapping("/quota/lock")
    public Result lockQuota(@NotNull Integer contestId, @NotNull Integer schoolId, @NotNull Integer lock){
        int code = contestService.setQuotaNumLock(contestId, schoolId, lock);
        if(code !=0 ){
            return ResultTool.error();
        }
        return ResultTool.success();
    }

    /**
     * 查看一场比赛的报名队伍
     *
     * @param contestId
     * @return
     */
    @ResponseBody
    @GetMapping("/teamContest")
    public Result getTeamByContest(@NotNull Integer contestId) {
        return ResultTool.successGet(teamService.getByContestId(contestId));
    }

    /**
     * 删除比赛-级联删除
     * 如果已经有报名记录则不删除
     * @param contestId
     * @return
     */
    @ResponseBody
    @PostMapping("/contest/delete")
    public Result deleteContest(@NotNull Integer contestId) {
        int code = contestService.deleteContest(contestId);
        if(code != 0){
            return ResultTool.resp(code);
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
    public Result checkBills(@NotEmpty @RequestParam(value = "billIds") List<Integer> billIds) {
        for (Integer i : billIds) {
            schoolService.checkBill(i);
        }
        return ResultTool.success();
    }

    @ResponseBody
    @GetMapping("/contest/report")
    public Result getDetailedInformation(@NotNull Integer contestId) {
        return ResultTool.successGet(contestService.getDetailedInformation(contestId));
    }

    /**
     * solo contest related
     */
    @ResponseBody
    @PostMapping("/solo")
    public Result addSoloContest(@Validated SoloContest soloContest) {
        Integer code = this.soloContestService.addSoloContest(soloContest);
        return ResultTool.resp(code);
    }

    @ResponseBody
    @PostMapping("/solo/update")
    public Result getSoloContest(@Validated SoloContest soloContest){
        Integer code = this.soloContestService.setSoloContest(soloContest);
        return ResultTool.resp(code);
    }

    @ResponseBody
    @PostMapping("/solo/delete")
    public Result deleteSoloContest(@NotNull Integer id){
        int code = soloContestService.delete(id);
        if(code !=0 ){
            return ResultTool.resp(code);
        }
        return ResultTool.success();
    }

    @ResponseBody
    @GetMapping("/solo/report")
    public Result getSoloContestDetails(@NotNull Integer soloContestId) {
        List<Map> res = this.soloContestService.getDetailsByContest(soloContestId);
        return ResultTool.successGet(res);
    }

    /**
     * article related
     */
    @ResponseBody
    @PostMapping("/article")
    public Result addArticle(@Validated Article article) {
        Integer code = this.articleService.addArticle(article);
        return ResultTool.resp(code);
    }

    @ResponseBody
    @GetMapping("/article")
    public Result getArticlesByAdmin() {
        Admin admin = (Admin) loginService.getUserFromSession();
        if (admin == null) {
            return ResultTool.successGet(null);
        }
        Integer adminId = admin.getId();
        return ResultTool.successGet(this.articleService.getByAdmin(adminId));
    }

    @ResponseBody
    @PostMapping("/article/update")
    public Result updateArticle_v2(@NotNull Integer articleId, @NotBlank String content,
                                   @NotBlank String coverUrl, @NotBlank String intro) {
        Article article = null;
        Admin admin = (Admin) loginService.getUserFromSession();
        Integer code = this.articleService.set(articleId, content, coverUrl, intro, admin.getId(), article);
        return ResultTool.resp(code, article);
    }


    @ResponseBody
    @PostMapping("/article/status")
    public Result updateStatus_v2(@NotNull Integer articleId, @NotNull Integer status) {
        Integer code = this.articleService.updateStatus(articleId, status);
        return ResultTool.resp(code);
    }


    @ResponseBody
    @PostMapping("/article/delete")
    public Result deleteArticle_v2(@NotNull Integer articleId) {
        Admin admin = (Admin) loginService.getUserFromSession();
        int code = this.articleService.delete(articleId, admin.getId());
        return ResultTool.resp(code);
    }

    /**
     * deprecated
     */

    @ResponseBody
    @PutMapping("/article")
    public Result updateArticle(@NotNull Integer articleId, @NotBlank String content,
                                @NotBlank String coverUrl, @NotBlank String intro) {
        Article article = null;
        Admin admin = (Admin) loginService.getUserFromSession();
        Integer code = this.articleService.set(articleId, content, coverUrl, intro, admin.getId(), article);
        return ResultTool.resp(code, article);
    }

    @ResponseBody
    @PutMapping("/article/status")
    public Result updateStatus(@NotNull Integer articleId, @NotNull Integer status) {
        Integer code = this.articleService.updateStatus(articleId, status);
        return ResultTool.resp(code);
    }

    @ResponseBody
    @DeleteMapping("/article")
    public Result deleteArticle(@NotNull Integer articleId) {
        Admin admin = (Admin) loginService.getUserFromSession();
        int code = this.articleService.delete(articleId, admin.getId());
        return ResultTool.resp(code);
    }

    /**
     * team credential related
     */

    @ResponseBody
    @PostMapping("/contest/credentials")
    public Result addTeamCredential(@NotNull MultipartFile file, @NotNull Integer contestId) {
        ZipInputStream zipFile = null;
        try {
            zipFile = new ZipInputStream(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return ResultTool.resp(Constants.FAIL);
        }
        List<Map<String, String>> failedList = new ArrayList<>();
        List<String> successList = new ArrayList<>();

        int code = credentialService.saveContestCredential(zipFile, contestId, failedList, successList);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", successList);
        resp.put("fail", failedList);
        return ResultTool.resp(code, resp);
    }

    /**
     * solo credential related
     */
    @ResponseBody
    @PostMapping("/solo/credentials")
    public Result addSoloCredentials(@NotNull MultipartFile file, @NotNull Integer soloContestId) {
        ZipInputStream zipFile = null;
        try {
            zipFile = new ZipInputStream(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return ResultTool.resp(Constants.FAIL);
        }
        List<Map<String, String>> failedList = new ArrayList<>();
        List<String> successList = new ArrayList<>();

        int code = credentialService.saveSoloCredential(zipFile, soloContestId, failedList, successList);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", successList);
        resp.put("fail", failedList);
        return ResultTool.resp(code, resp);
    }
}
