package a.test.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.aggregator.AbstractAggregatingMessageGroupProcessor;
import org.springframework.integration.annotation.Aggregator;
import org.springframework.integration.store.MessageGroup;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class MessageAggregator {
    @Aggregator(inputChannel = "processedChannel", outputChannel = "outputChannel")
    public List<String> aggregateMessage(Collection<Message<?>> messageGroup) {
        log.info("message-group - {}", messageGroup);

        List<String> odd = new LinkedList<>();
        List<String> even = new LinkedList<>();

        Iterator<Message<?>> iterator = messageGroup.iterator();
        while (iterator.hasNext()) {
            Message<?> next = iterator.next();
            Map<String,List<String>> payload = (Map<String, List<String>>) next.getPayload();
            if (payload.containsKey("odd")) odd.addAll(payload.get("odd"));
            if (payload.containsKey("even")) even.addAll(payload.get("even"));
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

//    @Aggregator(inputChannel = "processedChannel", outputChannel = "outputChannel")
//    @Override
//    protected Object aggregatePayloads(MessageGroup group, Map<String, Object> defaultHeaders) {
//        return aggregateMessage(group);
//    }


}
