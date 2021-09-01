package com.zhf.tkmapperstudy.config;

import com.zhf.tkmapperstudy.help.util.StaticUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


@Component
public class SpringConfig implements ApplicationContextAware {

    @Value("${spring.datasource.master.jdbc-url}")
    private String dataUrl;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        StaticUtil.activeProfiles = applicationContext.getEnvironment().getActiveProfiles()[0];
        StaticUtil.dataSource = dataUrl.split(":")[1];
    }


}
