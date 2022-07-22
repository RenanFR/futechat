package br.com.futechat.discord;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import br.com.futechat.commons.EnableFutechatCommons;
import br.com.futechat.discord.bot.commands.Command;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import reactor.core.publisher.Mono;

@SpringBootApplication
@EnableFutechatCommons
public class FutechatApplication {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private GatewayDiscordClient gatewayDiscordClient;

    @Autowired
    private Map<String, Command> commandHandlersMap;

    public static void main(String[] args) {
        SpringApplication.run(FutechatApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doStartupActions() {

        gatewayDiscordClient.on(ReadyEvent.class, readyEvent -> Mono.fromRunnable(() -> {
            LOGGER.info("O FUTECHAT ESTA CONECTADO COM O DISCORD");
        })).subscribe();

        gatewayDiscordClient.on(ChatInputInteractionEvent.class, event -> {
            Command commandHandler = commandHandlersMap.get(event.getCommandName());
            if (Optional.ofNullable(commandHandler).isPresent()) {
                return event.deferReply().withEphemeral(true)
                        .then(event.createFollowup(commandHandler.execute(event.getOptions())));
            }
            return event.deferReply().withEphemeral(true).then(event.createFollowup("Comando não existe no Futechat"));
        }).subscribe();
    }

}
