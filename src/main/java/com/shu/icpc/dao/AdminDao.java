package com.shu.icpc.dao;

import com.shu.icpc.entity.Admin;

public interface AdminDao {
    Admin findByPhone(String phone);

    int insert(Admin admin);

}
