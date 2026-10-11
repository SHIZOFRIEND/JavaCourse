package lb2;
public class Task5 {

    static class TestResource implements AutoCloseable {

        private boolean errorOnClose;

        TestResource(boolean errorOnClose) {
            this.errorOnClose = errorOnClose;
            System.out.println("open");
        }

        void use(boolean error) throws Exception {
            System.out.println("use");

            if (error) {
                throw new Exception("Ошибка в работе");
            }
        }

        @Override
        public void close() throws Exception {
            System.out.println("close");

            if (errorOnClose) {
                throw new Exception("Ошибка в close");
            }
        }
    }

    public static void main(String[] args) {

        System.out.println("1. Успешная работа");

        try (TestResource resource = new TestResource(false)) {
            resource.use(false);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println();


        System.out.println("2. Ошибка в работе");

        try (TestResource resource = new TestResource(false)) {
            resource.use(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println();


        System.out.println("3. Ошибка в работе и close");

        try (TestResource resource = new TestResource(true)) {
            resource.use(true);
        } catch (Exception e) {

            System.out.println("Основная ошибка: "
                    + e.getMessage());

            System.out.println("Количество suppressed: "
                    + e.getSuppressed().length);

            for (Throwable suppressed : e.getSuppressed()) {
                System.out.println(
                        "Подавленная ошибка: "
                                + suppressed.getMessage()
                );
            }
        }
    }
}