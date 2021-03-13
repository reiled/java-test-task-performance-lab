public class StringComparator {

    private StringComparator() {

    }

    // Путь наименьшего сопротивления - воспользоваться regexp, но ведь суть задания не в этом?
    public static boolean easyWay(String s1, String s2) {
        String regex = s2.replaceAll("(\\*)+", "(.)*");
        return s1.replaceFirst(regex, "").length() == 0;
    }

    public static boolean areEqual(String string, String pattern) {
        // удаляем идущие друг за другом wildcard'ы
        return _areEqual(string, pattern.replaceAll("(\\*)+", "*"));
    }

    private static boolean _areEqual(String string, String pattern)
    {
        // достигли конца обеих строк, выходим из рекурсии
        // пустые строки - совпадают
        if (string.length() == 0 && pattern.length() == 0) {
            return true;
        }

        // если встретили wildcard, после него еще есть символы, но строка уже полностью пройдена - строки не совпадают
        if (string.length() == 0 && pattern.length() > 1 && pattern.charAt(0) == '*') {
            return false;
        }

        // совпадение текущих символов в обеих строках
        if (string.length() != 0  && pattern.length() != 0 && string.charAt(0) == pattern.charAt(0)) {
            return _areEqual(string.substring(1), pattern.substring(1));
        }

        // встретили wildcard: или игнорируем его, или считаем символ строки за совпадение
        if (pattern.length() > 0 && pattern.charAt(0) == '*')
            return _areEqual(string, pattern.substring(1)) || _areEqual(string.substring(1), pattern);

        return false;
    }
}
