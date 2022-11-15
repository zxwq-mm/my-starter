package com.sun.job;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * xxl-job config
 */
@Configuration
@ConditionalOnProperty(value = "xxl.job.enable", havingValue = "true", matchIfMissing = true)
public class XxlJobConfiguration {

    @Value("${xxl.job.admin.addresses}")
    private String adminAddresses;

    @Value("${xxl.job.executor.ip}")
    private String ip;

    @Value("${xxl.job.executor.port}")
    private int port;

    @Value("${xxl.job.accessToken}")
    private String accessToken;

    /**
     * XxlJobSpringExcutor
     */
    @Bean
    @ConditionalOnMissingBean
    public XxlJobSpringExecutor xxlJobExecutor(Environment environment) {
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(this.adminAddresses);
        xxlJobSpringExecutor.setAppname(environment.getRequiredProperty("spring.application.name"));
        xxlJobSpringExecutor.setIp(this.ip);
        xxlJobSpringExecutor.setPort(this.port);
        xxlJobSpringExecutor.setAccessToken(this.accessToken);
        xxlJobSpringExecutor.setLogPath(environment.getRequiredProperty("logging.file.path"));
        xxlJobSpringExecutor.setLogRetentionDays(-1);
        return xxlJobSpringExecutor;
    }
}
