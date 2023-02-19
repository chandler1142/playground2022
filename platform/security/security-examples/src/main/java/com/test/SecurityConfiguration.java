package com.test;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
/**
 * 三个configure方法被调用的先后顺序依次为：
 * configure(AuthenticationManagerBuilder auth)、
 * configure(HttpSecurity http)、
 * configure(WebSecurity web)
 */
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_PATH = "/login";

    private static final String LOGOUT_PATH = "/logout";

    /**
     * 配置哪些请求是否需要过滤以及配置自定义的过滤器等等
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/test2").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll()
                .and()
                .httpBasic()
        ;
    }

    /**
     * web应用的全局配置，常用于静态资源的处理配置
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    /**
     * 配置自定义的身份验证逻辑实现
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }

}
