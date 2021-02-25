package com.boot.yuncourier.druid;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: skwen
 * @Description: DruidConfiguration-Druid監控配置
 * @Date: 2020-02-01
 */
@Configuration
public class DruidConfiguration {
    @Value("${spring.datasource.druid.stat-view-servlet.login-username}")
    private String username;
    @Value("${spring.datasource.druid.stat-view-servlet.login-password}")
    private String password;
    @Value("${spring.datasource.druid.stat-view-servlet.reset-enable}")
    private String resetEnable;
    @Value("${spring.datasource.druid.stat-view-servlet.allow}")
    private String allow;
    @Value("${spring.datasource.druid.stat-view-servlet.deny}")
    private String deny;
    @Value("${spring.datasource.druid.web-stat-filter.url-pattern}")
    private String urlPattern;
    @Value("${spring.datasource.druid.web-stat-filter.exclusions}")
    private String exclusions;


    /**
     * 配置监控服务器
     *
     * @return 返回监控注册的servlet对象
     */
    @Bean
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        // 添加IP白名单
        servletRegistrationBean.addInitParameter("allow", allow);
        // 添加IP黑名单，当白名单和黑名单重复时，黑名单优先级更高
        servletRegistrationBean.addInitParameter("deny", deny);
        // 添加控制台管理用户
        servletRegistrationBean.addInitParameter("loginUsername", username);
        servletRegistrationBean.addInitParameter("loginPassword", password);
        // 是否能够重置数据
        servletRegistrationBean.addInitParameter("resetEnable", resetEnable);
        return servletRegistrationBean;
    }

    /**
     * 配置服务过滤器
     *
     * @return 返回过滤器配置对象
     */
    @Bean
    public FilterRegistrationBean statFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        // 添加过滤规则
        filterRegistrationBean.addUrlPatterns(urlPattern);
        // 忽略过滤格式
        filterRegistrationBean.addInitParameter("exclusions", exclusions);
        return filterRegistrationBean;
    }
}