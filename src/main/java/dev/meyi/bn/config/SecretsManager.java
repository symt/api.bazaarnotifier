package dev.meyi.bn.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;


/**
 * Secrets Manager handles the various environment variables used by the application
 */
@Configuration
@PropertySource("file:./.env")
public class SecretsManager {

  private static SecretsManager secretsManager;
  private final Environment env;
  @Value("${spring.profiles.active:Unknown}")
  private String activeProfile;

  public SecretsManager(Environment env) {
    this.env = env;
    secretsManager = this;
  }

  public static SecretsManager getInstance() {
    return secretsManager;
  }

  @Bean("secret.key.api")
  public String getApiKey() {
    return env.getProperty("secret.key.api");
  }

  @Bean("secret.key.bot")
  public String getBotKey() {
    return env.getProperty("secret.key.bot");
  }

  public String getActiveProfile() {
    return activeProfile;
  }
}
