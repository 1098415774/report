package com.example.report.service;


import com.example.report.entity.ResumeEntity;
import com.sh.base.page.PageBean;

import java.util.Date;
import java.util.List;

public interface ResumeService {

    void insert(ResumeEntity resumeEntity);

    List<ResumeEntity> selectAll();

    List<ResumeEntity> selectByReference(Integer reference);

    List<ResumeEntity> selectByEntity(ResumeEntity entity);

    List<ResumeEntity> selectByEntityAndTime(ResumeEntity entity, Date startTime, Date endTime);

    PageBean selectPageByEntityAndTime(ResumeEntity entity, Date startTime, Date endTime) throws Exception;
}
