package com.dapeng.flow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 描述:swagger配置类
 *
 * @author liuxz
 * @date 2019/8/16 11:46
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 自行修改为自己的包路径
                .apis(RequestHandlerSelectors.basePackage("com.dapeng.flow.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("工作流微服务API文档")
//                .description("大鹏教育工作流微服务项目")
                //服务条款网址
//                .termsOfServiceUrl("https://blog.csdn.net/ysk_xh_521")
//                .version("1.0")
//                .contact(new Contact("liuxz", "http://liuxz.cn", "1510822551@qq.com"))
                .build();
    }
}

