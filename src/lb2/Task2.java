package lb2;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;

public class Task2 {

    public static void main(String[] args) {
        try {
            int age = -5;

            if (age < 0) {
                throw new IllegalArgumentException("Неверный возраст");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("1. " + e.getMessage());
        }
        try {
            throw new FileNotFoundException("Файл не найден");
        } catch (FileNotFoundException e) {
            System.out.println("2. " + e.getMessage());
        }

        try {
            throw new IllegalStateException("Неверное состояние объекта");
        } catch (IllegalStateException e) {
            System.out.println("3. " + e.getMessage());
        }
        try {
            throw new IOException("Сеть недоступна");
        } catch (IOException e) {
            System.out.println("4. " + e.getMessage());
        }
        try {
            throw new IOException("Ошибка конфигурации");
        } catch (IOException e) {
            System.out.println("5. " + e.getMessage());
        }
        try {
            throw new OutOfMemoryError("Недостаточно памяти");
        } catch (OutOfMemoryError e) {
            System.out.println("6. " + e.getMessage());
        }
        try {
            throw new IllegalArgumentException("Некорректный ввод");
        } catch (IllegalArgumentException e) {
            System.out.println("7. " + e.getMessage());
        }
        try {
            int a = 10;
            int b = 0;
            System.out.println(a / b);
        } catch (ArithmeticException e) {
            System.out.println("8. Деление на ноль");
        }
        try {
            throw new NoSuchElementException("Элемент не найден");
        } catch (NoSuchElementException e) {
            System.out.println("9. " + e.getMessage());
        }
        try {
            throw new IOException("Ошибка подключения к базе данных");
        } catch (IOException e) {
            System.out.println("10. " + e.getMessage());
        }
    }
}