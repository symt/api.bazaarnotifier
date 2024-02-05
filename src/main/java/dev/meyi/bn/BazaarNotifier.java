package dev.meyi.bn;

import dev.meyi.bn.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

@SpringBootApplication
public class BazaarNotifier {
	public static BazaarNotifier bn;
	public final AnnotationConfigWebApplicationContext ctx;
	public BazaarNotifier() {
		ctx = new AnnotationConfigWebApplicationContext();
		ctx.register(Config.class);
		ctx.refresh();

		BazaarNotifier.bn = this;
	}

	public static void main(String[] args) {


		SpringApplication.run(BazaarNotifier.class, args);
	}

}
