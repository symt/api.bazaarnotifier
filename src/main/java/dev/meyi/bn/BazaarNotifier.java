package dev.meyi.bn;

import dev.meyi.bn.config.SecretsManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * Entry point for the spring boot application
 */
@SpringBootApplication
public class BazaarNotifier {

  public static void main(String[] args) {
    // registers the secrets!
    AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
    ctx.register(SecretsManager.class);
    ctx.refresh();

    SpringApplication.run(BazaarNotifier.class, args);
  }

}
