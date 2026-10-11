package lb2;

import java.util.Optional;

public class Task7 {


    static Optional<String> findCourse(int id) {

        if (id == 1) {
            return Optional.of("Java");
        }

        return Optional.empty();
    }

 
    static int parseCourse(String value) {

        return Integer.parseInt(value);
    }

 
    static class EnrollResult {

        boolean success;
        String message;

        EnrollResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }


    static EnrollResult enroll(int studentId, int courseId) {

        if (studentId == 1 && courseId == 1) {
            return new EnrollResult(
                    true,
                    "Запись успешна"
            );
        }

        return new EnrollResult(
                false,
                "Запись отклонена"
        );
    }

 
    static double calculatePrice(int count)
            throws Exception {

        if (count < 0) {
            throw new Exception(
                    "Количество не может быть отрицательным"
            );
        }

        return count * 100;
    }


    public static void main(String[] args) {
 
        Optional<String> course = findCourse(2);

        if (course.isPresent()) {
            System.out.println(
                    "Курс: " + course.get()
            );
        } else {
            System.out.println(
                    "Курс не найден"
            );
        }


        
        try {
            int id = parseCourse("abc");

            System.out.println(
                    "ID курса: " + id
            );

        } catch (NumberFormatException e) {
            System.out.println(
                    "Неверный формат курса"
            );
        }


        
        EnrollResult result = enroll(2, 1);

        if (result.success) {
            System.out.println(result.message);
        } else {
            System.out.println(
                    "Причина отказа: "
                            + result.message
            );
        }


        
        try {
            double price = calculatePrice(-5);

            System.out.println(
                    "Цена: " + price
            );

        } catch (Exception e) {
            System.out.println(
                    "Ошибка расчёта: "
                            + e.getMessage()
            );
        }
    }
}