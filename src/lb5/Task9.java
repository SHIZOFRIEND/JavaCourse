package lb5;

import java.time.Duration;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Task9 {

    public static void main(String[] args) {
        ZoneId zone = ZoneId.of("Europe/Stockholm");

        ZonedDateTime start = ZonedDateTime.of(2026, 3, 28, 12, 0, 0, 0, zone);

        ZonedDateTime afterPeriod = start.plus(Period.ofDays(1));

        ZonedDateTime afterDuration = start.plus(Duration.ofHours(24));

        System.out.println("Начальная дата: " + start);
        System.out.println();

        System.out.println("После Period.ofDays(1):");
        System.out.println(afterPeriod);

        System.out.println("После Duration.ofHours(24):");
        System.out.println(afterDuration);

        System.out.println();

        Duration periodElapsed = Duration.between(start, afterPeriod);
        Duration durationElapsed = Duration.between(start, afterDuration);

        System.out.println("Реально прошло после Period: " + periodElapsed.toHours() + " часов");

        System.out.println("Реально прошло после Duration: " + durationElapsed.toHours() + " часов");

        System.out.println();
        System.out.println("Разница между результатами: " + Duration.between(afterPeriod, afterDuration).toHours() + " час");
    }
}