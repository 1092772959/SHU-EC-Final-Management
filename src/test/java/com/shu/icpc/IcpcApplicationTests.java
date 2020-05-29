package com.shu.icpc;

import com.shu.icpc.Component.MailService;
import com.shu.icpc.Component.OSSService;
import com.shu.icpc.dao.ArticleDao;
import com.shu.icpc.dao.SchoolDao;
import com.shu.icpc.entity.*;
import com.shu.icpc.service.*;
import com.shu.icpc.utils.Constants;
import com.shu.icpc.utils.PasswordGenerateUtil;
import com.shu.icpc.utils.TimeUtil;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipInputStream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IcpcApplicationTests extends CoreService {

//    @Resource
//    protected BillDao billDao;
//
//    @Resource
//    protected ContestService contestService;
//
//    @Resource
//    protected TeamService teamService;
//
//    @Test
//    public void origin(){
//        System.out.println("yes");
//    }
//
//    @Test
//    public void testBill(){
//        List<Contest> res  = contestService.getAsCoach(87);
//        for(Contest ct: res){
//            System.out.println(ct);
//        }
//    }

    @Test
    public void testContest() {
//        List<Map> res = teamService.getBySchoolAndContest(87, 3);
//        for(Map m:res){
//            System.out.println(m);
//        }
    }

    @Test
    public void testMailService() {
        String emailAddr = "13120716616@163.com", title = "SHU_TEST", content = "asdasd";
        //mailService.sendSimpleMail(emailAddr, title, content);
    }

    @Test
    public void genPassword() {
        String pswd = PasswordGenerateUtil.getPassword("123456", "13120716616", Constants.hashTime);
        System.out.println(pswd);
    }

    @Test
    public void addSolo() {
        SoloContest soloContest = new SoloContest();
    }

    @Resource
    private SoloContestService soloContestService;

    @Resource
    private OSSService ossService;

    @Test
    public void testOSSSerivice() {
        System.out.println(this.ossService.getToken(Constants.BUCKET_PRIVATE));
    }

    @Resource
    private ArticleDao articleDao;

    @Test
    public void testArticle() {
        Article article = new Article();
        article.setLatestEditTime(new Date());
        article.setStatus(2);
        article.setCoverUrl("http://");
        article.setAdminId(18);
        article.setTag(0);
        article.setTitle("测试文章");
        int code;
        code = articleDao.insert(article);
        System.out.println(article.getId());


        article.setStatus(1);
        code = articleDao.updateStatus(article.getId(), 1);
        System.out.println(code);

        article.setIntro("asdasd");
        article.setContent("<html><html/>");

        code = articleDao.update(article);
        System.out.println(code);

    }

    @Test
    public void testSoloCredential() {
        List<SoloCredential> res = this.soloCredentialDao.findBySoloContestAndSchool(1, 87);
        for (SoloCredential sc : res) {
            System.out.println(sc);
        }

        SoloCredential sc = this.soloCredentialDao.findBySoloContestAndStudent(1, 13);
        System.out.println(sc);
    }

    @Test
    public void deleteArticle() {
        int code = articleDao.delete(6);
        //return value is 0: false or 1: true
        System.out.println(code);
    }

    @Test
    public void updateArticle() {
        int code = articleDao.updateStatus(7, 3);
        //return value is 0 or 1
        System.out.println(code);
    }

    @Test
    public void testZip() {
        String path = "/Users/lixiuwen/Downloads/icpc_test/ICPC-EC-FINAL.zip";
        //File file = new File("/Users/lixiuwen/Downloads/test/zip");
        try {
            ZipInputStream zip = new ZipInputStream(new FileInputStream(path));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //    @Resource
//    private StudentDao studentDao;
//
//    @Resource
//    private SchoolDao schoolDao;
//
//    @Resource
//    private CoachDao coachDao;
//
//    @Resource
//    private AdminDao adminDao;
//
//    @Resource
//    private TeamDao teamDao;
//
//    @Resource
//    protected SignService signService;
//
//    @Resource
//    protected SchoolService schoolService;
//
//    @Test
//    public void baseTest(){
//        Student stu = studentDao.findByPhone("111");
//        System.out.println(stu);
//    }
//
//    @Test
//    public void contextLoads() {
//        Student stu = new Student();
//        stu.setFamilyName("LI");
//        stu.setFirstName("XIUWEN");
//        stu.setStuName("李修文");
//        stu.setSize("XL");
//        stu.setEnrollYear(2016);
//        stu.setPhone("999");
//        stu.setPswd("0");
//        stu.setSex("男");
////        stu.setSchoolId(4);
//
//        signService.studentSignUp(stu);
//    }
//
//    @Test
//    public void testStu(){
//        String origin = "0";
//        String newPassword = "1";
//        studentDao.updatePswd(1,newPassword );
//
//    }
//
//    @Test
//    public void testInsert(){
//        Admin admin = new Admin();
//        admin.setPhone("1111");
//        admin.setPswd("0");
////        int res = adminDao.insert(admin);
////        System.out.println(res);
//    }
//
//    @Test
//    public void test(){
//
//        School school = new School();
//        school.setSchoolName("上海大学");
//        school.setAbbrName("SHU");
//        school.setAddress("上大路99");
//        school.setBillEnterprise("上海大学计算机学院");
//        school.setCheckStatus(0);
//        school.setChiefName("沈云付");
//        school.setPhone("65123456");
//        school.setPostcode("200444");
//        school.setTaxNum("1231000042502637XE");
//        System.out.println(school);
//        schoolDao.insert(school);
//
//
//        school = schoolDao.findById(4);
//        System.out.println(school);
//
//        school.setAbbrName("SSSHU");
//        schoolDao.update(school);
//
////        school = schoolDao.findByName("上海大学");
//        System.out.println(school);
//
//    }
//
//    @Test
//    public void testCoachDao(){
//        Coach coach = new Coach();
//        coach.setCoachName("沈");
//        coach.setPhone("1111111");
//        coach.setEmail("123123@qq.com");
//        coach.setSchoolId(4);
//        coachDao.insert(coach);
//
//        List<Coach> res = coachDao.findBySchoolId(4);
//        for(Coach t: res){
//            System.out.println(t);
//        }
//    }
//
//    @Test
//    public void testAdmin(){
//        Admin admin =  new Admin();
//        admin.setPhone("1234567");
//        admin.setEmail("13123@qw.cn");
//        admin.setPswd("000000");
////        adminDao.insert(admin);
//
//        admin = adminDao.findByPhone("1234567");
//        System.out.println(admin);
//    }
//
//    @Test
//    public void testTeam(){
//        Team team = new Team();
////        team.setSchoolId(4);
////        team.setTeamName("IIIT");
////        team.setSchoolName("上海大学");
////        team.setCoachId(1);
////        team.setCoachName("沈云付");
////        team.setStuAId(1);
////        team.setStuBId(2);
////        team.setStuCId(3);
//
////        teamDao.insert(team);
//        List<Team> res;
//        res = teamDao.findBySchoolId(4);
//        System.out.println(res);
//    }
//
//    @Test
//    public void testSign1(){
//        Coach coach = new Coach();
//        coach.setPhone("013");
//        coach.setCoachName("测试");
//        coach.setSchoolId(7);
//
//        int res = signService.coachSignUp(coach);
//        System.out.println(res);
//    }
//
//    @Test
//    public void testSign2(){
////        Coach coach = new Coach();
////        School school = new School();
////
////        coach.setPhone("012");
////        coach.setCoachName("测试2");
////        coach.setPswd("0");
////        coach.setSex("男");
////        coach.setEmail("123123@qq.com");
////
////        school.setSchoolName("北京新东方");
////        school.setAbbrName("BNO");
////        school.setBillEnterprise("aslkdnasd");
////        school.setTaxNum("asdasd");
////        school.setPostcode("123123");
////
////        int res = signService.schoolSignUp(school,coach);
////        System.out.println(res);
//    }
//
//    @Test
//    public void testSign3(){
//        Coach coach = new Coach();
//        School school = new School();
//
//        coach.setPhone("013");
//        coach.setCoachName("测试3");
//        coach.setPswd("0");
//        coach.setSex("男");
//        coach.setEmail("123123@qq.com");
//
//        school.setSchoolName("北京新东方");
//        school.setAbbrName("SNO");
//        school.setBillEnterprise("aslkdnasd");
//        school.setTaxNum("asdasd");
//        school.setPostcode("123123");
//
//        int res = signService.schoolSignUp(school,coach);
//        System.out.println(res);
//    }
//
//    @Test
//    public void testAdminPass(){
//        List<Map> lists = studentDao.find();
//        for(Map list: lists){
//            System.out.println(list);
//        }
//    }
//
//    @Resource
//    private ContestService contestService;
//
//    @Test
//    public void testTeamFind(){
////        Object obj = teamDao.findByContestId(1);
////        System.out.println(obj);
//        System.out.println(contestService.getAsCoach(4));
////        boolean res = contestService.login(1,10,0);
////        System.out.println(res);
//    }
//
//    @Resource
//    private ContestDao contestDao;
//
//    @Test
//    public void testTeamAd(){
//        Integer haveLogin = contestDao.findNumMaxByContestAndSchool(1, 4);
//        if(haveLogin == null){
//            System.out.println("!!!");
//        }
//        System.out.println(haveLogin);
//    }
//
//    @Test
//    public void testQuotaSelect(){
//        List<Student> res = studentDao.findBySchoolId(87);
//        System.out.println(res);
//    }
//
    @Test
    public void testCoachas() {
        String pswd = PasswordGenerateUtil.getPassword("000", "0123456789", Constants.hashTime);
        System.out.println(pswd);
    }
//
//    @Resource
//    private CoachService coachService;
//
//    @Test
//    public void test123123(){
//        teamDao.delete(9);
//
//    }

    @Resource
    private MailService mailService;

    @Resource
    private CoachService coachService;

    @Resource
    protected SchoolDao schoolDao;

    @Resource
    private SignService signService;

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    public void testMail() throws InterruptedException {
//        signService.sendRetrieveEmail("1092772959@qq.com");
//        signService.checkAndRetrieve("1092772959@qq.com","275656","lixiuwen5618");
    }

    @Resource
    private ContestService contestService;

//    @Test
//    public void testRedis(){
////        String pswd = PasswordGenerateUtil.getPassword("000","13357730973", Constants.hashTime);
////        System.out.println(pswd);
////        List<Map> res = contestService.getDetailedInformation(62);
////        for(Map m :res){
////            System.out.println(m);
////        }
//
//        redisTemplate.opsForValue().set("1092772959","123456"+1, 5, TimeUnit.SECONDS);
//        String str = (String)redisTemplate.opsForValue().get("1092772959");
//        System.out.println(str);
//        System.out.println(str.substring(0,6));
//        System.out.println(str.charAt(6));
//    }

    @Test
    public void testTime() {
        String id = TimeUtil.getMillPrimaryKey();
        System.out.println(id);
        try {
            String path = ResourceUtils.getURL("classpath:").getPath();
            System.out.println(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        HashMap t;
    }

    @Test
    public void testIdGetter() {
        School school = new School();
        school.setSchoolName("测试大学");
        school.setTaxNum("asdasdXECE");
        school.setAbbrName("TEST");
        school.setChiefName("ME");
        //schoolDao.insert(school);
        //System.out.println(school.getId());

    }
}
