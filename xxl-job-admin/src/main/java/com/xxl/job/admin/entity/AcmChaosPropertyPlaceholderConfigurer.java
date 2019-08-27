 package com.xxl.job.admin.entity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;



/**
 * @author B250M-J
 * @date 2019/08/26
 */
@Configuration
public class AcmChaosPropertyPlaceholderConfigurer {

    @Bean
    @Scope("singleton")
    public ChaosPropertyPlaceholderConfigurers init() {
        ChaosPropertyPlaceholderConfigurers chaosPropertyPlaceholderConfigurer=new ChaosPropertyPlaceholderConfigurers();
        chaosPropertyPlaceholderConfigurer.setOrder(1);
        chaosPropertyPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(true);
        return  chaosPropertyPlaceholderConfigurer;
    }
    
}
