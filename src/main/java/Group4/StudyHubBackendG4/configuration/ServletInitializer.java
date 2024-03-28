package Group4.StudyHubBackendG4.configuration;

import Group4.StudyHubBackendG4.StudyHubBackendG4Application;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(StudyHubBackendG4Application.class);
    }

}
