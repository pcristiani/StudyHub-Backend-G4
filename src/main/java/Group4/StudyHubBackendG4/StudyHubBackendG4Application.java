package Group4.StudyHubBackendG4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
public class StudyHubBackendG4Application {

	public static void main(String[] args) {
		SpringApplication.run(StudyHubBackendG4Application.class, args);
		abrirNavegadorConRuntime("http://localhost:8080/swagger-ui/index.html");
	}

	private static void abrirNavegadorConRuntime(String url) {
		try {
			Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
		} catch (Exception e) {
			System.err.println("ERROR AL ABRIR EL NAVEGADOR!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		}
	}
}
