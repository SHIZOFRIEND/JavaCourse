import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Objects;

public class Task14 {

    static final class Money {
        private static final BigDecimal HUNDRED = new BigDecimal("100");

        private final BigDecimal amount;
        private final Currency currency;
        private final int scale;

        public Money(BigDecimal amount, Currency currency, RoundingMode roundingMode) {
            Objects.requireNonNull(amount, "Сумма не должна быть null");
            this.currency = Objects.requireNonNull(currency, "Валюта не должна быть null");
            Objects.requireNonNull(roundingMode, "Режим округления не должен быть null");
            this.scale = currency.getDefaultFractionDigits();
            if (scale < 0) {
                throw new IllegalArgumentException("Для валюты не определено количество дробных знаков");
            }

            this.amount = amount.setScale(scale, roundingMode);
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public Currency getCurrency() {
            return currency;
        }

        public Money add(Money other) {
            checkCurrency(other);
            return new Money(amount.add(other.amount), currency, RoundingMode.UNNECESSARY
            );
        }

        public Money subtract(Money other) {
            checkCurrency(other);
            return new Money(amount.subtract(other.amount),
                    currency,
                    RoundingMode.UNNECESSARY
            );
        }

        // Рассчитывает процент от суммы. Процент должен быть от 0 до 100.
        public Money percentage(BigDecimal percent,
                                RoundingMode roundingMode) {
            Objects.requireNonNull(percent, "Процент не должен быть null");
            Objects.requireNonNull(
                    roundingMode, "Режим округления не должен быть null");

            if (percent.signum() < 0
                    || percent.compareTo(HUNDRED) > 0) {
                throw new IllegalArgumentException(
                        "Процент должен находиться в диапазоне от 0 до 100");
            }

            BigDecimal result = amount.multiply(percent)
                    .divide(HUNDRED, scale, roundingMode);

            return new Money(
                    result, currency, RoundingMode.UNNECESSARY);
        }

        // Распределяет неотрицательную сумму пропорционально весам.
        // Оставшиеся минимальные денежные единицы распределяются
        // по порядку элементов, имеющих положительный вес.
        public List<Money> allocate(List<BigDecimal> weights,
                                    RoundingMode roundingMode) {
            Objects.requireNonNull(weights, "Веса не должны быть null");
            Objects.requireNonNull(
                    roundingMode, "Режим округления не должен быть null");

            if (weights.isEmpty()) {
                throw new IllegalArgumentException(
                        "Список весов не должен быть пустым");
            }

            if (amount.signum() < 0) {
                throw new IllegalArgumentException(
                        "Нельзя распределять отрицательную сумму");
            }

            BigDecimal totalWeight = BigDecimal.ZERO;
            List<Integer> recipients = new ArrayList<Integer>();

            for (int i = 0; i < weights.size(); i++) {
                BigDecimal weight = Objects.requireNonNull(
                        weights.get(i), "Вес не должен быть null");

                if (weight.signum() < 0) {
                    throw new IllegalArgumentException(
                            "Вес не может быть отрицательным");
                }

                totalWeight = totalWeight.add(weight);

                if (weight.signum() > 0) {
                    recipients.add(i);
                }
            }

            if (totalWeight.signum() == 0) {
                throw new IllegalArgumentException(
                        "Сумма весов должна быть положительной");
            }

            BigInteger totalMinorUnits =
                    amount.movePointRight(scale).toBigIntegerExact();

            List<BigInteger> shares = new ArrayList<BigInteger>();
            BigInteger allocated = BigInteger.ZERO;

// Рассчитываем предварительные доли в минимальных денежных единицах.
            for (BigDecimal weight : weights) {
                BigDecimal quota = new BigDecimal(totalMinorUnits)
                        .multiply(weight)
                        .divide(totalWeight, 0, roundingMode);

                BigInteger share = quota.toBigIntegerExact();
                shares.add(share);
                allocated = allocated.add(share);
            }

            // Рассчитываем предварительные доли в минимальных единицах.
            shares.clear();

            for (BigDecimal weight : weights) {
                BigDecimal quota = new BigDecimal(totalMinorUnits)
                        .multiply(weight)
                        .divide(totalWeight, 0, roundingMode);

                BigInteger share = quota.toBigIntegerExact();
                shares.add(share);
                allocated = allocated.add(share);
            }

            BigInteger remainder = totalMinorUnits.subtract(allocated);
            int cursor = 0;

            while (remainder.signum() != 0) {
                int index = recipients.get(cursor);
                cursor = (cursor + 1) % recipients.size();

                if (remainder.signum() > 0) {
                    shares.set(index, shares.get(index).add(BigInteger.ONE));
                    remainder = remainder.subtract(BigInteger.ONE);
                } else if (shares.get(index).signum() > 0) {
                    shares.set(index, shares.get(index).subtract(BigInteger.ONE));
                    remainder = remainder.add(BigInteger.ONE);
                }
            }

            List<Money> result = new ArrayList<Money>();

            for (BigInteger share : shares) {
                BigDecimal shareAmount = new BigDecimal(share, scale);

                result.add(new Money(
                        shareAmount, currency, RoundingMode.UNNECESSARY));
            }

            return Collections.unmodifiableList(result);
        }

        private void checkCurrency(Money other) {
            Objects.requireNonNull(other, "Money не должен быть null");

            if (!currency.equals(other.currency)) {
                throw new IllegalArgumentException(
                        "Нельзя выполнять операцию с разными валютами: "
                                + currency.getCurrencyCode() + " и "
                                + other.currency.getCurrencyCode());
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Money)) {
                return false;
            }

            Money other = (Money) obj;

            return currency.equals(other.currency)
                    && amount.compareTo(other.amount) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(amount.stripTrailingZeros(), currency);
        }

