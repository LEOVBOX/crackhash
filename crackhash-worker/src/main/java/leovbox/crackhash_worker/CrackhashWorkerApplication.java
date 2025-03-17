package leovbox.crackhash_worker;

import leovbox.crackhash_worker.services.BrutForceService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootApplication
public class CrackhashWorkerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrackhashWorkerApplication.class, args);

		String alphabetString = "abcd";
		char[] alphabetArray = alphabetString.toCharArray();
		Character[] newArray = IntStream.range(0, alphabetArray.length)
				.mapToObj(i -> alphabetArray[i])
				.toArray(Character[]::new);

		//List<String> result = BrutForceService.<Character>BrutForce(newArray, 3, 4, 4, "e2fc714c4727ee9395f324cd2e7f331f");
		//System.out.println(result);

	}

}
