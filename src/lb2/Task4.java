package lb2;

public class Task4 {

    static int example1() {
        try {
            System.out.println("try");
            return 1;
        } finally {
            System.out.println("finally");
        }
    }

    static int example2() {
        try {
            System.out.println("try");
            throw new Exception("Ошибка");
        } catch (Exception e) {
            System.out.println("catch");
            return 2;
        } finally {
            System.out.println("finally");
        }
    }

    static void example3() throws Exception {
        try {
            System.out.println("try");
            throw new Exception("Ошибка");
        } finally {
            System.out.println("finally");
        }
    }

    static int example4() {
        try {
            System.out.println("try");
            return 1;
        } catch (Exception e) {
            System.out.println("catch");
            return 2;
        } finally {
            System.out.println("finally");
        }
    }

    public static void main(String[] args) {

        System.out.println("Пример 1:");
        System.out.println("Результат: " + example1());

        System.out.println();

        System.out.println("Пример 2:");
        System.out.println("Результат: " + example2());

        System.out.println();

        System.out.println("Пример 3:");
        try {
            example3();
        } catch (Exception e) {
            System.out.println("Исключение поймано");
        }

        System.out.println();

        System.out.println("Пример 4:");
        System.out.println("Результат: " + example4());
    }
}