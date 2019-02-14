package com.example.report.dao;

import com.example.report.entity.ResumeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
@Mapper
public interface ResumeMapper {
    void insert(ResumeEntity resumeEntity);

    List<ResumeEntity> selectAll();

    List<ResumeEntity> selectByReference(Integer reference);

    List<ResumeEntity> selectByEntity(ResumeEntity entity);

    List<ResumeEntity> selectByEntityAndTime(HashMap map);

    Integer selectCountByEntityAndTime(HashMap map);

}
