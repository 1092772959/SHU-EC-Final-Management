package com.shu.icpc.controller;


import com.shu.icpc.entity.*;
import com.shu.icpc.utils.Result;
import com.shu.icpc.utils.ResultTool;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipOutputStream;

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

    @ResponseBody
    @GetMapping("/oss/token")
    public Result refreshToken(@NotBlank String bucket){
        String token = ossService.getToken(bucket);
        return ResultTool.successGet(ossService.getToken(bucket));
    }

    /**
     * credential related
     */
    @ResponseBody
    @GetMapping("/contest/credential/info")
    public Result getTeamCredentialInfo(@NotNull Integer contestId){
        Object user = loginService.getUserFromSession();
        List<TeamCredential> res = null;
        if(user instanceof Admin){
            res = credentialService.getTeamCredentialInfo(contestId);
        }else if(user instanceof Coach){
            Coach ch = (Coach)user;
            res = credentialService.getTeamCredentialInfo(contestId, ch.getSchoolId());
        }
        return ResultTool.successGet(res);
    }

    @ResponseBody
    @GetMapping(value = "/contest/credentials")
    public Result getTeamCredentials(@NotEmpty @RequestParam("ids") List<Integer> credentialIds,
                                     HttpServletResponse response){
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
    public Result getTeamCredentialUrl(@NotNull Integer id){
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
                                     HttpServletResponse response){
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
    public Result getSoloCredentialUrl(@NotNull Integer id){
        StringBuilder url = new StringBuilder();
        int code = this.credentialService.getSoloCredentialUrl(id, url);
        return ResultTool.resp(code, url.toString());
    }

    @ResponseBody
    @GetMapping("/solo/credential/info")
    public Result getSoloCredentialInfo(@NotNull Integer soloContestId){
        Object user = loginService.getUserFromSession();
        List<SoloCredential> res = null;
        if(user instanceof Admin){
            res = credentialService.getSoloCredentialInfo(soloContestId);
        }else if(user instanceof Coach){
            Coach ch = (Coach)user;
            res = credentialService.getSoloCredentialInfo(soloContestId, ch.getSchoolId());
        }
        return ResultTool.successGet(res);
    }
}
