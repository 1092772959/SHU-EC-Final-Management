package com.shu.icpc.service;

import com.shu.icpc.entity.*;
import com.shu.icpc.utils.Constants;
import com.shu.icpc.utils.HTTPUtil;
import com.shu.icpc.utils.ZipUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * include team credentials and solo credentials management
 */

@Service
@Transactional
public class CredentialService extends CoreService {

    private void appendMsgElem(List<Map<String, String>> list, String fileName, String msg) {
        Map<String, String> elem = new HashMap<>();
        elem.put("fileName", fileName);
        elem.put("error", msg);
        list.add(elem);
    }

    public List<TeamCredential> getTeamCredentialInfo(Integer contestId) {
        List<TeamCredential> res = teamCredentialDao.findByContest(contestId);
        return res;
    }

    public List<TeamCredential> getTeamCredentialInfo(Integer contestId, Integer schoolId) {
        List<TeamCredential> res = teamCredentialDao.findByContestAndSchool(contestId, schoolId);
        return res;
    }

    public List<TeamCredential> getTeamCredentialInfoStu(Integer contestId, Integer studentId) {
        List<TeamCredential> res = teamCredentialDao.findByContestAndStudent(contestId, studentId);
        return res;
    }

    public List<SoloCredential> getSoloCredentialInfo(Integer soloId) {
        return soloCredentialDao.findBySoloContest(soloId);
    }

    public List<SoloCredential> getSoloCredentialInfo(Integer soloId, Integer schoolId) {
        return soloCredentialDao.findBySoloContestAndSchool(soloId, schoolId);
    }

    public List<SoloCredential> getSoloCredentialInfoStu(Integer soloId, Integer studentId) {
        SoloCredential sc = soloCredentialDao.findBySoloContestAndStudent(soloId, studentId);
        List<SoloCredential> res = new ArrayList<>();
        res.add(sc);
        return res;
    }

    public Integer saveContestCredential(ZipInputStream zipFile, Integer contestId,
                                         List<Map<String, String>> failedList,
                                         List<String> successList) {
        Contest contest = contestDao.findById(contestId);
        if (contest == null) {
            return Constants.CONTEST_NOT_EXISTS;
        }

        Map<String, byte[]> map = new HashMap<>();
        int code = ZipUtil.extractFiles(zipFile, map);

        if (code != 0) {
            // zip file extraction error
            return Constants.ZIP_ERROR;
        }

        for (Map.Entry<String, byte[]> entry : map.entrySet()) {
            String fileName = entry.getKey();
            String[] parts = fileName.split("/");
            String indent = parts[parts.length - 1];

            byte[] bytes = entry.getValue();

            indent = indent.trim();
            String[] bases = indent.split("\\.");

            String teamIdStr = bases[0];
            Integer teamId = null;
            try {
                teamId = Integer.parseInt(teamIdStr);
            } catch (NumberFormatException e) {
                this.appendMsgElem(failedList, fileName, Constants.MSG_NAME_NOT_NUMBER);
                continue;
            }

            Team team = teamDao.findById(teamId);

            //make sure unique
            if (team == null) {
                this.appendMsgElem(failedList, fileName, Constants.MSG_FILE_NAME_NO_MAPPING);
                continue;
            }

            TeamCredential tmp = teamCredentialDao.findByContestAndTeam(contestId, teamId);
            if (tmp != null) {
                //file exists
                this.appendMsgElem(failedList, fileName, Constants.MSG_CREDENTIAL_EXISTS);
                continue;
            }

            School school = schoolDao.findById(team.getSchoolId());
            TeamCredential tc = new TeamCredential();


            StringBuilder key = new StringBuilder(contest.getContestTitle());
            key.append("-");
            key.append(school.getSchoolName());
            key.append("-");
            key.append(team.getTeamName());

            StringBuilder resp = new StringBuilder();   // url base if success else error message
            code = ossService.uploadBytes(key.toString(), bytes, ossService.BUCKET_PRIVATE, resp);

            if (code != 0) {
                this.appendMsgElem(failedList, fileName, resp.toString());
                continue;
            }

            tc.setName(key.toString());
            tc.setContestId(contestId);
            tc.setTeamId(teamId);
            tc.setUploadTime(Calendar.getInstance().getTime());
            tc.setBucket(ossService.BUCKET_PRIVATE);

            try {
                this.teamCredentialDao.insert(tc);
            } catch (Exception e) {
                e.printStackTrace();
                //TODO: add exception resp
            }
            successList.add(entry.getKey());
        }
        return Constants.SUCCESS;
    }

    public Integer getTeamCredentialUrl(Integer id, StringBuilder resUrl) {
        TeamCredential tc = this.teamCredentialDao.findById(id);
        if (tc == null) {
            return Constants.CREDENTIAL_NOT_EXISTS;
        }
        Bucket bucket = this.bucketDao.findByName(tc.getBucket());
        if (bucket == null) {
            return Constants.FAIL;
        }
        String url = ossService.genPrivateUrl(tc.getName(), bucket.getDomain());
        resUrl.append(url);
        return Constants.SUCCESS;
    }

    public byte[] getTeamCredentialFile(Integer id) {
        StringBuilder sb = new StringBuilder();
        System.out.println("id: " + id);
        int code = getTeamCredentialUrl(id, sb);

        if (code != Constants.SUCCESS) {
            return null;
        }
        byte[] res = HTTPUtil.getByteFrom(sb.toString());
        return res;
    }

