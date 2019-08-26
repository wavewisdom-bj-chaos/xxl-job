 package com.xxl.job.admin.entity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.wave.centimani.tool.component.deploy.ChaosPropertyPlaceholderConfigurer;


/**
 * @author B250M-J
 * @date 2019/08/26
 */
@Configuration
public class AcmChaosPropertyPlaceholderConfigurer {

   /* @Bean
    @Scope("singleton")
    public ChaosPropertyPlaceholderConfigurer init() {
        ChaosPropertyPlaceholderConfigurer chaosPropertyPlaceholderConfigurer=new ChaosPropertyPlaceholderConfigurer();
        chaosPropertyPlaceholderConfigurer.setOrder(1);
        chaosPropertyPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(true);
        return  chaosPropertyPlaceholderConfigurer;
    }*/
    
}
