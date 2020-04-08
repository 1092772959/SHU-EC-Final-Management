package com.shu.icpc.dao;

import com.shu.icpc.entity.Bucket;

public interface BucketDao {

    Bucket findByName(String name);
}
