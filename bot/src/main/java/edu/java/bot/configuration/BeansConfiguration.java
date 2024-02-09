package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import edu.java.bot.processor.DefaultUserMessagesProcessor;
import edu.java.bot.processor.UserMessagesProcessor;
import edu.java.bot.service.BotService;
import edu.java.bot.util.TextResolver;
import java.util.ArrayList;
import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class BeansConfiguration {

    @Bean
    public ResourceBundleMessageSource messageSourceResourceBundle() {
        ResourceBundleMessageSource messageSourceResourceBundle = new ResourceBundleMessageSource();
        messageSourceResourceBundle.setBasename("message");
        messageSourceResourceBundle.setDefaultLocale(Locale.of("ru"));
        return messageSourceResourceBundle;
    }

    @Bean
    public UserMessagesProcessor userMessagesProcessor(
        TextResolver textResolver,
        BotService botService
    ) {
        ArrayList<Command> commands = new ArrayList<>();
        commands.add(new HelpCommand(textResolver, commands));
        commands.add(new StartCommand(textResolver, botService));
        commands.add(new TrackCommand(textResolver, botService));
        commands.add(new UntrackCommand(textResolver, botService));
        commands.add(new ListCommand(textResolver, botService));
        return new DefaultUserMessagesProcessor(textResolver, commands);
    }

    @Bean
    public TelegramBot telegramBot(ApplicationConfig config) {
        return new TelegramBot(config.telegramToken());
    }
}
