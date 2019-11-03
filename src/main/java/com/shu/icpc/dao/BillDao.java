package com.shu.icpc.dao;

import com.shu.icpc.entity.Bill;

import java.util.List;
import java.util.Map;

public interface BillDao {
    List<Bill> findAll();

    List<Bill> findBySchoolId(Integer schoolId);

    Bill findById(Integer id);

    int updateCheckedById(Integer id, Integer checked);

    int insert(Bill bill);

    int update(Bill bill);

    int delete(Integer id);
}
