package a.test.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Component
public class MasterService {
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

    @Transformer(inputChannel = "splittedChannel", outputChannel = "processedChannel")
    public Map<String, List<String>> handleMessage(GenericMessage<Map<String, List<String>>> message) {
        if (message.getPayload().containsKey("even")) {

            List<String> even = slave1.processStrings(message.getPayload().get("even"));
            return Map.of("even", even);

        }

        if (message.getPayload().containsKey("odd")) {

            List<String> odd = slave2.processStrings(message.getPayload().get("odd"));
            return Map.of("odd", odd);
        }

        return Map.of();
    }
}
