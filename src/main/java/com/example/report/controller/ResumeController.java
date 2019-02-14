package com.example.report.controller;

import com.example.report.ResumeContants;
import com.example.report.entity.ResumeCheckEntity;
import com.example.report.entity.ResumeEntity;
import com.example.report.form.ResumeCheckForm;
import com.example.report.form.ResumeForm;
import com.example.report.service.ResumeCheckService;
import com.example.report.service.ResumeService;
import com.sh.base.page.PageBean;
import com.sh.base.result.ResultConstant;
import com.sh.base.result.ResultData;
import com.sh.base.utils.StringUtils;
import com.sh.base.xls.CreateXlsFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/resume")
public class ResumeController {
    private static final Logger logger = LoggerFactory.getLogger(ResumeController.class);

    private final String isFirst = "100";

    private final String isNotFirst = "200";

    private final String SELECT_RESUME_LIST = "selectResumeList";

    private final String SELECT_RESUME_LIST_START = "start";

    private final String SELECT_RESUME_LIST_END = "end";

    private final String SELECT_RESUME_CHECK_LIST = "selectResumeCheckList";

    @Autowired
    private ResumeService resumeService;
    @Autowired
    private ResumeCheckService resumeCheckService;

    private ResultData resultData;

    @RequestMapping("/selectResumeAll")
    @ResponseBody
    public ResultData selectResume(ResumeForm form, HttpServletRequest request){
        resultData = new ResultData();
        try {
            String starttime = request.getParameter("starttime");
            String endtime = request.getParameter("endtime");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
            ResumeEntity entity = new ResumeEntity();
            //服务器上tomcat是默认编码，所以要进行转码，如果是本地tomcat是utf-8编码要把这行代码去掉
//            StringUtils.ISO88591ToUTF8(form);
            form.FormToEntity(entity);
            Date start = null;
            Date end = null;
            if (StringUtils.isNotEmpty(starttime)){
                start = df.parse(starttime);
            }
            if (StringUtils.isNotEmpty(endtime)){
                end = df.parse(endtime);
            }
            request.getSession().setAttribute(SELECT_RESUME_LIST,entity);
            request.getSession().setAttribute(SELECT_RESUME_LIST_START,start);
            request.getSession().setAttribute(SELECT_RESUME_LIST_END,end);
            resultData.setState(ResultConstant.SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            resultData.setState(ResultConstant.ERROR);
            resultData.setMsg(e.getMessage());
        }
        return resultData;
    }



    @RequestMapping("/resumeSelectList")
    public String resumeSelect(HttpServletRequest request){
        return "resumelist/resumeselect";
    }

    @RequestMapping("/listResume")
    public String listResume(){
        return "resumelist/resumelist";
    }

    @RequestMapping("listResumeJson")
    @ResponseBody
    public ResultData listResumeJson(ResumeForm form,HttpServletRequest request){
        resultData = new ResultData();
        ResumeEntity entity = (ResumeEntity) request.getSession().getAttribute(SELECT_RESUME_LIST);
        Date start = (Date) request.getSession().getAttribute(SELECT_RESUME_LIST_START);
        Date end = (Date) request.getSession().getAttribute(SELECT_RESUME_LIST_END);
        try {
            PageBean pageBean = resumeService.selectPageByEntityAndTime(entity,start,end);
            resultData.setRows(pageBean.getData());
            resultData.setTotal(pageBean.getTotalRecord());
            resultData.setState(ResultConstant.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultData;
    }


    @RequestMapping("/selectResumeCheckAll")
    @ResponseBody
    public ResultData selectResumeCheck(ResumeCheckForm form, HttpServletRequest request){
        resultData = new ResultData();
        try {
            ResumeCheckEntity entity = new ResumeCheckEntity();
            //服务器上tomcat是默认编码，所以要进行转码，如果是本地tomcat是utf-8编码要把这行代码去掉
//            StringUtils.ISO88591ToUTF8(form);
            form.FormToEntity(entity);
//            List<ResumeCheckEntity> resultList = resumeCheckService.selectByEntity(entity);
//            List<ResumeCheckForm> formList = new ArrayList<>();
//            for (ResumeCheckEntity entity1 : resultList){
//                ResumeCheckForm form1 = new ResumeCheckForm();
//                form1.EntityToForm(entity1);
//                formList.add(form1);
//            }
            request.getSession().setAttribute(SELECT_RESUME_CHECK_LIST,entity);
            resultData.setState(ResultConstant.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            resultData.setState(ResultConstant.SUCCESS);
            resultData.setMsg(e.getMessage());
        }
        return resultData;
    }

    @RequestMapping("listResumeCheckJson")
    @ResponseBody
    public ResultData listResumeCheckJson(HttpServletRequest request){
        resultData = new ResultData();
        ResumeCheckEntity entity = (ResumeCheckEntity) request.getSession().getAttribute(SELECT_RESUME_CHECK_LIST);
        try {
            PageBean pageBean = resumeCheckService.selectPageByEntity(entity);
            resultData.setRows(pageBean.getData());
            resultData.setTotal(pageBean.getTotalRecord());
            resultData.setState(ResultConstant.SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        return resultData;
    }

    @RequestMapping(value = "/resumeXlsDownload",method = RequestMethod.GET)
    public ResponseEntity<byte[]> resumeXlsFileDownload(HttpServletRequest request){
        HttpHeaders headers = new HttpHeaders();//http头信息
        File file = null;
        byte[] filedata = null;
        try {
            ResumeEntity entity = (ResumeEntity) request.getSession().getAttribute(SELECT_RESUME_LIST);
            Date start = (Date) request.getSession().getAttribute(SELECT_RESUME_LIST_START);
            Date end = (Date) request.getSession().getAttribute(SELECT_RESUME_LIST_END);
            List<ResumeEntity> entityList = resumeService.selectByEntityAndTime(entity,start,end);
            List<ResumeForm> resultlist = new ArrayList<>();
            for (ResumeEntity entity1 : entityList){
                ResumeForm form = new ResumeForm();
                form.EntityToForm(entity1);
                resultlist.add(form);
            }
            file = createResumeXlsFile(resultlist);
            InputStream is = new FileInputStream(file);
            filedata = new byte[is.available()];
            is.read(filedata);
            is.close();
            String filename = "招聘报名表.xls";
            String downloadFileName = new String(filename.getBytes("UTF-8"),"iso-8859-1");//设置编码

            headers.setContentDispositionFormData("attachment", downloadFileName);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return new ResponseEntity<byte[]>(filedata,headers,HttpStatus.CREATED);
    }

    private File createResumeXlsFile(List<ResumeForm> resultlist) throws Exception {
        File file = new File("招聘报名表.xls");
        CreateXlsFile xls = new CreateXlsFile(file);
        String[] title = {"ID","申请人姓名","申请人手机号","申请岗位",
                "申请公司","介绍人ID","地址","申请时间"};
        List<String[]> datas = new ArrayList<>();
        for (ResumeForm form : resultlist){
            String[] data = new String[8];
            data[0] = String.valueOf(form.getId());
            data[1] = form.getName();
            data[2] = form.getMobile();
            data[3] = form.getPost();
            String attribution = "";
            if (form.getAttribution() != null){
                switch (form.getAttribution()){
                    case 0:
                        attribution = "盛华";
                        break;
                    case 1:
                        attribution = "科慧";
                        break;
                }
            }
            data[4] = attribution;
            data[5] = String.valueOf(form.getReference());
            data[6] = form.getLocation();
            data[7] = form.getCreateTime();
            datas.add(data);
        }

        xls.setTitle(title);
        xls.setData(datas);
        return xls.write();
    }

    @RequestMapping(value = "/resumeCheckXlsDownload",method = RequestMethod.GET)
    public ResponseEntity<byte[]> resumeCheckXlsFileDownload(HttpServletRequest request){
        HttpHeaders headers = new HttpHeaders();//http头信息
        File file = null;
        byte[] filedata = null;
        try {
            ResumeCheckEntity entity = (ResumeCheckEntity) request.getSession().getAttribute(SELECT_RESUME_CHECK_LIST);
            List<ResumeCheckEntity> entityList = resumeCheckService.selectByEntity(entity);
            List<ResumeCheckForm> resultlist = new ArrayList<>();
            for (ResumeCheckEntity entity1 : entityList){
                ResumeCheckForm form = new ResumeCheckForm();
                form.EntityToForm(entity1);
                resultlist.add(form);
            }
            file = createResumeCheckXlsFile(resultlist);
            InputStream is = new FileInputStream(file);
            filedata = new byte[is.available()];
            is.read(filedata);
            is.close();
            String filename = "转发信息表.xls";
            String downloadFileName = new String(filename.getBytes("UTF-8"),"iso-8859-1");//设置编码

            headers.setContentDispositionFormData("attachment", downloadFileName);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return new ResponseEntity<byte[]>(filedata,headers,HttpStatus.CREATED);
    }

    private File createResumeCheckXlsFile(List<ResumeCheckForm> resultlist) throws Exception {
        File file = new File("转发信息表.xls");
        CreateXlsFile xls = new CreateXlsFile(file);
        String[] title = {"ID","员工工号","转发次数","是否获得奖励", "所属公司"};
        List<String[]> datas = new ArrayList<>();
        for (ResumeCheckForm form : resultlist){
            String[] data = new String[8];
            data[0] = String.valueOf(form.getId());
            data[1] = String.valueOf(form.getReference());
            data[2] = String.valueOf(form.getIsrewarded());
            String isrewarded = "";
            if (form.getIsrewarded() != null){
                switch (form.getIsrewarded()){
                    case 0:
                        isrewarded = "否";
                        break;
                    case 1:
                        isrewarded = "是";
                        break;
                }
            }
            data[3] = isrewarded;
            String attribution = "";
            if (form.getAttribution() != null){
                switch (form.getAttribution()){
                    case 0:
                        attribution = "盛华";
                        break;
                    case 1:
                        attribution = "科慧";
                        break;
                }
            }
            data[4] = attribution;
            datas.add(data);
        }

        xls.setTitle(title);
        xls.setData(datas);
        return xls.write();
    }

    @RequestMapping("/resumeCheckSelectList")
    public String resumeCheckSelect(){
        return "resumelist/resumecheckselect";
    }

    @RequestMapping("/listResumeCheck")
    public String listResumeCheck(){
        return "resumelist/resumechecklist";
    }


}
