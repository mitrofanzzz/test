package a.test.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.Splitter;
import org.springframework.integration.splitter.AbstractMessageSplitter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class MessageSplitter extends AbstractMessageSplitter {
    //    @Splitter(inputChannel = "channel", outputChannel = "splittedChannel",applySequence = "true")
    protected Map<String, List<String>> splitMessageAndGetEven(Message<List<String>> message) {
        List<String> listOfEven = getListOfEven(message);
        Map<String, List<String>> mapa = Map.of("even", listOfEven);
//        log.info("splitMessageAndGetEven started with message - {}, and return - {}", message, mapa);
        return mapa;
    }

    //    @Splitter(inputChannel = "channel", outputChannel = "splittedChannel",applySequence = "true")
    protected Map<String, List<String>> splitMessageAndGetOdd(Message<List<String>> message) {
        List<String> copyOfList = new ArrayList<>(message.getPayload());
        List<String> listOfEven = getListOfEven(message);
        copyOfList.removeAll(listOfEven);
        Map<String, List<String>> mapa = Map.of("odd", copyOfList);
//        log.info("splitMessageAndGetOdd started with message - {} and return - {}", message, mapa);
        return mapa;
    }

    private List<String> getListOfEven(Message<List<String>> message) {
        return message.getPayload().stream().flatMap(everyNth(2)).collect(Collectors.toList());
    }

    private <T> Function<T, Stream<T>> everyNth(int n) {
        return new Function<T, Stream<T>>() {
            int i = 0;

            @Override
            public Stream<T> apply(T t) {
                if (i++ % n == 0) {
                    return Stream.of(t);
                }
                return Stream.empty();
            }
        };
    }

    @Splitter(inputChannel = "channel", outputChannel = "splittedChannel", applySequence = "true")
    @Override
    protected Object splitMessage(Message<?> message) {
//        Collection<Message<?>> messages = new ArrayList<Message<?>>();
//
//        Message<?> msg = MessageBuilder.withPayload(splitMessageAndGetEven((Message<List<String>>) message))
//                .build();
//
//        messages.add(msg);
//
//        Message<?> msg1 = MessageBuilder.withPayload(splitMessageAndGetOdd((Message<List<String>>) message))
//                .build();
//
//        messages.add(msg1);

        List list = new LinkedList();
        list.add(new GenericMessage<>(splitMessageAndGetEven((Message<List<String>>) message)));
        list.add(new GenericMessage<>(splitMessageAndGetOdd((Message<List<String>>) message)));
        return list;
    }
}
