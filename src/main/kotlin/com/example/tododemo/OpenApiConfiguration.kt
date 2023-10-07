package com.example.tododemo

import com.example.tododemo.entities.MyConfig
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component


@Configuration
class OpenAPIConfig {

    @Bean
    fun myOpenAPI(): OpenAPI {
        val info: Info = Info()
            .title("TODO Demo REST API")
            .version("1.0")
            .description("TODO Demo REST API")
        return OpenAPI().info(info)
    }

    @Bean
    fun myConfig(@Value("\${myapp.url}") url: String): MyConfig {
        return MyConfig(url)
    }

//    @Bean
//    fun myConfig(config: MyOtherConfig): MyConfig {
//        return MyConfig(config.url)
//    }
}
//
//@ConfigurationProperties(prefix = "myapp")
//@Component
//data class MyOtherConfig(
//    var url: String = ""
//)
