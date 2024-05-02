package dz.lab22.lab22;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class Lab22Application {

	public static void main(String[] args) {
		SpringApplication.run(Lab22Application.class, args);
	}

	@GetMapping("/")
	public String index(@RequestParam(value="nom", defaultValue = "Visiteur") String nom){
		return String.format("Bonjour %s!", nom);
	}
}
