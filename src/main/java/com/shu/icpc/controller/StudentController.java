package com.shu.icpc.controller;

import com.shu.icpc.entity.Coach;
import com.shu.icpc.entity.Contest;
import com.shu.icpc.entity.SoloContest;
import com.shu.icpc.entity.Student;
import com.shu.icpc.utils.Constants;
import com.shu.icpc.utils.Result;
import com.shu.icpc.utils.ResultTool;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RequiresRoles(value="stu")
@RequestMapping("/api/stu")
@Controller
@Validated
public class StudentController extends CoreController{

    @ResponseBody
    @GetMapping("/info")
    public Result getInfo(@NotNull Integer studentId){
        Student student = studentService.getById(studentId);
        return ResultTool.successGet(student);
    }

    @ResponseBody
    @PostMapping("/info")
    public Result setInfo(@NotNull Integer id,
                          @NotBlank String familyName, @NotBlank String firstName,
                          @NotBlank String college, @NotBlank String major,
                          @NotNull Integer enrollYear, @NotBlank String email,
                          @NotBlank String size, @NotBlank String sex){
        Student stu = new Student();
        stu.setId(id);
        stu.setFamilyName(familyName);
        stu.setFirstName(firstName);
        stu.setCollege(college);
        stu.setMajor(major);
        stu.setEnrollYear(enrollYear);
        stu.setEmail(email);
        stu.setSize(size);
        stu.setSex(sex);

        studentService.setStudent(stu);
        return ResultTool.success();
    }

    @ResponseBody
    @PostMapping("/password")
    public Result changePassword(@NotNull Integer studentId, @NotBlank String oldPassword, @NotBlank String newPassword){
        int code = studentService.setPassword(studentId, oldPassword, newPassword);
        return ResultTool.resp(code);
    }

    @ResponseBody
    @GetMapping("/contest")
    public Result getContests(@NotNull Integer studentId){
        List<Contest> res = contestService.getByStudent(studentId);
        return ResultTool.successGet(res);
    }

    /**
     * solo contest related
     */

    @ResponseBody
    @GetMapping("/mySolo")
    public Result getRegisteredSoloContests(){
        Integer studentId = this.getUserFromSession().getId();
        List<SoloContest> res = soloContestService.getByStuId(studentId);
        return ResultTool.successGet(res);
    }

    @ResponseBody
    @PostMapping("/solo/signIn")
    public Result signInSoloContest(@NotNull Integer soloContestId,
                                    @NotNull Integer isStarred){
        Integer studentId = this.getUserFromSession().getId();
        Integer code = this.soloContestService.signIn(studentId, soloContestId, isStarred);
        return ResultTool.resp(code);
    }

    @ResponseBody
    @PostMapping("/solo/signOff")
    public Result signOffSoloContest(@NotNull Integer soloContestId){
        Integer studentId = this.getUserFromSession().getId();
        Integer code = this.soloContestService.signOff(studentId, soloContestId);
        return ResultTool.resp(code);
    }

    private Student getUserFromSession(){
        Subject user = SecurityUtils.getSubject();
        Session session = user.getSession();
        return (Student)session.getAttribute(Constants.SESSION_USER);
    }
}
