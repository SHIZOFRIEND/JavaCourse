package lb5;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Task6 {
    private static final BigDecimal CENT = new BigDecimal("0.01");

    private static List<BigDecimal> distributeDiscount(List<BigDecimal> prices, BigDecimal discount, RoundingMode roundingMode) {
        Objects.requireNonNull(prices, "Список цен не должен быть null");
        Objects.requireNonNull(discount, "Скидка не должна быть null");
        Objects.requireNonNull(roundingMode, "Режим округления не должен быть null");

        List<BigDecimal> normalizedPrices = new ArrayList<BigDecimal>();
        BigDecimal total = BigDecimal.ZERO;

        for (BigDecimal price : prices) {
            Objects.requireNonNull(price, "Цена не должна быть null");

            price = price.setScale(2, roundingMode);

            if (price.signum() < 0) {
                throw new IllegalArgumentException("Цена не может быть отрицательной");
            }

            normalizedPrices.add(price);
            total = total.add(price);
        }

        discount = discount.setScale(2, roundingMode);

        if (discount.signum() < 0 || discount.compareTo(total) > 0) {
            throw new IllegalArgumentException(
                    "Скидка должна быть от 0 до общей стоимости");
        }

        List<BigDecimal> shares = new ArrayList<BigDecimal>();

        if (total.signum() == 0) {
            return shares;
        }

        BigDecimal allocated = BigDecimal.ZERO;

        for (BigDecimal price : normalizedPrices) {
            BigDecimal share = discount.multiply(price)
                    .divide(total, 2, roundingMode);

            shares.add(share);
            allocated = allocated.add(share);
        }

        long remainingCents = discount.subtract(allocated)
                .movePointRight(2)
                .longValueExact();

        while (remainingCents != 0) {
            boolean changed = false;

            for (int i = 0; i < shares.size(); i++) {
                BigDecimal share = shares.get(i);
                BigDecimal price = normalizedPrices.get(i);

                if (remainingCents > 0
                        && share.add(CENT).compareTo(price) <= 0) {

                    shares.set(i, share.add(CENT));
                    remainingCents--;
                    changed = true;

                } else if (remainingCents < 0
                        && share.compareTo(CENT) >= 0) {

                    shares.set(i, share.subtract(CENT));
                    remainingCents++;
                    changed = true;
                }

                if (remainingCents == 0) {
                    break;
                }
            }

            if (!changed) {
                throw new IllegalStateException("Не удалось распределить остаток скидки");
            }
        }

        return shares;
    }

    public static void main(String[] args) {
        List<BigDecimal> prices = Arrays.asList(new BigDecimal("19.99"), new BigDecimal("34.99"), new BigDecimal("45.02")
        );

        BigDecimal discount = new BigDecimal("12.34");
        RoundingMode roundingMode = RoundingMode.HALF_UP;
        List<BigDecimal> shares = distributeDiscount(prices, discount, roundingMode);
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal totalDiscount = BigDecimal.ZERO;
        BigDecimal finalTotal = BigDecimal.ZERO;
        System.out.println("Режим округления: " + roundingMode);
        System.out.printf("%-10s %-12s %-12s%n", "Позиция", "Цена", "Скидка");
        for (int i = 0; i < prices.size(); i++) {
            BigDecimal price = prices.get(i);
            BigDecimal share = shares.get(i);
            BigDecimal finalPrice = price.subtract(share);
            System.out.printf("%-10d %-12s %-12s%n", i + 1, price.toPlainString(), share.toPlainString());
            total = total.add(price);
            totalDiscount = totalDiscount.add(share);
            finalTotal = finalTotal.add(finalPrice);
            System.out.println("После скидки: " + finalPrice.toPlainString());
        }
        BigDecimal expectedTotal = total.subtract(discount);
        System.out.println("Исходная сумма:       " + total.toPlainString());
        System.out.println("Ожидаемая скидка:     " + discount.toPlainString());
        System.out.println("Распределённая скидка:" + totalDiscount.toPlainString());
        System.out.println("Ожидаемый итог:       " + expectedTotal.toPlainString());
        System.out.println("Фактический итог:     " + finalTotal.toPlainString());
        System.out.println("Суммы совпадают:      " + expectedTotal.equals(finalTotal));
    }
}