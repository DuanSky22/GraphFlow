package com.iscas.dataflow.util;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(value = { "com.iscas.dataflow" },excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "org.easyapm.server.client.*"))
//@Import({ DataConfiguration.class, MvcConfiguration.class })
//@Profile({ "prod" })
public class AppConfiguration {

}
