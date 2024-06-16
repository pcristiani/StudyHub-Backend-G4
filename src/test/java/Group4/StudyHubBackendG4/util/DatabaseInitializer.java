package Group4.StudyHubBackendG4.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class DatabaseInitializer {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void executeSqlScript(String scriptPath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(scriptPath)));
            jdbcTemplate.execute(content);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read SQL script from path: " + scriptPath, e);
        }
    }
}
