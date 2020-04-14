package com.shu.icpc.Component.credential;


import com.shu.icpc.Component.CredentialNameGetter;
import com.shu.icpc.entity.TeamCredential;
import com.shu.icpc.service.CoreService;

public class TeamCredentialNameGetter extends CoreService implements CredentialNameGetter {
    @Override
    public String getName(Integer id) {
        TeamCredential tc = this.teamCredentialDao.findById(id);
        if(tc == null) return null;
        return tc.getName();
    }
}