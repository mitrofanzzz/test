package a.test.config;

import a.test.services.MessageAggregator;
import a.test.services.MyCorrelationStrategy;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.dsl.Transformers;
import org.springframework.messaging.support.GenericMessage;

import java.util.Arrays;
import java.util.List;

@Configuration
@NoArgsConstructor
@Slf4j
public class PollerConfiguration {
    String[] array = {"Item1", "Item2", "Item3", "Item4"};
    private MessageAggregator messageAggregator;
    private MyCorrelationStrategy myCorrelationStrategy;

    @Autowired
    public void setMyCorrelationStrategy(MyCorrelationStrategy myCorrelationStrategy) {
        this.myCorrelationStrategy = myCorrelationStrategy;
    }

    @Autowired
    public void setMessageAggregator(MessageAggregator messageAggregator) {
        this.messageAggregator = messageAggregator;
    }


    @Bean
    public IntegrationFlow poolingFlow() {
        return IntegrationFlows.from(
                stupidSource(),
                p -> p.poller(
                        Pollers.fixedDelay(1000L)
                                .maxMessagesPerPoll(1L)))
                .transform(Transformers.converter(source -> Arrays.asList((String[]) source)))
                .log()
                .channel("channel")
                .get();
    }

    @Bean
    public MessageSource<String[]> stupidSource() {
        return () -> new GenericMessage<String[]>(array);
    }
}
