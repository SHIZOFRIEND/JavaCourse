package lb5;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.zone.ZoneOffsetTransition;
import java.time.zone.ZoneRules;
import java.util.List;

public class Task12 {

    private static final ZoneId ZONE = ZoneId.of("Europe/Stockholm");

    private static ZonedDateTime createLessonTime(LocalDateTime localTime) {
        ZoneRules rules = ZONE.getRules();
        List<ZoneOffset> offsets = rules.getValidOffsets(localTime);

        if (offsets.isEmpty()) {
            throw new DateTimeException("Время занятия не существует из-за перехода DST: " + localTime + " в зоне " + ZONE);
        }

        return ZonedDateTime.ofLocal(localTime, ZONE, offsets.get(0));
    }

    public static void main(String[] args) {
        ZoneRules rules = ZONE.getRules();

        System.out.println("Часовой пояс: " + ZONE);

        LocalDateTime gap = LocalDateTime.of(2026, 3, 29, 2, 30);

        System.out.println("\n1. Пропущенное локальное время:");
        System.out.println("Исходное время: " + gap);
        System.out.println("Допустимые смещения: " + rules.getValidOffsets(gap));

        ZoneOffsetTransition gapTransition = rules.getTransition(gap);
        System.out.println("Переход: " + gapTransition);
        System.out.println("До перехода: " + gapTransition.getDateTimeBefore());
        System.out.println("После перехода: " + gapTransition.getDateTimeAfter());

        System.out.println("gap.atZone(): " + gap.atZone(ZONE));

        try {
            System.out.println("Создание времени занятия: " + createLessonTime(gap));
        } catch (DateTimeException e) {
            System.out.println("Занятие отклонено: " + e.getMessage());
        }

        ZonedDateTime beforeGap = LocalDateTime.of(2026, 3, 29, 1, 30).atZone(ZONE);

        System.out.println("\n2. Добавление часа и дня:");
        System.out.println("Начало: " + beforeGap);
        System.out.println("+ 1 час: " + beforeGap.plusHours(1));
        System.out.println("+ 1 день: " + beforeGap.plusDays(1));
        System.out.println("+ 24 часа: " + beforeGap.plusHours(24));

        System.out.println("Реальное время после + 1 дня: " + Duration.between(beforeGap, beforeGap.plusDays(1)).toHours() + " часов");

        LocalDateTime overlap = LocalDateTime.of(2026, 10, 25, 2, 30);

        List<ZoneOffset> offsets = rules.getValidOffsets(overlap);

        System.out.println("\n3. Повторённое локальное время:");
        System.out.println("Исходное время: " + overlap);
        System.out.println("Допустимые смещения: " + offsets);

        if (offsets.size() > 1) {
            ZonedDateTime firstOccurrence = ZonedDateTime.ofLocal(overlap, ZONE, offsets.get(0));
            ZonedDateTime secondOccurrence = ZonedDateTime.ofLocal(overlap, ZONE, offsets.get(1));

            System.out.println("Первое вхождение: " + firstOccurrence);
            System.out.println("Второе вхождение: " + secondOccurrence);
            System.out.println("Первое вхождение + 1 час: " + firstOccurrence.plusHours(1));
            System.out.println("Первое вхождение + 1 день: " + firstOccurrence.plusDays(1));
            System.out.println("Первое вхождение + 24 часа: " + firstOccurrence.plusHours(24));
        }

        System.out.println("\nПолитика расписания:");
        System.out.println("1. Несуществующее время — отклонить создание занятия.");
        System.out.println("2. Повторённое время — выбрать первое вхождение.");
        System.out.println("3. Хранить выбранный момент с часовым поясом.");
    }
}