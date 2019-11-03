package com.shu.icpc.dao;

import javax.management.relation.Role;
import java.util.List;

public interface RoleDao {
    List<String> findByPhone(String phone);

    int insertUserRole(String phone, Integer roleId);

    Integer findRoleIdByRoleName(String name);
}
