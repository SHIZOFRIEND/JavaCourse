package lb5;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.random.RandomGenerator;

public class Task7 {
    private static final int SAMPLE_SIZE = 20;
    private static final int UPPER_BOUND_CENTS = 10_000;
    private static final long TEST_SEED = 12345L;

    private static RandomGenerator productionRandom() {
        return RandomGenerator.getDefault();
    }

    private static RandomGenerator testRandom(long seed) {
        return new Random(seed);
    }

    private static double[] generateSample(RandomGenerator random, int size) {
        Objects.requireNonNull(random, "Источник случайных чисел не должен быть null");

        if (size <= 0) {
            throw new IllegalArgumentException("Размер выборки должен быть положительным");
        }
        double[] sample = new double[size];
        for (int i = 0; i < size; i++) {
            sample[i] = random.nextInt(UPPER_BOUND_CENTS) / 100.0;
        }
        return sample;
    }

    private static void printStatistics(String title, double[] sample) {
        double min = sample[0];
        double max = sample[0];
        double sum = 0.0;
        System.out.println("\n" + title);
        System.out.println("Выборка:");
        for (double value : sample) {
            System.out.printf(Locale.US, "%.2f ", value);
            min = Math.min(min, value);
            max = Math.max(max, value);
            sum += value;
        }

        double average = sum / sample.length;
        double roundedAverage = Math.round(average * 100.0) / 100.0;
        System.out.println("\n\nКоличество элементов: " + sample.length);
        System.out.printf(Locale.US, "Минимум: %.2f%n", min);
        System.out.printf(Locale.US, "Максимум: %.2f%n", max);
        System.out.printf(Locale.US, "Среднее без округления: %.6f%n", average);
        System.out.printf(Locale.US, "Среднее после округления: %.2f%n", roundedAverage);
        System.out.println("Допустимый диапазон: от 0.00 до 99.99");
    }

    public static void main(String[] args) {
        double[] productionSample = generateSample(productionRandom(), SAMPLE_SIZE);

        double[] testSample1 = generateSample(testRandom(TEST_SEED), SAMPLE_SIZE);

        double[] testSample2 = generateSample(testRandom(TEST_SEED), SAMPLE_SIZE);

        printStatistics("Production-выборка", productionSample);
        printStatistics("Тестовая выборка с seed = " + TEST_SEED, testSample1);

        System.out.println("\nВоспроизводимость теста: " + Arrays.equals(testSample1, testSample2));
    }
}