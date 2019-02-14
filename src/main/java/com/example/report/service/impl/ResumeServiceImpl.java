package com.example.report.service.impl;

import com.example.report.dao.ResumeMapper;
import com.example.report.entity.ResumeEntity;
import com.example.report.form.ResumeForm;
import com.example.report.service.ResumeService;
import com.sh.base.page.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service(value = "resumeService")
@Transactional
public class ResumeServiceImpl implements ResumeService {
    @Autowired
    private ResumeMapper resumeMapper;

    @Autowired
    private PageBean pageBean;

    @Override
    public void insert(ResumeEntity resumeEntity) {
        resumeMapper.insert(resumeEntity);
    }

    @Override
    public List<ResumeEntity> selectAll() {
        return resumeMapper.selectAll();
    }

    @Override
    public List<ResumeEntity> selectByReference(Integer reference) {
        return resumeMapper.selectByReference(reference);
    }

    @Override
    public List<ResumeEntity> selectByEntity(ResumeEntity entity) {
        return resumeMapper.selectByEntity(entity);
    }

    @Override
    public List<ResumeEntity> selectByEntityAndTime(ResumeEntity entity, Date startTime, Date endTime) {
        HashMap<String,Object> map = new HashMap<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        map.put("entity",entity);
        String start = null;
        String end = null;
        if (startTime != null){
            start = df.format(startTime);
        }
        if (endTime != null){
            end = df.format(endTime);
        }
        map.put("starttime",start);
        map.put("endtime",end);
        return resumeMapper.selectByEntityAndTime(map);
    }

    @Override
    public PageBean selectPageByEntityAndTime(ResumeEntity entity, Date startTime, Date endTime) throws Exception {
        HashMap<String,Object> map = new HashMap<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        map.put("entity",entity);
        String start = null;
        String end = null;
        if (startTime != null){
            start = df.format(startTime);
        }
        if (endTime != null){
            end = df.format(endTime);
        }
        map.put("starttime",start);
        map.put("endtime",end);
        Integer totalRecord =resumeMapper.selectCountByEntityAndTime(map);
        pageBean.setTotalRecord(totalRecord);
        map.put("startindex",pageBean.getStartIndex());
        map.put("pagesize",pageBean.getPageSize());
        List<ResumeEntity> resultlist = resumeMapper.selectByEntityAndTime(map);
        List<ResumeForm> formList = new ArrayList<>();
        for (ResumeEntity entity1 : resultlist){
            ResumeForm form1 = new ResumeForm();
            form1.EntityToForm(entity1);
            formList.add(form1);
        }
        pageBean.setData(formList);
        pageBean.setTotalPage(totalRecord%pageBean.getPageSize() == 0 ? totalRecord/pageBean.getPageSize() : totalRecord/pageBean.getPageSize() + 1);
        return pageBean;
    }
}
