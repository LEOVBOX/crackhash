package leovbox.crackhash;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CrackhashApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrackhashApplication.class, args);
		//System.out.println("WORKERS_URLS: " + System.getenv("WORKERS_URLS"));
	}
}
