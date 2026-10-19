package lb5;
import java.util.Objects;

public class Task4 {


    private static void demonstrateUnboxing() {
        Integer value = null;

        try {
            int number = value;
            System.out.println(number);
        } catch (NullPointerException e) {
            System.out.println("NPE: нельзя преобразовать null в int");
        }
    }


    private static int increment(Integer value) {
        Objects.requireNonNull(value, "value не должен быть null");
        return value.intValue() + 1;
    }

    private static boolean sameValue(Integer first, Integer second) {
        return Objects.equals(first, second);
    }

    public static void main(String[] args) {
        System.out.println("1. Unboxing null:");
        demonstrateUnboxing();

        System.out.println("\n2. Сравнение Integer через == и equals:");

        Integer small1 = Integer.valueOf(100);
        Integer small2 = Integer.valueOf(100);

        Integer large1 = Integer.valueOf(1000);
        Integer large2 = Integer.valueOf(1000);

        System.out.println("100: == " + (small1 == small2));
        System.out.println("100: equals() " + small1.equals(small2));

        System.out.println("1000: == " + (large1 == large2));
        System.out.println("1000: equals() " + large1.equals(large2));

        System.out.println("\n3. Правильное сравнение значений:");
        System.out.println(sameValue(small1, small2));
        System.out.println(sameValue(large1, large2));
        System.out.println("Сравнение null: " + sameValue(null, null));
        System.out.println("null и число: " + sameValue(null, 100));

        System.out.println("\n4. Явный контракт:");
        System.out.println("10 + 1 = " + increment(10));

        try {
            increment(null);
        } catch (NullPointerException e) {
            System.out.println("Нарушен контракт: " + e.getMessage());
        }
    }
}