package com.shu.icpc.Component.credential;


import com.shu.icpc.Component.CredentialNameGetter;
import com.shu.icpc.entity.SoloCredential;
import com.shu.icpc.service.CoreService;

public class SoloCredentialNameGetter extends CoreService implements CredentialNameGetter {
    @Override
    public String getName(Integer id) {
        SoloCredential cc = this.soloCredentialDao.findById(id);
        if(cc == null) return null;
        return cc.getName();
    }
}
