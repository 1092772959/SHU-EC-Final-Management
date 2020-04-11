package com.shu.icpc.controller;

import com.shu.icpc.entity.*;
import com.shu.icpc.utils.Constants;
import com.shu.icpc.utils.Result;
import com.shu.icpc.utils.ResultTool;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.tomcat.util.bcel.Const;
import org.apache.tomcat.util.bcel.classfile.Constant;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiresRoles(value={"coach" })
@RequestMapping("/api/coach")
@Controller
@Validated
public class CoachController extends CoreController {

    @ResponseBody
    @GetMapping("/info")
    public Result getInfo(@NotNull Integer coachId){
        Coach coach = coachService.getById(coachId);

        return ResultTool.successGet(coach);
    }

    @ResponseBody
    @PostMapping("/info")
    public Result setInfo(@NotNull Integer id, @NotBlank String coachName,
                          @NotBlank String familyName, @NotBlank String firstName,
                          @NotBlank String email, @NotBlank String size, @NotBlank String sex){
        Coach coach = new Coach();
        coach.setId(id);
        coach.setCoachName(coachName);
        coach.setFamilyName(familyName);
        coach.setFirstName(firstName);
        coach.setEmail(email);
        coach.setSize(size);
        coach.setSex(sex);
        coachService.setCoach(coach);
        return ResultTool.success();
    }

    @ResponseBody
    @PostMapping("/password")
    public Result changePassword(@NotNull Integer coachId,
                                 @NotBlank String oldPassword, @NotBlank String newPassword){
        int code = coachService.setPassword(coachId, oldPassword, newPassword);
        return ResultTool.resp(code);
    }

    @ResponseBody
    @GetMapping("/students")
    public Result getStudents(@NotNull Integer schoolId){
        List<Student> students = studentService.getBySchoolId(schoolId);
        return ResultTool.successGet(students);
    }

    @ResponseBody
    @GetMapping("/coaches")
    public Result getCoaches(@NotNull Integer schoolId){
        List<Coach> coaches = coachService.getBySchoolId(schoolId);
        return ResultTool.successGet(coaches);
    }

    @RequiresRoles("chief")
    @ResponseBody
    @PostMapping("/passCoach")
    public Result passCoach(@NotEmpty @RequestParam(value="coachIds") List<Integer> coachIds){
        for(Integer coachId: coachIds){
            coachService.setCheckStatus(coachId, Constants.CHECK_STATUS_PASS);
        }
        return ResultTool.success();
    }

    @RequiresRoles("chief")
    @ResponseBody
    @PostMapping("/rejectCoach")
    public Result rejectCoach(@NotEmpty @RequestParam(value="coachIds") List<Integer> coachIds){
        for(Integer coachId: coachIds){
            coachService.setCheckStatus(coachId, Constants.CHECK_STATUS_REJECTED);
        }
        return ResultTool.success();
    }

    @ResponseBody
    @PostMapping("/passStu")
    public Result passStudent(@NotEmpty @RequestParam(value="studentIds") List<Integer> studentIds){
        for (Integer studentId: studentIds){
            studentService.setCheckStatus(studentId, Constants.CHECK_STATUS_PASS);
        }
        return ResultTool.success();
    }

    @ResponseBody
    @PostMapping("/rejectStu")
    public Result rejectStudent(@NotEmpty @RequestParam(value="studentIds") List<Integer> studentIds){
        for (Integer studentId: studentIds){
            studentService.setCheckStatus(studentId, Constants.CHECK_STATUS_REJECTED);
        }
        return ResultTool.success();
    }

    @ResponseBody
    @PostMapping("/team")
    public Result addTeam(@NotNull Team team){
        if(teamService.add(team)){
            return ResultTool.success();
        }
        return ResultTool.resp(Constants.FAIL);
    }

    @ResponseBody
    @GetMapping("/team")
    public Result getTeams(@NotNull Integer schoolId){
        return ResultTool.successGet(teamService.getBySchoolId(schoolId));
    }

    @ResponseBody
    @PostMapping("/delTeam")
    public Result deleteTeam(@NotNull Integer teamId){
        boolean res = teamService.delete(teamId);
        if(!res){
            return ResultTool.resp(Constants.FAIL);
        }
        return ResultTool.success();
    }

