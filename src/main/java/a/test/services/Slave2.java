package a.test.services;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class Slave2 extends Slave {
    @Override
    public List<String> processStrings(List<String> stringList) {
        try {
            Thread.sleep((long)(10000 - (Math.random() * 5000)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return stringList.stream().map(String::toUpperCase).collect(Collectors.toList());
    }
}
