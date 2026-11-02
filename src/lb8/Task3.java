
package lb8;

import java.util.*;
import java.util.stream.Collectors;

class ValueObject {
    private String value;

    public ValueObject(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ValueObject{" + value + "}";
    }
}

public class Task3 {

    public static void main(String[] args) {

        List<Integer> durations = Arrays.asList(10, 20, 30, 40);

        int sum = durations.stream()
                .reduce(0, Integer::sum);

        System.out.println("Сумма длительностей: " + sum);

        int wrongSum = durations.stream()
                .reduce(10, Integer::sum);

        System.out.println("Сумма с неправильной identity: " + wrongSum);

        List<ValueObject> objects = Arrays.asList(
                new ValueObject("Java"),
                new ValueObject("Python"),
                new ValueObject("C++")
        );

        ValueObject result = objects.stream()
                .reduce(
                        new ValueObject(""),
                        (a, b) -> new ValueObject(
                                a.getValue() + b.getValue()
                        )
                );

        System.out.println("Объединение объектов: " + result);
    }
}

