package com.xcs.spring;

import com.xcs.spring.config.ChildConfiguration;
import com.xcs.spring.config.MyConfiguration;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.context.request.SessionScope;

import java.security.AccessControlContext;

/**
 * @author xcs
 * @date 2023年11月24日 13时56分
 **/
public class ConfigurableBeanFactoryDemo {

    public static void main(String[] args) {
        // 创建 ApplicationContext
        ConfigurableBeanFactory configurableBeanFactory = new AnnotationConfigApplicationContext(MyConfiguration.class, ChildConfiguration.class).getBeanFactory();

        // 设置父级 BeanFactory
        configurableBeanFactory.setParentBeanFactory(new DefaultListableBeanFactory());

        // 获取BeanPostProcessor数量
        int beanPostProcessorCount = configurableBeanFactory.getBeanPostProcessorCount();
        System.out.println("获取BeanPostProcessor数量: " + beanPostProcessorCount);

        configurableBeanFactory.registerScope("customScope", new Scope() {
            @Override
            public Object get(String name, ObjectFactory<?> objectFactory) {
                return null;
            }

            @Override
            public Object remove(String name) {
                return null;
            }

            @Override
            public void registerDestructionCallback(String name, Runnable callback) {

            }

            @Override
            public Object resolveContextualObject(String key) {
                return null;
            }

            @Override
            public String getConversationId() {
                return "";
            }
        });

        // 获取所有已注册的 Scope 名称
        String[] scopeNames = configurableBeanFactory.getRegisteredScopeNames();
        System.out.println("获取所有已注册的Scope名称: " + String.join(", ", scopeNames));

        // 获取注册的 Scope
        Scope customScope = configurableBeanFactory.getRegisteredScope("customScope");
        System.out.println("获取注册的Scope :" + customScope);

        // 获取ApplicationStartup
        //ApplicationStartup applicationStartup = configurableBeanFactory.getApplicationStartup();
        //System.out.println("获取ApplicationStartup: " + applicationStartup);

        // 获取AccessControlContext
        AccessControlContext accessControlContext = configurableBeanFactory.getAccessControlContext();
        System.out.println("获取AccessControlContext: " + accessControlContext);

        // 拷贝配置
        ConfigurableListableBeanFactory otherFactory = new DefaultListableBeanFactory();
        otherFactory.registerScope("myScope", new SessionScope());
        configurableBeanFactory.copyConfigurationFrom(otherFactory);
        System.out.println("拷贝配置copyConfigurationFrom: " + otherFactory);

        // 获取所有已注册的 Scope 名称，复制过后的
        String[] scopeNames2 = configurableBeanFactory.getRegisteredScopeNames();
        System.out.println("获取所有已注册的Scope名称: " + String.join(", ", scopeNames2));

        // 注册别名
        String beanName = "myService";
        String alias = "helloService";
        configurableBeanFactory.registerAlias(beanName, alias);
        configurableBeanFactory.registerAlias("myChildService","childService");
        System.out.println("注册别名registerAlias, BeanName: " + beanName + "alias: " + alias);

        // 获取合并后的 BeanDefinition
        BeanDefinition mergedBeanDefinition = configurableBeanFactory.getMergedBeanDefinition("myService");
        System.out.println("获取合并后的 BeanDefinition: " + mergedBeanDefinition);

        // 获取合并后的 BeanDefinition，childService的配置类是继承myService的，但是scope设置的不一样
        BeanDefinition mergedBeanDefinition2 = configurableBeanFactory.getMergedBeanDefinition("childService");
        System.out.println("获取合并后的 BeanDefinition: " + mergedBeanDefinition2);

        // 判断是否为 FactoryBean
        String factoryBeanName = "myService";
        boolean isFactoryBean = configurableBeanFactory.isFactoryBean(factoryBeanName);
        System.out.println("判断是否为FactoryBean:" + isFactoryBean);

        // 设置当前 Bean 是否正在创建
        String currentBeanName = "myService";
        boolean inCreation = true;
        configurableBeanFactory.setCurrentlyInCreation(currentBeanName, inCreation);
        System.out.println("设置当前Bean是否正在创建: " + currentBeanName);

        // 判断指定的 Bean 是否正在创建
        boolean isCurrentlyInCreation = configurableBeanFactory.isCurrentlyInCreation(currentBeanName);
        System.out.println("判断指定的Bean是否正在创建" + isCurrentlyInCreation);

        // 注册依赖关系，dependentBeanName 依赖于 beanName。即 dependentBeanName 在使用时，需要 beanName 的存在或已经创建
        String dependentBeanName = "dependentBean";
        configurableBeanFactory.registerDependentBean(beanName, dependentBeanName);
        System.out.println("注册依赖关系beanName: " + beanName + "\ndependentBeanName: " + dependentBeanName);

        // 获取所有依赖于指定 Bean 的 Bean 名称
        // 表示那些依赖于 beanName 的 Bean
        String[] dependentBeans = configurableBeanFactory.getDependentBeans(beanName);
        System.out.println("获取所有依赖于指定Bean的Bean名称: " + String.join(", ", dependentBeans));

        // 获取指定 Bean 依赖的所有 Bean 名称
        // 表示**beanName 依赖于哪些 Bean**
        // 1.子配置类不引用myService(),然后myService设置为SCOPE_PROTOTYPE，那么就会返回不需要依赖任何其他bean
        // 因为不会被立即创建，而是等到实际需要时才会生成实例。因此，在没有任何其他 Bean 主动引用 myService 时，它的依赖关系也就不会显式地记录在依赖图中
        // 2.没有子配置类，设置为单例，那么就依赖myConfiguration
        // 3.有子配置类，设置为单例，子类return new MyServiceImpl()，返回childConfiguration，设置为prototype，返回空
        // 因为子类代理覆盖父类，当 Spring 创建 ChildConfiguration 的代理对象时，它实际上覆盖了父类中的 @Bean 方法定义。即使 myService() 方法是在 MyConfiguration 中定义的，
        // Spring 会将它与 ChildConfiguration 的上下文联系在一起，因为 ChildConfiguration 是最终的代理类
        // 4.有子配置类，不管设置单例还是prototype，子类return myService()，返回依赖childConfiguration
        // 在实际使用中，如果某个 @Bean 方法调用了 myService()，而这个 @Bean 方法是在 ChildConfiguration 中声明的，那么 Spring 会认为 myService 的创建过程依赖于 ChildConfiguration
        String[] dependencies = configurableBeanFactory.getDependenciesForBean(beanName);
        System.out.println("获取指定Bean依赖的所有Bean名称: " + String.join(", ", dependencies));

        // 销毁指定 Bean 实例
        Object beanInstance = configurableBeanFactory.getBean(beanName);
        configurableBeanFactory.destroyBean(beanName, beanInstance);
        System.out.println("销毁指定 Bean 实例: " + beanName);

        // 销毁所有单例 Bean
        configurableBeanFactory.destroySingletons();
        System.out.println("销毁所有单例Bean destroySingletons" );
    }

}
