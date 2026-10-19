package lb5;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Task5 {

    public static void main(String[] args) {
        System.out.println("1. Сравнение способов создания BigDecimal:");

        BigDecimal first = new BigDecimal(0.1);
        BigDecimal second = BigDecimal.valueOf(0.1);
        BigDecimal third = new BigDecimal("0.1");

        System.out.println("new BigDecimal(0.1):       " + first);
        System.out.println("BigDecimal.valueOf(0.1):   " + second);
        System.out.println("new BigDecimal(\"0.1\"):    " + third);

        System.out.println("\n2. Сравнение значений:");
        System.out.println("first.compareTo(second): " + first.compareTo(second));
        System.out.println("second.compareTo(third): " + second.compareTo(third));

        System.out.println("\n3. Расчёт стоимости трёх курсов:");

        BigDecimal javaCourse = new BigDecimal("1299.90");
        BigDecimal pythonCourse = new BigDecimal("899.50");
        BigDecimal csharpCourse = new BigDecimal("1199.99");

        System.out.println("Курс Java:   " + javaCourse + " руб.");
        System.out.println("Курс Python: " + pythonCourse + " руб.");
        System.out.println("Курс C#:     " + csharpCourse + " руб.");

        BigDecimal total = javaCourse
                .add(pythonCourse)
                .add(csharpCourse);

        System.out.println("Общая стоимость: " + total.setScale(2, RoundingMode.HALF_UP) + " руб.");

        System.out.println("\n4. Расчёт скидки 10%:");

        BigDecimal discountRate = new BigDecimal("0.10");
        BigDecimal discount = total.multiply(discountRate);
        BigDecimal discountedTotal = total.subtract(discount)
                .setScale(2, RoundingMode.HALF_UP);

        System.out.println("Размер скидки: "
                + discount.setScale(2, RoundingMode.HALF_UP) + " руб.");
        System.out.println("Стоимость со скидкой: " + discountedTotal + " руб.");
    }
}