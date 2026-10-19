package lb5;
public class Task1 {
    private static void compare(String title, String firstName, String secondName,
                                String first, String second) {
        System.out.println(title);
        System.out.println(firstName + " == " + secondName + ": " + (first == second));
        System.out.println(firstName + ".equals(" + secondName + "): " + first.equals(second));
        System.out.println();
    }

    private static String concatenateAtRuntime(String first, String second) {
        return first + second;
    }

    public static void main(String[] args) {
        String literal1 = "Java";
        String literal2 = "Java";
        compare("1. Два одинаковых литерала:", "literal1", "literal2",
                literal1, literal2);

        String created = new String("Java");
        compare("2. Литерал и new String:", "literal1", "created",
                literal1, created);

        String compileTimeConcat = "Ja" + "va";
        compare("3. Конкатенация на этапе компиляции:", "literal1", "compileTimeConcat",
                literal1, compileTimeConcat);

        String part1 = "Ja";
        String part2 = "va";
        String runtimeConcat = concatenateAtRuntime(part1, part2);
        compare("4. Конкатенация во время выполнения:", "literal1", "runtimeConcat",
                literal1, runtimeConcat);

        String interned = created.intern();
        compare("5. Результат intern():", "literal1", "interned",
                literal1, interned);

        System.out.println("created == interned: " + (created == interned));
        System.out.println("runtimeConcat == runtimeConcat.intern(): "
                + (runtimeConcat == runtimeConcat.intern()));
    }
}