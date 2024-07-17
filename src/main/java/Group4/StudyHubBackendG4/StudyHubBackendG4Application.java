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

import java.io.*;
import java.nio.charset.StandardCharsets;


@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
public class StudyHubBackendG4Application {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(StudyHubBackendG4Application.class, args);
		initializeFirebase();
		abrirNavegadorConRuntime("https://studyhub-backend-production.up.railway.app/swagger-ui/index.html");
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
		String serviceAccountKeyContent = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
		if (serviceAccountKeyContent == null) {
			throw new IOException("Environment variable GOOGLE_APPLICATION_CREDENTIALS not found");
		}

		try (InputStream serviceAccountKey = new ByteArrayInputStream(serviceAccountKeyContent.getBytes(StandardCharsets.UTF_8))) {
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccountKey))
					.build();
			FirebaseApp.initializeApp(options);
		}
	}
}

