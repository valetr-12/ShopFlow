package co.edu.unbosque.shopflow.catalogo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ConfiguracionCatalogo {

	@Bean
	@Primary
	public ServicioCatalogo servicioCatalogo(CatalogoRemoto remoto) {
		return new CatalogoCacheProxy(remoto);
	}
}
