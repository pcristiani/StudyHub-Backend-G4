package Group4.StudyHubBackendG4;

import Group4.StudyHubBackendG4.persistence.User;
import Group4.StudyHubBackendG4.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class StudyHubBackendG4Application {

	@Autowired
	UserRepo userRepo;

	@GetMapping("/getUsers")
	public String getAllUsers(){
		return userRepo.findAll().stream().toList().toString();
	}

	@GetMapping("/insertUsers")
	public String insertUser1(){
		User u = new User();
		u.setId(1);
		u.setName("Pablito");
		return userRepo.save(u).toString();
	}

	public static void main(String[] args) {
		SpringApplication.run(StudyHubBackendG4Application.class, args);
	}

}
