package dev.meyi.bn.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;


@Configuration
@PropertySource("file:./.env")
public class Config {

  public final Environment env;

  public Config(Environment env) {
    this.env = env;
  }

  @Bean("secret.key.api")
  public String getApiKey() {
    return env.getProperty("secret.key.api");
  }

  @Bean("secret.key.bot")
  public String getBotKey() {
    return env.getProperty("secret.key.bot");
  }
}
