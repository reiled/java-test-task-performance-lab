import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        // String source = "{sphere: {center: [0, 0, 0], radius: 10.67}, line: {[1, 0.5, 15], [43, -14.6, 0.04]}}";
        // String source1 = "{sphere: {radius: 10.67, center: [0, 0, 0]}, line: {[1, 0.5, 15], [43, -14.6, 0.04]}}";
        // String source2 = "{line: {[1, 0.5, 15], [43, -14.6, 0.04]}, sphere: {center: [0, 0, 0], radius: 10.67}}";

        // для этих данных существует 2 точки коллизии: (2.07, 4.71, 2.71) и (-1.48, 2.35, 0.35)
        // String test = "{line: {[4, 6, 4], [7.6, 8.4, 6.4]}, sphere: {center: [1, 4, 0], radius: 3}}";
        // одна точка - (4, 0, 0)
        // String test = "{line: {[4, 0, 1], [4, 0, 5]}, sphere: {center: [0, 0, 0], radius: 4}}";

        String data = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(args[0]));
            data = bufferedReader.readLine();
            bufferedReader.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return;
        } catch (ArrayIndexOutOfBoundsException ex) {
            showUsage();
            return;
        }

        Pair<Line, Sphere> objects = parseData(data);
        ArrayList<Point3D> collisionPoints = ObjectCollider.findCollisionPoints(objects.getKey(), objects.getValue());

        if (collisionPoints.isEmpty()) {
            System.out.println("Коллизий не найдено");
        } else {
            for (Point3D point : collisionPoints) {
                System.out.printf(Locale.US, "[%.2f, %.2f, %.2f]%n", point.getX(), point.getY(), point.getZ());
            }
        }
    }

    private static void showUsage() {
        System.out.println("Неправильно заданы аргументы запуска. Пример использования: java -jar task2 path-to-file");
    }

    private static Pair<Line, Sphere> parseData(String data) {
        Sphere sphere = null;
        Line line = null;
        // опускаем внешние фигурные скобки, разбиваем на 2 строки, содержащие информацию об отдельном объекте
        String[] objectData = data.substring(1, data.length()-1).replaceAll(" ", "").split("},");
        for (String s : objectData) {
            if (s.contains("sph")) {
                // парсим сферу
                // парсим радиус
                StringBuilder r = new StringBuilder();
                for (int i = s.indexOf("rad") + 7; i < s.length(); ++i) {
                    char ch = s.charAt(i);
                    if (Character.isDigit(ch) || ch == '.') {
                        r.append(ch);
                    } else {
                        break;
                    }
                }
                double radius = Double.parseDouble(r.toString());

                // парсим положение центра
                String[] coords = s.substring(s.indexOf('[') + 1, s.indexOf(']')).split(",");
                Point3D center = new Point3D(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]));

                sphere = new Sphere(radius, center);
            } else {
                // парсим линию
                int substrEnd = s.endsWith("}") ? s.length() - 2 : s.length() - 1;
                String[] points = s.substring(s.indexOf("{[") + 2, substrEnd).split("],\\[");
                String[] coords1 = points[0].split(",");
                String[] coords2 = points[1].split(",");

                Point3D first = new Point3D(Double.parseDouble(coords1[0]), Double.parseDouble(coords1[1]), Double.parseDouble(coords1[2]));
                Point3D second = new Point3D(Double.parseDouble(coords2[0]), Double.parseDouble(coords2[1]), Double.parseDouble(coords2[2]));

                line = new Line(first, second);
            }
        }
        return new Pair<>(line, sphere);
    }

    // TODO: Render scene
}
