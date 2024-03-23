package Group4.StudyHubBackendG4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
public class StudyHubBackendG4Application {

	public static void main(String[] args) {
		SpringApplication.run(StudyHubBackendG4Application.class, args);
	}

}
