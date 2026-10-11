package lb2;
public class Task1 {

    static void method1() {
        method2();
    }

    static void method2() {
        method3();
    }

    static void method3() {
        method4();
    }

    static void method4() {
        int a = 10;
        int b = 0;

        System.out.println(a / b);
    }

    public static void main(String[] args) {
        try {
            method1();
        } catch (ArithmeticException e) {
            System.out.println("Произошло исключение:");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Программа продолжает работу");
    }
}