
package lb8;

import java.util.*;
import java.util.stream.*;

class Statistics {
    private final long count;
    private final long sum;
    private final int min;
    private final int max;

    public Statistics(long count, long sum, int min, int max) {
        this.count = count;
        this.sum = sum;
        this.min = min;
        this.max = max;
    }

    public long getCount() {
        return count;
    }

    public long getSum() {
        return sum;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "count=" + count +
                ", sum=" + sum +
                ", min=" + min +
                ", max=" + max +
                '}';
    }
}

public class Task6 {

    static class Accumulator {
        long count = 0;
        long sum = 0;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        void add(int value) {
            count++;
            sum += value;
            min = Math.min(min, value);
            max = Math.max(max, value);
        }

        Accumulator combine(Accumulator other) {
            count += other.count;
            sum += other.sum;
            min = Math.min(min, other.min);
            max = Math.max(max, other.max);
            return this;
        }

        Statistics finish() {
            return new Statistics(
                    count,
                    sum,
                    count == 0 ? 0 : min,
                    count == 0 ? 0 : max
            );
        }
    }

    public static Collector<Integer, Accumulator, Statistics>
    statisticsCollector() {

        return Collector.of(
                Accumulator::new,
                Accumulator::add,
                Accumulator::combine,
                Accumulator::finish
        );
    }

    public static void main(String[] args) {

        List<Integer> numbers = Arrays.asList(
                10, 20, 30, 40, 50, 60
        );

        Statistics result = numbers.stream()
                .collect(statisticsCollector());

        System.out.println("Собственный Collector:");
        System.out.println(result);

        Statistics parallelResult = numbers.parallelStream()
                .collect(statisticsCollector());

        System.out.println("\nПараллельный Collector:");
        System.out.println(parallelResult);

        IntSummaryStatistics standardResult = numbers.stream()
                .collect(Collectors.summarizingInt(Integer::intValue));

        System.out.println("\nСтандартный Collector:");
        System.out.println("count=" + standardResult.getCount());
        System.out.println("sum=" + standardResult.getSum());
        System.out.println("min=" + standardResult.getMin());
        System.out.println("max=" + standardResult.getMax());
    }
}
