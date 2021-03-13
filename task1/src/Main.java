/*
 * task1
 * Реализуйте функцию, которая конвертирует число (без знака) из десятичной системы исчисления
 * в любую другую. Ваша функция должна иметь следующий прототип:
 * String itoBase(unsigned int nb, String base); nb – это подаваемое число, base – система исчисления.
 * На пример, «01» - двоичная, «012» - троичная, «0123456789abcdef» - шеснадцатиричная, «котики»
 * - система исчисления в котиках.
 * Дополнительно*: перегрузите функцию, чтобы она могла конвертировать число из любой системы
 * исчисления в любую другую:
 * String itoBase(String nb, String baseSrc, String baseDst);
 * Для проверки задания, напишите метод main, который принимает необходимые значения из
 * аргументов командной строки, и выводит результат на экран. При некорректном вводе
 * аргументов должен выводится usage.
 */

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Main {

    // args[0] - целое положительное число
    // args[1] - основание системы исчисления в формате «01» - двоичная, «012» - троичная, «0123456789abcdef» и т.д.
    // args[2] - второе (целевое) основание СИ (для перевода из одной СИ в другую)
    public static void main(String[] args) {
        switch (args.length) {
            case 2:
                int number;
                try {
                    number = Integer.parseInt(args[0]);
                } catch (NumberFormatException ex) {
                    showUsage();
                    return;
                }
                System.out.println(itoBase(number, args[1]));
                break;

            case 3:
                System.out.println(itoBase(args[0], args[1], args[2]));
                break;

            default:
                showUsage();
        }
    }

    private static void showUsage() {
        System.out.println("Неправильно заданы аргументы запуска. Пример использования: java -jar task1 number baseSrc [baseDst]");
    }

    public static String itoBase(int nb, String base) {
        if (nb == 0) {
            return String.valueOf(base.charAt(0));
        }

        StringBuilder convertedNumber = new StringBuilder();

        int actualBase = base.length();

        while (nb > 0) {
            convertedNumber.append(base.charAt(nb % actualBase));
            nb = nb / actualBase;
        }
        convertedNumber.reverse();

        return convertedNumber.toString();
    }

    public static String itoBase(String nb, String baseSrc, String baseDst) {
        return itoBase(baseToI(nb, baseSrc), baseDst);
    }

    private static int baseToI(String number, String base) {
        int n = 0;
        int actualBase = base.length();

        for(int i = 0; i < number.length(); ++i) {
            n = actualBase * n + base.indexOf(number.charAt(i));
        }

        return n;
    }
}

