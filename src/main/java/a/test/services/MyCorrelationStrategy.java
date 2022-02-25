package a.test.services;

import org.springframework.integration.aggregator.CorrelationStrategy;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class MyCorrelationStrategy implements CorrelationStrategy {

    public static final String CORRELATION_ID = "CORRELATION_ID";

    @Override
    public Object getCorrelationKey(Message<?> message) {
        return message.getHeaders().get(CORRELATION_ID);
    }
}
