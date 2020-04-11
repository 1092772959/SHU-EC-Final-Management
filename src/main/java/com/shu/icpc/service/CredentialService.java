package com.shu.icpc.service;

import com.shu.icpc.entity.*;
import com.shu.icpc.utils.Constants;
import com.shu.icpc.utils.FileUtil;
import com.shu.icpc.utils.ZipUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

    public List<TeamCredential> getTeamCredentialInfo(Integer contestId){
        List<TeamCredential> res = teamCredentialDao.findByContest(contestId);
        return res;
    }

    public List<TeamCredential> getTeamCredentiaInfo(Integer contestId, Integer schoolId){
        List<TeamCredential> res = teamCredentialDao.findByContestAndSchool(contestId, schoolId);
        return res;
    }

    public Integer saveContestCredential(ZipInputStream zipFile, Integer contestId,
                                         List<Map<String, String>> failedList,
                                         List<String> successList) {
        Contest contest = contestDao.findById(contestId);

        Map<String, byte[]> map = new HashMap<>();
        int code = ZipUtil.extractFiles(zipFile, map);

        if (code != 0) {
            // zip file extraction error
            return Constants.ZIP_ERROR;
        }

        for (Map.Entry<String, byte[]> entry : map.entrySet()) {
            String fileName = entry.getKey();
            String[] parts = fileName.split("/");
            String indent = parts[parts.length-1];

            byte[] bytes = entry.getValue();

            indent = indent.trim();
            String[] bases = indent.split("\\.");

            String teamIdStr = bases[0];
            Integer teamId = null;
            try{
                teamId = Integer.parseInt(teamIdStr);
            }catch(NumberFormatException e){
                Map<String, String> elem = new HashMap<>();
                elem.put("fileName", fileName);
                elem.put("error", Constants.MSG_NAME_NOT_NUMBER);
                failedList.add(elem);
                continue;
            }

            Team team = teamDao.findById(teamId);

            //make sure unique
            if (team == null) {
                Map<String, String> elem = new HashMap<>();
                elem.put("fileName", fileName);
                elem.put("error", Constants.MSG_CREDENTIAL_EXISTS);
                failedList.add(elem);
                continue;
            }

            TeamCredential tmp = teamCredentialDao.findByContestAndTeam(contestId, teamId);
            if(tmp != null){
                Map<String, String> elem = new HashMap<>();
                elem.put("fileName", fileName);
                elem.put("error", Constants.MSG_NAME_NOT_NUMBER);
                failedList.add(elem);
                continue;
            }

            School school = schoolDao.findById(team.getSchoolId());
            TeamCredential tc = new TeamCredential();


            StringBuilder key = new StringBuilder(contest.getContestTitle());
            key.append("-");
            key.append(school.getSchoolName());
            key.append("-");
            key.append(team.getTeamName());
            System.out.println(key);

            StringBuilder resp = new StringBuilder();   // url base if success else error message
            code = ossService.uploadBytes(key.toString(), bytes, ossService.BUCKET_PRIVATE, resp);

            if (code != 0) {
                System.out.println("tos failed");
                Map<String, String> elem = new HashMap<>();
                elem.put("fileName", entry.getKey());
                elem.put("error", resp.toString());
                failedList.add(elem);
                continue;
            }

            tc.setName(key.toString());
            tc.setContestId(contestId);
            tc.setTeamId(teamId);
            tc.setUploadTime(Calendar.getInstance().getTime());
            tc.setBucket(ossService.BUCKET_PRIVATE);

            //save to mysql
            try{
                this.teamCredentialDao.insert(tc);
            }catch(Exception e){
                //TODO: add exception resp
            }
            successList.add(entry.getKey());
        }
        return Constants.SUCCESS;
    }

    public Integer getTeamCredentialUrl(Integer id, StringBuilder resUrl){
        TeamCredential tc = this.teamCredentialDao.findById(id);
        if(tc == null){
            return Constants.CREDENTIAL_NOT_EXISTS;
        }
        Bucket bucket = this.bucketDao.findByName(tc.getBucket());
        if(bucket == null){
            return Constants.FAIL;
        }
        String url = ossService.genPrivateUrl(tc.getName(), bucket.getDomain());
        resUrl.append(url);
        return Constants.SUCCESS;
    }

    public byte[] getTeamCredentialFile(Integer id){
        StringBuilder sb = new StringBuilder();
        System.out.println("id: " + id);
        int code = getTeamCredentialUrl(id, sb);

        if(code != Constants.SUCCESS){
            return null;
        }
        URL url = null;
        byte[] res = null;
        try {
            url = new URL(sb.toString());
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(6 * 1000);
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            InputStream inputStream = conn.getInputStream();
            res = FileUtil.readInputStream(inputStream);
            /*
            // failed version: downloaded file cannot be opened as pdf
            int length = url.openConnection().getContentLength();
            InputStream is = url.openStream();
            res = new byte[length];
            is.read(res);
            is.close();
            */
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }


    public Integer getLoadTeamCredentialAsZip(List<Integer> ids, ZipOutputStream zipOutputStream) {
        //TODO: return a zipFile

        /*
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

        for(Integer id: ids){
            byte[] res = getTeamCredentialFile(id);
            if(res == null){
                //TODO: add error resp msg
                continue;
            }
            /*
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
            System.out.println(tc.getName());

            ZipEntry zipEntry = new ZipEntry(base + tc.getName() + ".pdf");

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


    //TODO: solo parts (same logic)
    public Integer saveSoloCredential(ZipInputStream zipFile, Integer soloContestId,
                                      List<Map<String, String>> failedName, List<String> successList) {


        return Constants.SUCCESS;
    }

}
