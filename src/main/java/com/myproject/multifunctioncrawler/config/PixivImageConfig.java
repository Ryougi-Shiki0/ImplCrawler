package com.myproject.multifunctioncrawler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.*;

public class PixivImageConfig implements WebMvcConfigurer {

    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/resources/");
    }
}
