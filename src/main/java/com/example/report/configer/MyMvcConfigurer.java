package com.example.report.configer;

import com.sh.base.page.PageBean;
import com.sh.base.page.PageContants;
import com.sh.base.page.PageInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
public class MyMvcConfigurer extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(pageInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

    @Bean
    @Scope("request")
    public PageBean pageBean(){
        PageBean pageBean = new PageBean();
        pageBean.setPageNum(1);
        return pageBean;
    }

    @Bean
    public PageInterceptor pageInterceptor(){
        return new PageInterceptor();
    }
}
