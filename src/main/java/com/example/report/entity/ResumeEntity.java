package com.example.report.entity;

import com.sh.base.entity.BaseEntity;

import javax.validation.constraints.NotNull;

public class ResumeEntity extends BaseEntity {
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

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(@NotNull String mobile) {
        this.mobile = mobile;
    }

    public String getPost() {
        return post;
    }

    public void setPost(@NotNull String post) {
        this.post = post;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(@NotNull String location) {
        this.location = location;
    }

    public Integer getReference() {
        return reference;
    }

    public void setReference(Integer reference) {
        this.reference = reference;
    }

}
