package a.test.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.Aggregator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class MessageAggregator {

    public static final String PROCESSED_CHANNEL = "processedChannel";
    public static final String OUTPUT_CHANNEL = "outputChannel";
    public static final String ODD = "odd";
    public static final String EVEN = "even";

    @Aggregator(inputChannel = PROCESSED_CHANNEL, outputChannel = OUTPUT_CHANNEL)
    public List<String> aggregateMessage(Collection<Message<?>> messageGroup) {
        List<String> odd = new LinkedList<>();
        List<String> even = new LinkedList<>();

        Iterator<Message<?>> iterator = messageGroup.iterator();
        while (iterator.hasNext()) {
            Message<?> next = iterator.next();
            Map<String, List<String>> payload = (Map<String, List<String>>) next.getPayload();
            if (payload.containsKey(ODD)) odd.addAll(payload.get(ODD));
            if (payload.containsKey(EVEN)) even.addAll(payload.get(EVEN));
        }

        return merge(odd, even);
    }

    public static List<String> merge(List<String> odd, List<String> even) {
        ArrayList<String> merged = new ArrayList<>();
        for (String s : even) {
            merged.add(s);
            merged.add(odd.get(even.indexOf(s)));
        }
        return merged;
    }
}
