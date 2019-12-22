package space.delusive.discord.racoonsuperbot.config;

import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import space.delusive.discord.racoonsuperbot.repository.YoutubeVideoRepository;
import space.delusive.discord.racoonsuperbot.repository.impl.YoutubeVideoRepositoryImpl;

@Configuration
@ComponentScan
@EnableScheduling
public class ApplicationConfiguration {

    @Bean
    YoutubeVideoRepository getYoutubeVideoRepository(
            @Value("${youtube.api.token}") String apiToken,
            @Value("${youtube.api.search.url}") String url) {
        return new YoutubeVideoRepositoryImpl(url, apiToken);
    }

    @Bean
    @SneakyThrows
    JDA getJda(@Value("${discord.bot.token}") String botToken) {
        return new JDABuilder(botToken).build();
    }

}
