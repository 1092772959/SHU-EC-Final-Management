package com.shu.icpc.service;

import com.shu.icpc.entity.Contest;
import com.shu.icpc.entity.TeamCredential;
import com.shu.icpc.entity.School;
import com.shu.icpc.entity.Team;
import com.shu.icpc.utils.Constants;
import com.shu.icpc.utils.ZipUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Service
@Transactional
public class CredentialService extends CoreService {

    public Integer saveContestCredential(ZipInputStream zipFile, Integer contestId,
                                         List<Map<String, String>> failedList,
                                         List<String> successList) {
        Contest contest = contestDao.findById(contestId);

        Map<String, byte[]> map = new HashMap<>();
        int code = ZipUtil.extractFiles(zipFile, map);

        if (code != 0) {
            // zip file extraction error
            return Constants.FAIL;
        }

        List<TeamCredential> teamCredentials = new ArrayList<>();

        for (Map.Entry<String, byte[]> entry : map.entrySet()) {
            String fileName = entry.getKey();
            String[] parts = fileName.split("/");
            String indent = parts[parts.length-1];

            byte[] bytes = entry.getValue();

            indent = indent.trim();
            System.out.println(fileName +", " + indent);
            String[] bases = indent.split("\\.");
            System.out.println(bases[0]);
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

            if (team == null) {
                Map<String, String> elem = new HashMap<>();
                elem.put("fileName", fileName);
                elem.put("error", Constants.MSG_FILE_NAME_NO_TEAM);
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


            try{
                this.teamCredentialDao.insert(tc);
            }catch(Exception e){
                //TODO: add exception resp
            }
            successList.add(entry.getKey());
        }
        return Constants.SUCCESS;
    }

    public Integer getTeamCredential(Integer id, StringBuilder sb){
        //TODO: return a single url
        return Constants.SUCCESS;
    }

    public Integer getTeamCredential(Integer contestId, ZipOutputStream zipOutputStream){
        //TODO: return a zipFile
        return Constants.SUCCESS;
    }


    public Integer saveSoloCredential(ZipInputStream zipFile, Integer soloContestId,
                                      List<Map<String, String>> failedName, List<String> successList) {
        //TODO: same logic

        return Constants.SUCCESS;
    }

}
