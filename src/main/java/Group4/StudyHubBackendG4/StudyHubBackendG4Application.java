package Group4.StudyHubBackendG4;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
public class StudyHubBackendG4Application {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(StudyHubBackendG4Application.class, args);
		initializeFirebase();
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

	private static void initializeFirebase() throws IOException {
		InputStream serviceAccountKey = StudyHubBackendG4Application.class.getClassLoader().getResourceAsStream("serviceAccountKey.json");
		if (serviceAccountKey == null) {
			throw new IOException("Service account key file not found");
		}
		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccountKey))
				.build();
		FirebaseApp.initializeApp(options);
	}
}

