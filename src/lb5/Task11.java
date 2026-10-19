package lb5;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class Task11 {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.uuuu HH:mm:ss");

    public static void main(String[] args) {

        Instant webinarStart = Instant.parse("2026-10-19T15:00:00Z");

        List<ZoneId> zones = Arrays.asList(
                ZoneId.of("Europe/Stockholm"),
                ZoneId.of("America/New_York"),
                ZoneId.of("Asia/Tokyo")
        );

        System.out.println("Момент вебинара (Instant): " + webinarStart);
        System.out.println("\nОтображение вебинара в разных часовых поясах:");

        for (ZoneId zone : zones) {
            ZonedDateTime localTime = webinarStart.atZone(zone);

            System.out.println(zone + " -> " + localTime.format(FORMATTER) + " (UTC" + localTime.getOffset() + ")"
            );
        }

        LocalDateTime scheduledLocalTime = LocalDateTime.of(2026, 10, 19, 17, 0);
        ZoneId businessZone = ZoneId.of("Europe/Stockholm");
        Instant expectedInstant = scheduledLocalTime.atZone(businessZone).toInstant();

        System.out.println("\nПочему нельзя полагаться на default zone:");
        System.out.println("Заданное местное время: " + scheduledLocalTime);
        System.out.println("Бизнес-зона: " + businessZone);
        System.out.println("Ожидаемый Instant: " + expectedInstant);

        List<ZoneId> possibleServerZones = Arrays.asList(
                ZoneId.of("UTC"),
                ZoneId.of("Europe/Stockholm"),
                ZoneId.of("America/New_York")
        );

        for (ZoneId serverZone : possibleServerZones) {
            Instant result =
                    scheduledLocalTime.atZone(serverZone).toInstant();

            System.out.println("Если использовать " + serverZone + " -> " + result + "; совпадает: " + expectedInstant.equals(result)
            );
        }

        Instant serverDependentResult = scheduledLocalTime.atZone(ZoneId.systemDefault()).toInstant();
        System.out.println("\nТекущая default zone сервера: " + ZoneId.systemDefault());
        System.out.println("Результат через default zone: " + serverDependentResult);
        System.out.println("Правильный бизнес-результат: " + expectedInstant);
    }
}