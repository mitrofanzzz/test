package a.test.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Component
public class MasterService {
    public static final String ODD = "odd";
    public static final String EVEN = "even";
    public static final String SPLITTED_CHANNEL = "splittedChannel";
    public static final String PROCESSED_CHANNEL = "processedChannel";
    private Slave1 slave1;
    private Slave2 slave2;

    @Autowired
    public void setSlave1(Slave1 slave1) {
        this.slave1 = slave1;
    }

    @Autowired
    public void setSlave2(Slave2 slave2) {
        this.slave2 = slave2;
    }

    @Transformer(inputChannel = SPLITTED_CHANNEL, outputChannel = PROCESSED_CHANNEL)
    public Map<String, List<String>> handleMessage(GenericMessage<Map<String, List<String>>> message) {
        if (message.getPayload().containsKey(EVEN)) {
            return getStringListMap(message, slave1, EVEN);
        }

        if (message.getPayload().containsKey(ODD)) {
            return getStringListMap(message, slave2, ODD);
        }
        return Map.of();
    }

    private Map<String, List<String>> getStringListMap(GenericMessage<Map<String, List<String>>> message, Slave slave, String key) {
        List<String> odd = slave.processStrings(message.getPayload().get(key));
        return Map.of(key, odd);
    }
}
