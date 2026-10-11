package lb3;

public class Task6 {

    static class Result<T> {

        private final T value;
        private final String error;
        private final boolean success;
        private Result(T value, String error, boolean success) {
            if (success && value == null) {
                throw new IllegalArgumentException("У успешного результата значение не может быть null");
            }
            if (!success && error == null) {
                throw new IllegalArgumentException("У ошибочного результата должна быть ошибка");
            }
            this.value = value;
            this.error = error;
            this.success = success;
        }

        public static <T> Result<T> success(T value) {
            if (value == null) {
                throw new IllegalArgumentException("Значение успеха не может быть null");
            }
            return new Result<>(value, null, true);
        }
        public static <T> Result<T> error(String error) {
            if (error == null || error.isEmpty()) {
                throw new IllegalArgumentException("Сообщение об ошибке не может быть пустым");
            }
            return new Result<>(null, error, false);
        }

        public boolean isSuccess() {
            return success;
        }

        public boolean isError() {
            return !success;
        }

        public T getValue() {
            if (!success) {
                throw new IllegalStateException("Нельзя получить значение ошибочного результата");
            }
            return value;
        }

        public String getError() {
            if (success) {
                throw new IllegalStateException("У успешного результата нет ошибки");
            }

            return error;
        }

        @Override
        public String toString() {
            if (success) {
                return "Success: " + value;
            }
            return "Error: " + error;
        }
    }
    public static void main(String[] args) {

        Result<String> stringResult =
                Result.success("Курс Java найден");

        System.out.println(stringResult);
        System.out.println("Значение: " + stringResult.getValue());

        Result<Integer> integerResult = Result.success(95);

        System.out.println(integerResult);
        System.out.println("Значение: " + integerResult.getValue());

        Result<String> errorResult = Result.error("Курс не найден");

        System.out.println(errorResult);
        System.out.println("Ошибка: " + errorResult.getError());

        if (stringResult.isSuccess()) {
            System.out.println("Строковый результат успешно получен.");
        }
        if (errorResult.isError()) {
            System.out.println("Получен ошибочный результат."
            );
        }
    }
}