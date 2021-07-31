package com.nobblecrafts.xpto.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class XptoWebMvcConfigurer implements WebMvcConfigurer {

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    var pageHandler = new PageableHandlerMethodArgumentResolver();
    pageHandler.setFallbackPageable(PageRequest.of(0, 20));
    pageHandler.setOneIndexedParameters(true);
    resolvers.add(pageHandler);
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    if (!registry.hasMappingForPattern("/assets/**")) {
      registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/assets/");
    }
  }
}
