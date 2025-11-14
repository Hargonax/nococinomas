package es.bifacia.nococinomas;

import es.bifacia.nococinomas.service.MainService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NoCocinoMasUtilityApplication {

	@Autowired
	private MainService mainService;

	public static void main(String[] args) {
		SpringApplication.run(NoCocinoMasUtilityApplication.class, args);
	}

	@PostConstruct
	public void start() throws Exception {
		mainService.runApplication();
	}

}
