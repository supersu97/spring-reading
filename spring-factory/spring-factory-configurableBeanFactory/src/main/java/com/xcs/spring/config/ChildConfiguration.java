package com.xcs.spring.config;

import com.xcs.spring.service.MyService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author subo
 * @date 2024/10/12
 * @apiNote
 */
@Configuration
public class ChildConfiguration extends MyConfiguration {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public MyService myChildService() {
        return myService();
        //return new MyServiceImpl();
    }
}
