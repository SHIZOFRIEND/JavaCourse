package lb5;
public class Task2 {
    private static final int LINE_COUNT = 10_000;

    private static String buildWithPlus(int count) {
        String report = "";
        for (int i = 1; i <= count; i++) {
            report += "Строка " + i + "\n";
        }
        return report;
    }

    private static String buildWithStringBuilder(int count) {
        StringBuilder builder = new StringBuilder(count * 16);
        for (int i = 1; i <= count; i++) {
            builder.append("Строка ").append(i).append('\n');
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        buildWithPlus(300);
        buildWithStringBuilder(300);

        long plusStart = System.nanoTime();
        String plusReport = buildWithPlus(LINE_COUNT);
        long plusTime = System.nanoTime() - plusStart;

        long builderStart = System.nanoTime();
        String builderReport = buildWithStringBuilder(LINE_COUNT);
        long builderTime = System.nanoTime() - builderStart;

        System.out.println("Количество строк: " + LINE_COUNT);
        System.out.println("Отчёты совпадают: " + plusReport.equals(builderReport));
        System.out.println("Длина отчёта: " + plusReport.length() + " символов");
        System.out.printf("Время через +: %.3f мс%n", plusTime / 1_000_000.0);
        System.out.printf("Время через StringBuilder: %.3f мс%n",
                builderTime / 1_000_000.0);
        System.out.println("Первая строка: "
                + plusReport.substring(0, plusReport.indexOf('\n')));
        int lastLineStart = plusReport.lastIndexOf('\n', plusReport.length() - 2) + 1;
        System.out.println("Последняя строка: "
                + plusReport.substring(lastLineStart, plusReport.length() - 1));


    }
}