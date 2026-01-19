package ke.fintech.ledger_api;


import ke.fintech.ledger_api.utilities.AppLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableJpaRepositories(basePackages = "ke.vbs.dairy.dairy_api.repository")
public class App {


	public static void main(String[] args) {

	var context =	SpringApplication.run(App.class, args);
		AppLogger ticketLogger = context.getBean(AppLogger.class);
		ticketLogger.debug("Application has been Launched : msg Logger");
		System.out.println("Application has been Launched : msg from sout");

	}

}