    @ResponseBody
    @GetMapping("/school")
    public Result getSchool(@NotNull Integer schoolId){
        School school = schoolService.getById(schoolId);
        return ResultTool.successGet(school);
    }

    @ResponseBody
    @PostMapping("/school")
    public Result updateSchool(@NotNull Integer schoolId, @NotBlank String schoolName,
                               @NotBlank String abbrName, @NotBlank String address,
                               @NotBlank String chiefName, @NotBlank String phone, @NotBlank String billEnterprise,
                               @NotBlank String postcode, @NotBlank String taxNum){
        School school = new School(schoolId, schoolName, abbrName, address, chiefName,
                phone, billEnterprise, postcode, taxNum);
        schoolService.setSchool(school);
        return ResultTool.success();
    }


    @ResponseBody
    @GetMapping("/contest/team")
    public Result getMySchoolsTeamsByContest(@NotNull Integer schoolId, @NotNull Integer contestId){
        return ResultTool.successGet(teamService.getBySchoolAndContest(schoolId, contestId));
    }

    @ResponseBody
    @PostMapping("/signUp")
    public Result registerForContest( @NotNull Integer contestId, @NotNull Integer teamId,
                                      @NotNull Integer isStarred, @NotNull Boolean onSite){
        boolean res = contestService.login(contestId, teamId, isStarred, onSite);
        if(res){
            return ResultTool.success();
        }
        return ResultTool.resp(Constants.FAIL);
    }

    @ResponseBody
    @PostMapping("/contest/quit")
    public Result cancelContestSign( @NotNull Integer contestId, @NotNull Integer teamId){
        contestService.signOff(contestId, teamId);
        return ResultTool.success();
    }

    @ResponseBody
    @PostMapping("/contest/meal")
    public Result updateMealNum(@NotNull Integer contestId, @NotNull Integer schoolId, @NotNull Integer num){
        boolean res = contestService.setMealCoachNum(contestId, schoolId, num);
        if(!res){
            return ResultTool.resp(Constants.FAIL);
        }
        return ResultTool.success();
    }

    @ResponseBody
    @GetMapping("/teamContest")
    public Result getTeamByContest(@NotNull Integer contestId){
        List<Map> res = teamService.getByContestId(contestId);
        return ResultTool.successGet(res);
    }

    /**
     * 以本学校的身份查看比赛
     * @param schoolId
     * @return
     */
    @ResponseBody
    @GetMapping("/contest")
    public Result getContests(@NotNull Integer schoolId){
        return ResultTool.successGet(contestService.getAsCoach(schoolId));
    }

    @ResponseBody
    @GetMapping("/bill")
    public Result getBill(@NotNull Integer schoolId){
        return ResultTool.successGet(schoolService.getBySchool(schoolId));
    }

    @ResponseBody
    @PostMapping("/bill")
    public Result addBill( @Validated Bill bill){
        if(!schoolService.checkBillType(bill) || !schoolService.addBill(bill)){
            return ResultTool.resp(Constants.FAIL);
        }
        return ResultTool.success();
    }

    @ResponseBody
    @PostMapping("/bill/update")
    public Result updateBill( @Validated Bill bill){
        System.out.println(bill);
        if(bill.getId() == null || bill.getChecked() == null){
            throw new ValidationException();                //非空校验
        }
        if(!schoolService.checkBillType(bill) || !schoolService.setBill(bill)){
            return ResultTool.resp(Constants.FAIL);
        }
        return ResultTool.success();
    }

    /**
     * solo contest related
     * @param soloContestId
     * @return
     */
    @ResponseBody
    @GetMapping("/solo/schoolStatus")
    public Result getSoloContestsBySchool(@NotNull Integer soloContestId){
        Integer schoolId = getUserFromSession().getSchoolId();
        List<Map> res = this.soloContestService.getDetailsBySchool(soloContestId, schoolId);
        return ResultTool.successGet(res);
    }

    private Coach getUserFromSession(){
        Subject user = SecurityUtils.getSubject();
        Session session = user.getSession();
        return (Coach)session.getAttribute(Constants.SESSION_USER);
    }

    /**
     * credential related
     */
}
