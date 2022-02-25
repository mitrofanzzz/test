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
    public List<String> aggregateMessage(MessageGroup messageGroup) {
        List<String> listOfOdd = new LinkedList<>();
        List<String> listOfEven = new LinkedList<>();
log.info("message-group - {}", messageGroup);
        for (Message<?> message : messageGroup.getMessages()) {
            Map<String, List<String>> payload = (Map<String, List<String>>) message.getPayload();

            if (payload.containsKey("odd")) listOfOdd.addAll(payload.get("odd"));
            if (payload.containsKey("even")) listOfEven.addAll(payload.get("even"));

        }
        return merge(listOfOdd, listOfEven);
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
