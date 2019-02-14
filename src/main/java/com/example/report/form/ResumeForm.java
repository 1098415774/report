package com.example.report.form;


import com.example.report.entity.ResumeEntity;
import com.sh.base.form.BaseForm;

public class ResumeForm extends BaseForm<ResumeEntity> {
    /*
     * 申请人姓名
     * */
    private String name;
    /*
     * 申请人手机号
     * */
    private String mobile;
    /*
     * 申请岗位
     * */
    private String post;
    /*
     * 地址
     * */
    private String location;
    /*
     * 介绍人ID
     * */
    private Integer reference;

    private Integer attribution;

    public Integer getAttribution() {
        return attribution;
    }

    public void setAttribution(Integer attribution) {
        this.attribution = attribution;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getReference() {
        return reference;
    }

    public void setReference(Integer reference) {
        this.reference = reference;
    }

    @Override
    public void FormToEntity(ResumeEntity entity) throws Exception{
        super.FormToEntity(entity);
        System.out.println(toString());

        entity.setReference(this.getReference());
        entity.setPost(this.getPost());
        entity.setName(this.getName());
        entity.setMobile(this.getMobile());
        entity.setLocation(this.getLocation());
        entity.setAttribution(this.getAttribution());
    }


    @Override
    public String toString() {
        return "location: " + this.location + "\n mobile: " + this.mobile +
                "\n name: " + this.name + "\n post: " + this.post + "\n attribution：" + this.attribution;
    }
}
