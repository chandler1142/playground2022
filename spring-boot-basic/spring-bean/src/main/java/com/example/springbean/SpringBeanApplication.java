package com.example.springbean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Bean的完整生命周期经历了各种方法调用，这些方法可以划分为以下几类：
 *
 * 1、Bean自身的方法
 *  这个包括了Bean本身调用的方法和通过配置文件中<bean>的init-method和destroy-method指定的方法
 *
 * 2、Bean级生命周期接口方法
 *  这个包括了BeanNameAware、BeanFactoryAware、InitializingBean和DiposableBean这些接口的方法
 *
 * 3、容器级生命周期接口方法
 *  这个包括了InstantiationAwareBeanPostProcessor 和 BeanPostProcessor 这两个接口实现，一般称它们的实现类为“后处理器”。
 *
 * 4、工厂后处理器接口方法
 *  这个包括了AspectJWeavingEnabler, ConfigurationClassPostProcessor, CustomAutowireConfigurer等等非常有用的工厂后处理器　　接口的方法。工厂后处理器也是容器级的。在应用上下文装配配置文件之后立即调用
 */
public class SpringBeanApplication {

    public static void main(String[] args) {
        System.out.println("现在开始初始化容器");
        ApplicationContext factory = new ClassPathXmlApplicationContext("application.xml");
        System.out.println("容器初始化成功");

        Person person = factory.getBean("person111",Person.class);
        System.out.println(person);

        System.out.println("现在开始关闭容器！");
        ((ClassPathXmlApplicationContext)factory).registerShutdownHook();
    }

}
