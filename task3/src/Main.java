import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        // в задании отсутствуют уточнения по формату записи даты в логах, в приведенных примерах
        // в формате даты использован литерал Z, что означает временную зону, однако в предшествующие
        // ему цифры различны и больше похожи на запись милисекунд, чем на запись таймзоны по стандарту RFC822
        // 2020-01-01Т12:51:32.124Z – [username1] - wanna top up 10l (успех)
        // 2020-01-01Т12:51:34.769Z – [username2] - wanna scoop 50l (фейл)
        // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        // LogGenerator.writeMockLogToFile("mock.log", LogGenerator.generateMockLog(17500));
        // LogGenerator.writeMockLogToFile("mock.log", LogGenerator.generateMockLog(50));

        if (args.length != 3) {
            showUsage();
            return;
        }

        BarrelWaterLevelLog log;
        GregorianCalendar dateFrom;
        GregorianCalendar dateTo;
        try {
            if (!(new File(args[0]).isFile())) {
                showUsage();
                return;
            }

            dateFrom = parseDate(args[1]);
            dateTo = parseDate(args[2]);

            if (dateFrom.compareTo(dateTo) > 0) {
                showUsage();
                return;
            }

            log = new BarrelWaterLevelLog(args[0]);
        } catch (Exception ex) {
            showUsage();
            return;
        }

        ArrayList<BarrelWaterLevelLogRecord> selected = log.filterByDate(args[1], args[2]);

        ArrayList<BarrelWaterLevelLogRecord> topUps = selected.stream()
                .filter(x -> x.getActionType() == ActionType.TOP_UP)
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<BarrelWaterLevelLogRecord> scoops = selected.stream()
                .filter(x -> x.getActionType() == ActionType.SCOOP)
                .collect(Collectors.toCollection(ArrayList::new));

        long nTimesWaterToppedUp = topUps.size();

        long nTimesWaterScooped = scoops.size();

        long nTimesAllActionsFailed = selected.stream()
                .filter(x -> x.getActionResult() == ActionResult.FAIL)
                .count();

        // long nTimesTopUpFailed = topUps.stream()
        //         .filter(x -> x.getActionResult() == ActionResult.FAIL)
        //         .count();

        // long nTimesScoopFailed = nTimesAllActionsFailed - nTimesTopUpFailed;

        // double topUpFailsPercent = 100.0 * nTimesTopUpFailed / nTimesWaterToppedUp;

        double allFailsPercent = selected.size() > 0 ? 100.0 * nTimesAllActionsFailed / selected.size() : 0;

        long waterAmountToppedUp = topUps.stream()
                .filter(x -> x.getActionResult() == ActionResult.SUCCESS)
                .mapToInt(BarrelWaterLevelLogRecord::getWaterAmount)
                .sum();

        long waterAmountIgnoredWhileToppingUp = topUps.stream()
                .filter(x -> x.getActionResult() == ActionResult.FAIL)
                .mapToInt(BarrelWaterLevelLogRecord::getWaterAmount)
                .sum();

        long waterAmountScooped = scoops.stream()
                .filter(x -> x.getActionResult() == ActionResult.SUCCESS)
                .mapToInt(BarrelWaterLevelLogRecord::getWaterAmount)
                .sum();

        long waterAmountIgnoredWhileScooping = scoops.stream()
                .filter(x -> x.getActionResult() == ActionResult.FAIL)
                .mapToInt(BarrelWaterLevelLogRecord::getWaterAmount)
                .sum();

        int selectedIntervalOffset = 0;
        long beforeIntervalWaterAmountToppedUp = 0;
        long beforeIntervalWaterAmountScooped = 0;
        if (selected.size() > 0) {
            selectedIntervalOffset = log.getLog().indexOf(selected.get(0));

            ArrayList<BarrelWaterLevelLogRecord> beforeInterval = log.getLog().stream()
                    .limit(selectedIntervalOffset)
                    .filter(x -> x.getActionResult() == ActionResult.SUCCESS)
                    .collect(Collectors.toCollection(ArrayList::new));

            beforeIntervalWaterAmountToppedUp = beforeInterval.stream()
                    .filter(x -> x.getActionType() == ActionType.TOP_UP)
                    .mapToInt(BarrelWaterLevelLogRecord::getWaterAmount)
                    .sum();

            beforeIntervalWaterAmountScooped = beforeInterval.stream()
                    .filter(x -> x.getActionType() == ActionType.SCOOP)
                    .mapToInt(BarrelWaterLevelLogRecord::getWaterAmount)
                    .sum();

        } else {
            if (dateTo.compareTo(log.getLog().get(0).getDateTime()) > 0 && dateTo.compareTo(log.getLog().get(log.getLog().size() - 1).getDateTime()) <= 0) {
                // если интервал с отсутствующими данными находится в пределах лога
                // подсчитаем уровень воды в бочке к его началу
                int offset = log.getLog().stream()
                        .filter(x -> x.getDateTime().compareTo(dateTo) <= 0)
                        .collect(Collectors.toCollection(ArrayList::new))
                        .size();

                ArrayList<BarrelWaterLevelLogRecord> temp = log.getLog().stream()
                        .limit(offset)
                        .filter(x -> x.getActionResult() == ActionResult.SUCCESS)
                        .collect(Collectors.toCollection(ArrayList::new));

                beforeIntervalWaterAmountToppedUp = temp.stream()
                        .filter(x -> x.getActionType() == ActionType.TOP_UP)
                        .mapToInt(BarrelWaterLevelLogRecord::getWaterAmount)
                        .sum();

                beforeIntervalWaterAmountScooped = temp.stream()
                        .filter(x -> x.getActionType() == ActionType.SCOOP)
                        .mapToInt(BarrelWaterLevelLogRecord::getWaterAmount)
                        .sum();
            } else if (dateFrom.compareTo(log.getLog().get(log.getLog().size() - 1).getDateTime()) >= 0) {
                // интервал начинается после последней даты в логе
                beforeIntervalWaterAmountToppedUp = log.getLog().stream()
                        .filter(x -> x.getActionResult() == ActionResult.SUCCESS)
                        .filter(x -> x.getActionType() == ActionType.TOP_UP)
                        .mapToInt(BarrelWaterLevelLogRecord::getWaterAmount)
                        .sum();

                beforeIntervalWaterAmountScooped = log.getLog().stream()
                        .filter(x -> x.getActionResult() == ActionResult.SUCCESS)
                        .filter(x -> x.getActionType() == ActionType.SCOOP)
                        .mapToInt(BarrelWaterLevelLogRecord::getWaterAmount)
                        .sum();
            }
        }

        long waterLevelBeforeInterval = log.getMetaData().getInitialWaterLevel() + beforeIntervalWaterAmountToppedUp - beforeIntervalWaterAmountScooped;
        long waterLevelAfterInterval = waterLevelBeforeInterval + waterAmountToppedUp - waterAmountScooped;

        // Не совсем понял условия, нужно ли постоянно перезаписывать файл или дополнять
        String csvFile = "./output.csv";
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(csvFile))) {
            //header
            bufferedWriter.write("Процент ошибок за указанный период");

            bufferedWriter.write(",");
            bufferedWriter.write("Попыток налить воду");
            bufferedWriter.write(",");
            bufferedWriter.write("Объем налитой воды");
            bufferedWriter.write(",");
            bufferedWriter.write("Объем не налитой воды");

            bufferedWriter.write(",");
            bufferedWriter.write("Попыток зачерпнуть воду");
            bufferedWriter.write(",");
            bufferedWriter.write("Объем зачерпнутой воды");
            bufferedWriter.write(",");
            bufferedWriter.write("Объем не зачерпнутой воды");

            bufferedWriter.write(",");
            bufferedWriter.write("Объем воды в начале периода");
            bufferedWriter.write(",");
            bufferedWriter.write("Объем воды в конце периода");

            bufferedWriter.newLine();

            bufferedWriter.write(String.format(Locale.US, "%.2f", allFailsPercent));

            bufferedWriter.write(",");
            bufferedWriter.write(String.format("%d", nTimesWaterToppedUp));
            bufferedWriter.write(",");
            bufferedWriter.write(String.format("%d", waterAmountToppedUp));
            bufferedWriter.write(",");
            bufferedWriter.write(String.format("%d", waterAmountIgnoredWhileToppingUp));

            bufferedWriter.write(",");
            bufferedWriter.write(String.format("%d", nTimesWaterScooped));
            bufferedWriter.write(",");
            bufferedWriter.write(String.format("%d", waterAmountScooped));
            bufferedWriter.write(",");
            bufferedWriter.write(String.format("%d", waterAmountIgnoredWhileScooping));

            bufferedWriter.write(",");
            bufferedWriter.write(String.format("%d", waterLevelBeforeInterval));
            bufferedWriter.write(",");
            bufferedWriter.write(String.format("%d", waterLevelAfterInterval));

            bufferedWriter.newLine();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void showUsage() {
        System.out.println("Неправильно заданы аргументы запуска. Пример использования: java -jar task3.jar ./log.log 2020-01-01T12:00:00 2020-01-01T13:00:00");
    }

    private static GregorianCalendar parseDate(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        GregorianCalendar d = new GregorianCalendar();
        d.setTime(format.parse(date));
        return d;
    }
}
