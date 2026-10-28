package co.edu.unbosque.shopflow.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "ShopFlow",
        version = "1.0.0",
        description = "Procesamiento y consulta de pedidos"))
public class OpenApiConfig {

}