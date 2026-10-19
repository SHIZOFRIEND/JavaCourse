package lb5;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;

public class Task8 {

    private static LocalDate findLastStudyDay(LocalDate date) {
        while (date.getDayOfWeek() == DayOfWeek.SATURDAY
                || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            date = date.minusDays(1);
        }

        return date;
    }

    public static void main(String[] args) {
        LocalDate courseStart = LocalDate.of(2024, 2, 1);
        int moduleCount = 3;
        int moduleDurationMonths = 1;

        System.out.println("Начало обучения: " + courseStart);
        System.out.println("\nНачало модулей:");

        for (int i = 0; i < moduleCount; i++) {
            LocalDate moduleStart = courseStart.plusMonths(
                    (long) i * moduleDurationMonths);

            System.out.println("Модуль " + (i + 1) + ": " + moduleStart);
        }

        LocalDate courseEnd = courseStart
                .plusMonths((long) moduleCount * moduleDurationMonths)
                .minusDays(1);

        LocalDate lastStudyDay = findLastStudyDay(courseEnd);

        System.out.println("\nПоследний календарный день курса: " + courseEnd);
        System.out.println("Последний учебный день: " + lastStudyDay);

        System.out.println("\nПроверка високосного года:");

        int year = courseStart.getYear();

        System.out.println("Год начала курса: " + year);
        System.out.println("Год високосный: " + Year.isLeap(year));
        System.out.println("В феврале " + year + " года "
                + (Year.isLeap(year) ? 29 : 28) + " дней");

        LocalDate leapDay = LocalDate.of(2024, 2, 29);
        System.out.println("29 февраля существует: "
                + (Year.isLeap(leapDay.getYear())
                && leapDay.getMonthValue() == 2
                && leapDay.getDayOfMonth() == 29));

        System.out.println("\nПроверка неизменяемости LocalDate:");

        LocalDate originalDate = courseStart;
        LocalDate changedDate = courseStart.plusDays(10);

        System.out.println("Исходная дата: " + originalDate);
        System.out.println("После plusDays(10): " + changedDate);
        System.out.println("Исходный объект не изменился: "
                + originalDate.equals(courseStart));
    }
}