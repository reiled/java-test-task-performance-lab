import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

public class LogGenerator {

    private LogGenerator() {

    }

    public static ArrayList<String> generateMockLog(int nEntries) {
        ArrayList<String> logEntries = new ArrayList<>();
        Random random = new Random();
        final String[] usernames = new String[] {
                "Charles", "John", "Liberty", "Joseph", "Elmer", "Hamzah", "Tobias", "Samuel", "Sami", "Jax", "Casey", "Otis", "Charlie"
        };

        // в задании отсутствуют уточнения по формату записи даты в логах, в приведенных примерах
        // в формате даты использован литерал Z, что означает временную зону, однако в предшествующие
        // ему цифры различны и больше похожи на запись милисекунд, чем на запись таймзоны по стандарту RFC822
        // 2020-01-01Т12:51:32.124Z – [username1] - wanna top up 10l (успех)
        // 2020-01-01Т12:51:34.769Z – [username2] - wanna scoop 50l (фейл)
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        GregorianCalendar calendar = new GregorianCalendar(2021, Calendar.JANUARY, 1);
        calendar.set(Calendar.HOUR, 12);

        final int barrelVolume = 200;
        int currentWaterLevel = 32;

        logEntries.add("META DATA:");
        logEntries.add(String.format("%d", barrelVolume));
        logEntries.add(String.format("%d", currentWaterLevel));

        final String actionTopUp = "wanna top up";
        final String actionScoop = "wanna scoop";

        final String actionCanBePerformedMessage = "(успех)";
        final String actionCantBePerformedMessage = "(фейл)";

        String action;
        boolean canPerformAction;

        int waterAmount = 0;
        for (int i = 0; i < nEntries; ++i) {
            waterAmount = random.nextInt(50);
            canPerformAction = false;

            calendar.add(Calendar.MINUTE, 5 + random.nextInt(295));
            calendar.add(Calendar.SECOND, random.nextInt(60));
            calendar.add(Calendar.MILLISECOND, random.nextInt(1000));

            if (random.nextBoolean()) {
                // top up
                action = actionTopUp;
                if (waterAmount + currentWaterLevel < barrelVolume) {
                    canPerformAction = true;
                    currentWaterLevel += waterAmount;
                }
            } else {
                // scoop
                action = actionScoop;
                if (currentWaterLevel - waterAmount > 0) {
                    canPerformAction = true;
                    currentWaterLevel -= waterAmount;
                }
            }

            logEntries.add(
                    String.format("%s - %s - %s %dl %s",
                            dateFormatter.format(calendar.getTime()),
                            usernames[random.nextInt(usernames.length)],
                            action,
                            waterAmount,
                            canPerformAction ? actionCanBePerformedMessage : actionCantBePerformedMessage
                    )
            );
        }

        return logEntries;
    }

    public static void writeMockLogToFile(String filename, ArrayList<String> mockLog) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename))) {
            for (String s : mockLog) {
                bufferedWriter.write(s);
                bufferedWriter.newLine();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