        @Override
        public String toString() {
            return amount.toPlainString() + " "
                    + currency.getCurrencyCode();
        }
    }

    public static void main(String[] args) {
        Currency usd = Currency.getInstance("USD");
        Currency eur = Currency.getInstance("EUR");
        RoundingMode rounding = RoundingMode.HALF_UP;

        Money price1 = new Money(
                new BigDecimal("99.99"), usd, rounding);
        Money price2 = new Money(
                new BigDecimal("25.50"), usd, rounding);

        System.out.println("Исходные цены:");
        System.out.println("Курс Java: " + price1);
        System.out.println("Курс Python: " + price2);

        System.out.println("\n1. Сложение:");
        System.out.println(price1.add(price2));

        System.out.println("\n2. Вычитание:");
        System.out.println(price1.subtract(price2));

        System.out.println("\n3. Расчёт 10% от цены Java:");
        Money discount = price1.percentage(
                new BigDecimal("10"), rounding);
        System.out.println("Скидка: " + discount);
        System.out.println("После скидки: " + price1.subtract(discount));

        System.out.println("\n4. Распределение 10 долларов:");
        Money total = new Money(
                new BigDecimal("10.00"), usd, rounding);

        List<Money> parts = total.allocate(
                Arrays.asList(
                        new BigDecimal("1"),
                        new BigDecimal("1"),
                        new BigDecimal("1")
                ),
                rounding
        );

        BigDecimal checkTotal = BigDecimal.ZERO;

        for (int i = 0; i < parts.size(); i++) {
            System.out.println("Участник " + (i + 1) + ": " + parts.get(i));
            checkTotal = checkTotal.add(parts.get(i).getAmount());
        }

        System.out.println("Сумма долей: " + checkTotal);
        System.out.println("Итог совпадает: "
                + total.getAmount().equals(checkTotal));

        System.out.println("\n5. Содержательное равенство:");
        Money a = new Money(new BigDecimal("10.0"), usd, rounding);
        Money b = new Money(new BigDecimal("10.00"), usd, rounding);
        Money c = new Money(new BigDecimal("10.00"), eur, rounding);

        System.out.println("10.0 USD == 10.00 USD по equals: "
                + a.equals(b));
        System.out.println("10.00 USD == 10.00 EUR по equals: "
                + a.equals(c));

        System.out.println("\n6. Запрет смешения валют:");
        try {
            System.out.println(price1.add(
                    new Money(new BigDecimal("10.00"), eur, rounding)));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}