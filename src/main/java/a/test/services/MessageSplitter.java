package a.test.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.Splitter;
import org.springframework.integration.splitter.AbstractMessageSplitter;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class MessageSplitter extends AbstractMessageSplitter {

    public static final String EVEN = "even";
    public static final String ODD = "odd";
    public static final String TRUE = "true";
    public static final String CHANNEL = "channel";
    public static final String SPLITTED_CHANNEL = "splittedChannel";

    protected Map<String, List<String>> splitMessageAndGetEven(Message<List<String>> message) {
        List<String> listOfEven = getListOfEven(message);
        return Map.of(EVEN, listOfEven);
    }

    protected Map<String, List<String>> splitMessageAndGetOdd(Message<List<String>> message) {
        List<String> copyOfList = new ArrayList<>(message.getPayload());
        List<String> listOfEven = getListOfEven(message);
        copyOfList.removeAll(listOfEven);
        return Map.of(ODD, copyOfList);
    }

    private List<String> getListOfEven(Message<List<String>> message) {
        return message.getPayload().stream().flatMap(everyNth(2)).collect(Collectors.toList());
    }

    private <T> Function<T, Stream<T>> everyNth(int position) {
        return new Function<T, Stream<T>>() {
            int i = 0;

            @Override
            public Stream<T> apply(T t) {
                if (i++ % position == 0) {
                    return Stream.of(t);
                }
                return Stream.empty();
            }
        };
    }

    @Splitter(inputChannel = CHANNEL, outputChannel = SPLITTED_CHANNEL, applySequence = TRUE)
    @Override
    protected Object splitMessage(Message<?> message) {
        List<Message<?>> list = new LinkedList<>();
        list.add(new GenericMessage<>(splitMessageAndGetEven((Message<List<String>>) message)));
        list.add(new GenericMessage<>(splitMessageAndGetOdd((Message<List<String>>) message)));
        return list;
    }
}
