package com.shu.icpc.service;

import com.shu.icpc.entity.Admin;
import org.springframework.stereotype.Service;

@Service
public class AdminService extends CoreService{

    public Admin getByPhone(String phone){
        return adminDao.findByPhone(phone);
    }
}
