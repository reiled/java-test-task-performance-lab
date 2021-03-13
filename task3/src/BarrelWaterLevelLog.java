import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.stream.Collectors;

public class BarrelWaterLevelLog {

    static class MetaData {
        private final int initialWaterLevel;
        private final int maxBarrelVolume;

        public MetaData(int waterLevel, int barrelVolume) {
            initialWaterLevel = waterLevel;
            maxBarrelVolume = barrelVolume;
        }

        public int getInitialWaterLevel() {
            return initialWaterLevel;
        }

        public int getMaxBarrelVolume() {
            return maxBarrelVolume;
        }
    }

    private final ArrayList<BarrelWaterLevelLogRecord> log;
    private final MetaData metaData;

    private BarrelWaterLevelLog() {
        this.log = null;
        this.metaData = null;
    }

    public BarrelWaterLevelLog(String pathToLogFile) {
        this.log = parse(pathToLogFile);
        this.metaData = parseMetaData(pathToLogFile);
    }

    private static ArrayList<BarrelWaterLevelLogRecord> parse(String fileName) {
        ArrayList<BarrelWaterLevelLogRecord> logLines = new ArrayList<>();
        String line;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String header = bufferedReader.readLine();
            int barrelVolume = Integer.parseInt(bufferedReader.readLine());
            int currentWaterLevel = Integer.parseInt(bufferedReader.readLine());

            while ((line = bufferedReader.readLine()) != null) {
                logLines.add(BarrelWaterLevelLogRecord.parse(line));
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        return logLines;
    }

    private static MetaData parseMetaData(String fileName) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String header = bufferedReader.readLine();
            int barrelVolume = Integer.parseInt(bufferedReader.readLine());
            int currentWaterLevel = Integer.parseInt(bufferedReader.readLine());
            return new MetaData(currentWaterLevel, barrelVolume);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public ArrayList<BarrelWaterLevelLogRecord> filterByDate(String dateFromAsString, String dateToAsString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        GregorianCalendar dateFrom = new GregorianCalendar();
        GregorianCalendar dateTo = new GregorianCalendar();
        try {
            dateFrom.setTime(format.parse(dateFromAsString));
            dateTo.setTime(format.parse(dateToAsString));
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
        }

        return filterByDate(dateFrom, dateTo);
    }

    public ArrayList<BarrelWaterLevelLogRecord> filterByDate(GregorianCalendar dateFrom, GregorianCalendar dateTo) {
        return log.stream()
                .filter(x -> x.getDateTime().compareTo(dateFrom) >= 0 && x.getDateTime().compareTo(dateTo) <= 0)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<BarrelWaterLevelLogRecord> getLog() {
        return log;
    }

    public MetaData getMetaData() {
        return metaData;
    }
}
