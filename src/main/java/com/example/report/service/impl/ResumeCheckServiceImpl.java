package com.example.report.service.impl;
import com.example.report.dao.ResumeCheckMapper;
import com.example.report.entity.ResumeCheckEntity;
import com.example.report.form.ResumeCheckForm;
import com.example.report.service.ResumeCheckService;
import com.sh.base.page.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class ResumeCheckServiceImpl implements ResumeCheckService {
    @Autowired
    private ResumeCheckMapper resumeCheckMapper;

    @Autowired
    private PageBean pageBean;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return resumeCheckMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(ResumeCheckEntity record) {
        return resumeCheckMapper.insert(record);
    }

    @Override
    public int insertSelective(ResumeCheckEntity record) {
        return resumeCheckMapper.insertSelective(record);
    }

    @Override
    public ResumeCheckEntity selectByReference(Integer reference) {
        return resumeCheckMapper.selectByReference(reference);
    }

    @Override
    public List<ResumeCheckEntity> selectByEntity(ResumeCheckEntity entity) {
        return resumeCheckMapper.selectByEntity(entity);
    }

    @Override
    public PageBean selectPageByEntity(ResumeCheckEntity entity) throws Exception {
        HashMap<String,Object> map = new HashMap<>();
        map.put("entity",entity);
        Integer totalRecord = resumeCheckMapper.selectCountByMap(map);
        pageBean.setTotalRecord(totalRecord);
        map.put("startindex",pageBean.getStartIndex());
        map.put("pagesize",pageBean.getPageSize());
        List<ResumeCheckEntity> resultlist = resumeCheckMapper.selectByMap(map);
        List<ResumeCheckForm> formList = new ArrayList<>();
        for (ResumeCheckEntity entity1 : resultlist){
            ResumeCheckForm form = new ResumeCheckForm();
            form.EntityToForm(entity1);
            formList.add(form);
        }
        pageBean.setData(formList);
        pageBean.setTotalPage(totalRecord%pageBean.getPageSize() == 0 ? totalRecord/pageBean.getPageSize() : totalRecord/pageBean.getPageSize() + 1);
        return pageBean;
    }

    @Override
    public int updateByPrimaryKeySelective(ResumeCheckEntity record) {
        return resumeCheckMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(ResumeCheckEntity record) {
        return resumeCheckMapper.updateByPrimaryKey(record);
    }
}
