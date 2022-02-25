package a.test.services;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Slave1 {
    public List<String> processStrings(List<String> stringList) {
        return stringList.stream().map(s -> new StringBuilder(s).reverse().toString()).collect(Collectors.toList());
    }
}
