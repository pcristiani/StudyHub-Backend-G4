package Group4.StudyHubBackendG4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
public class StudyHubBackendG4Application {

	public static void main(String[] args) {
		SpringApplication.run(StudyHubBackendG4Application.class, args);
		abrirNavegadorConRuntime("http://localhost:8080/swagger-ui/index.html");
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("*")
						.allowedOrigins("*")
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
						.allowedHeaders("*");
			}
		};
	}

	private static void abrirNavegadorConRuntime(String url) {
		try {
			Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
		} catch (Exception e) {
			System.err.println("ERROR AL ABRIR EL NAVEGADOR!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		}
	}
}

