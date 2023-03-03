package project.pet.PostFlow.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public int averageWaitingTimeInMinutes() {
        return 5;
    }

}
