/*
 * Напишите программу, которая сравнивает 2 строки одинаковые ли они. Результат: вывод «ОК»,
 * если строки идентичны, «КО», если не идентичны. Строки подаются в виде аргументов командной
 * строки.
 * Примечание: во второй строке может быть символ ‘*’ – он заменяет собой любое количество
 * любых символов.
 * Например:
 * «аааа» «аааа» - ОК
 * «аааа» «аа*» - ОК
 * «a» «*****» - ОК
 */

public class Main {

    /*
    private static final String[][] EXAMPLES = {
            new String[] {
                    "aaaa", "aaaa"
            },
            new String[] {
                    "aaaa", "aaab"
            },
            new String[] {
                    "aaaa", "aaaaa"
            },
            new String[] {
                    "aaaa", "aa*"
            },
            new String[] {
                    "aaaa", "aa*aa"
            },
            new String[] {
                    "aaaa", "aa*b"
            },
            new String[] {
                    "aaab", "aa*b"
            },
            new String[] {
                    "abbb", "aa*b"
            },
            new String[] {
                    "abbb", "ab***"
            },
            new String[] {
                    "aaaaaaa", "aa**a****a*"
            },
            new String[] {
                    "baaaaaa", "aa**a****a*"
            },
            new String[] {
                    "baaaaaaa", "baaa*a*"
            },
            new String[] {
                    "a", "*****"
            }
    };
     */

    public static void main(String[] args) {
        if (args.length != 2) {
            showUsage();
            return;
        }

        System.out.println(StringComparator.areEqual(args[0], args[1]) ? "ОК" : "КО");
    }

    public static void showUsage() {
        System.out.println("Неправильно заданы аргументы запуска. Пример использования: java -jar task4 firstString secondString");
    }
}
