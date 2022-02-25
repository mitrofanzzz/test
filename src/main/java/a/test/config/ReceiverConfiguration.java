package a.test.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;

@Slf4j
@Configuration
public class ReceiverConfiguration {

    public static final String OUTPUT_CHANNEL = "outputChannel";

    @Bean
    @ServiceActivator(inputChannel = OUTPUT_CHANNEL)
    public MessageHandler messageHandler(){
        return message -> log.info(message.getPayload().toString());
    }

}
