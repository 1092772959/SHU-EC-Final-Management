package com.shu.icpc.controller;


import com.shu.icpc.entity.*;
import com.shu.icpc.utils.Constants;
import com.shu.icpc.utils.Result;
import com.shu.icpc.utils.ResultTool;
import org.apache.catalina.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

@RequestMapping("/api")
@Controller
@Validated
public class CommonController extends CoreController {

    @ResponseBody
    @GetMapping("/team")
    public Result getTeams(Integer schoolId) {
        Object res = null;
        Object user = loginService.getUserFromSession();
        if(user instanceof Coach){
            res = teamService.getBySchoolId(((Coach)user).getSchoolId());
        }else if(user instanceof Student){
            res = teamService.getByStudent(((Student)user).getId());
        }
        return ResultTool.successGet(res);
    }

    /**
     * 学校-竞赛名额查看
     */
    @ResponseBody
    @GetMapping("/quota")
    public Result getQuotaByContest(@NotNull Integer contestId, Integer schoolId) {
        List<Map> res = null;
        Object user = loginService.getUserFromSession();
        if(user instanceof Admin ){
            res = contestService.getQuotaByContest(contestId);
        }else if(user instanceof Coach){
            Coach ch = (Coach)user;
            res = contestService.getQuotaByContestAndSchool(contestId, ch.getSchoolId());
        }else {
            return ResultTool.resp(Constants.UNAUTHORIZEDEXCEPTION_CODE);
        }
        return ResultTool.successGet(res);
    }

    @ResponseBody
    @PostMapping("/quota")
    public Result addQuota(@NotNull Integer contestId, @NotNull Integer schoolId, @NotNull Integer num) {
        Object user = loginService.getUserFromSession();
        if(!(user instanceof Admin) && !(user instanceof Coach)){
            return ResultTool.resp(Constants.UNAUTHORIZEDEXCEPTION_CODE);
        }
        int code = contestService.setQuota(contestId, schoolId, num);
        if (code != 0) {
            return ResultTool.resp(Constants.FAIL);
        }
        return ResultTool.success();
    }


    @ResponseBody
    @GetMapping("/solo")
    public Result getAllSoloContests() {
        List<SoloContest> res = this.soloContestService.getAll();
        return ResultTool.successGet(res);
    }

    @ResponseBody
    @GetMapping("/oss/token")
    public Result refreshToken(@NotBlank String bucket) {
        String token = ossService.getToken(bucket);
        return ResultTool.successGet(ossService.getToken(bucket));
    }

    /**
     * credential related
     */
    @ResponseBody
    @GetMapping("/contest/credential/info")
    public Result getTeamCredentialInfo(@NotNull Integer contestId) {
        Object user = loginService.getUserFromSession();
        List<TeamCredential> res = null;
        if (user instanceof Admin) {
            res = credentialService.getTeamCredentialInfo(contestId);
        } else if (user instanceof Coach) {
            Coach ch = (Coach) user;
            res = credentialService.getTeamCredentialInfo(contestId, ch.getSchoolId());
        } else if (user instanceof Student) {
            Student stu = (Student) user;
            res = credentialService.getTeamCredentialInfoStu(contestId, stu.getId());
        }
        return ResultTool.successGet(res);
    }

    @ResponseBody
    @GetMapping(value = "/contest/credentials")
    public Result getTeamCredentials(@NotEmpty @RequestParam("ids") List<Integer> credentialIds,
                                     HttpServletResponse response) {
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int code = credentialService.getLoadTeamCredentialAsZip(credentialIds, zos);

        response.setContentType("application/zip");
        response.addHeader("Content-Disposition", "attachment; filename=\"Proofs.zip\"");
        return ResultTool.resp(code);
    }

    @ResponseBody
    @GetMapping("/contest/credential")
    public Result getTeamCredentialUrl(@NotNull Integer id) {
        StringBuilder url = new StringBuilder();
        int code = this.credentialService.getTeamCredentialUrl(id, url);
        return ResultTool.resp(code, url.toString());
    }

    /**
     * solo credential related
     */
    @ResponseBody
    @GetMapping("/solo/credentials")
    public Result getSoloCredentials(@NotEmpty @RequestParam("ids") List<Integer> credentialIds,
                                     HttpServletResponse response) {
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int code = credentialService.getSoloCredentialAsZip(credentialIds, zos);

        response.setContentType("application/zip");
        response.addHeader("Content-Disposition", "attachment; filename=\"Proofs.zip\"");
        return ResultTool.resp(code);
    }

    @ResponseBody
    @GetMapping("/solo/credential")
    public Result getSoloCredentialUrl(@NotNull Integer id) {
        StringBuilder url = new StringBuilder();
        int code = this.credentialService.getSoloCredentialUrl(id, url);
        return ResultTool.resp(code, url.toString());
    }

    @ResponseBody
    @GetMapping("/solo/credential/info")
    public Result getSoloCredentialInfo(@NotNull Integer soloContestId) {
        Object user = loginService.getUserFromSession();
        List<SoloCredential> res = null;
        if (user instanceof Admin) {
            res = credentialService.getSoloCredentialInfo(soloContestId);
        } else if (user instanceof Coach) {
            Coach ch = (Coach) user;
            res = credentialService.getSoloCredentialInfo(soloContestId, ch.getSchoolId());
        } else if (user instanceof Student) {
            Student stu = (Student) user;
            res = credentialService.getSoloCredentialInfoStu(soloContestId, stu.getId());
        }
        return ResultTool.successGet(res);
    }
}