    public Integer getLoadTeamCredentialAsZip(List<Integer> ids,
                                              ZipOutputStream zipOutputStream) {
        /* for test only
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File("/Users/lixiuwen/Downloads/icpc_test/test.zip"));
            zipOutputStream = new ZipOutputStream(fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/

        String base = "Proof of Awards/";
        ZipEntry dir = new ZipEntry(base);
        try {
            zipOutputStream.putNextEntry(dir);
            zipOutputStream.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Integer id : ids) {
            byte[] res = getTeamCredentialFile(id);
            if (res == null) {
                //TODO: add error resp msg
                continue;
            }
            /* for test only
            File f = new File("/Users/lixiuwen/Downloads/icpc_test/gao.pdf");
            try {
                FileOutputStream fos_2 = new FileOutputStream(f);
                fos_2.write(res);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            TeamCredential tc = teamCredentialDao.findById(id);
            String name = tc.getName();

            //TODO: currently fixed as pdf format, allow more in the future
            ZipEntry zipEntry = new ZipEntry(base + name + ".pdf");

            try {
                zipOutputStream.putNextEntry(zipEntry);
                zipOutputStream.write(res);
                zipOutputStream.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            zipOutputStream.flush();
            zipOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Constants.SUCCESS;
    }


    /**
     * soloCrendtial Related
     */
    public Integer saveSoloCredential(ZipInputStream zipFile, Integer soloContestId,
                                      List<Map<String, String>> failedList, List<String> successList) {
        SoloContest soloContest = soloContestDao.findById(soloContestId);
        if (soloContest == null) {
            return Constants.SOLO_CONTEST_NOT_EXISTS;
        }

        Map<String, byte[]> map = new HashMap<>();
        int code = ZipUtil.extractFiles(zipFile, map);

        if (code != 0) {
            // zip file extraction error
            return Constants.ZIP_ERROR;
        }

        for (Map.Entry<String, byte[]> entry : map.entrySet()) {
            String fileName = entry.getKey();
            String[] parts = fileName.split("/");
            String indent = parts[parts.length - 1];

            byte[] bytes = entry.getValue();

            indent = indent.trim();
            String[] bases = indent.split("\\.");

            String studentIdStr = bases[0];
            Integer studentId = null;
            try {
                studentId = Integer.parseInt(studentIdStr);
            } catch (NumberFormatException e) {
                this.appendMsgElem(failedList, fileName, Constants.MSG_NAME_NOT_NUMBER);
                continue;
            }
            Student stu = studentDao.findById(studentId);

            //make sure unique
            if (stu == null) {
                this.appendMsgElem(failedList, fileName, Constants.MSG_FILE_NAME_NO_MAPPING);
                continue;
            }

            SoloCredential tmp = soloCredentialDao.findBySoloContestAndStudent(soloContestId, studentId);
            if (tmp != null) {
                this.appendMsgElem(failedList, fileName, Constants.MSG_CREDENTIAL_EXISTS);
                continue;
            }

            School school = schoolDao.findById(stu.getSchoolId());
            SoloCredential sc = new SoloCredential();


            StringBuilder key = new StringBuilder(soloContest.getTitle());
            key.append("-");
            key.append(school.getSchoolName());
            key.append("-");
            key.append(stu.getStuName());

            StringBuilder resp = new StringBuilder();   // url base if success else error message
            code = ossService.uploadBytes(key.toString(), bytes, ossService.BUCKET_PRIVATE, resp);

            if (code != 0) {
                this.appendMsgElem(failedList, fileName, resp.toString());
                continue;
            }
            sc.setName(key.toString());
            sc.setSoloContestId(soloContestId);
            sc.setStudentId(studentId);
            sc.setUploadTime(Calendar.getInstance().getTime());
            sc.setBucket(ossService.BUCKET_PRIVATE);

            int c = this.soloCredentialDao.insert(sc);

            successList.add(entry.getKey());
        }
        return Constants.SUCCESS;
    }

    public Integer getSoloCredentialAsZip(List<Integer> ids,
                                          ZipOutputStream zipOutputStream) {
        String base = "Proof of Awards/";
        ZipEntry dir = new ZipEntry(base);
        try {
            zipOutputStream.putNextEntry(dir);
            zipOutputStream.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Integer id : ids) {
            byte[] res = getSoloCredentialFile(id);
            if (res == null) {
                continue;
            }
            SoloCredential sc = soloCredentialDao.findById(id);
            String name = sc.getName();

            //TODO: currently fixed as pdf format, allow more in the future
            ZipEntry zipEntry = new ZipEntry(base + name + ".pdf");

            try {
                zipOutputStream.putNextEntry(zipEntry);
                zipOutputStream.write(res);
                zipOutputStream.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            zipOutputStream.flush();
            zipOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Constants.SUCCESS;
    }

    public Integer getSoloCredentialUrl(Integer id, StringBuilder resUrl) {
        SoloCredential cc = this.soloCredentialDao.findById(id);
        if (cc == null) {
            return Constants.CREDENTIAL_NOT_EXISTS;
        }
        Bucket bucket = this.bucketDao.findByName(cc.getBucket());
        if (bucket == null) {
            return Constants.FAIL;
        }
        String url = ossService.genPrivateUrl(cc.getName(), bucket.getDomain());
        resUrl.append(url);
        return Constants.SUCCESS;
    }

    public byte[] getSoloCredentialFile(Integer id) {
        StringBuilder sb = new StringBuilder();
        int code = getSoloCredentialUrl(id, sb);
        if (code != Constants.SUCCESS) {
            return null;
        }
        byte[] res = HTTPUtil.getByteFrom(sb.toString());
        return res;
    }
}



