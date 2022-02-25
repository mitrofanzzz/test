package a.test.services;

import org.springframework.integration.aggregator.CorrelationStrategy;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class MyCorrelationStrategy implements CorrelationStrategy {
    @Override
    public Object getCorrelationKey(Message<?> message) {
        return message.getHeaders().get("CORRELATION_ID");
    }
}
