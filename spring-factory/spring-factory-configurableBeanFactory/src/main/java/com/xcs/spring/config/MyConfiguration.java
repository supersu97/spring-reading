package com.xcs.spring.config;

import com.xcs.spring.service.MyService;
import com.xcs.spring.service.impl.MyServiceImpl;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author xcs
 * @date 2023年11月24日 14时17分
 **/
@Configuration
public class MyConfiguration {
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MyService myService() {
        return new MyServiceImpl();
    }
}
