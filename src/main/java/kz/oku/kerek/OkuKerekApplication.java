package kz.oku.kerek;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class OkuKerekApplication {

	public static void main(String[] args) {
		SpringApplication.run(OkuKerekApplication.class, args);
	}

}
