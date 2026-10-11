package lb2;

public class Task6 {

    static class CourseImportException extends Exception {

        private int recordNumber;

        public CourseImportException(
                int recordNumber,
                String message,
                Throwable cause) {

            super(message, cause);
            this.recordNumber = recordNumber;
        }

        public int getRecordNumber() {
            return recordNumber;
        }
    }

    // Низкоуровневый parser
    static int parseCourseId(String value) {
        return Integer.parseInt(value);
    }

    // Уровень импорта
    static void importCourse(
            int recordNumber,
            String courseId)
            throws CourseImportException {

        try {
            int id = parseCourseId(courseId);

            System.out.println(
                    "Курс импортирован: " + id
            );

        } catch (NumberFormatException e) {

            throw new CourseImportException(
                    recordNumber,
                    "Ошибка импорта курса",
                    e
            );
        }
    }

    public static void main(String[] args) {

        try {
            importCourse(15, "abc");

        } catch (CourseImportException e) {

            System.out.println(
                    "Ошибка в записи: "
                            + e.getRecordNumber()
            );

            System.out.println(
                    "Сообщение: "
                            + e.getMessage()
            );

            System.out.println(
                    "Причина: "
                            + e.getCause()
            );
        }
    }
}