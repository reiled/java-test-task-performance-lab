import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class BarrelWaterLevelLogRecord {

    private static final SimpleDateFormat LOG_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private static final String FAIL_MESSAGE_TEXT = "(фейл)";
    private static final String SUCCESS_MESSAGE_TEXT = "(успех)";
    private static final String ACTION_SCOOP_TEXT = "wanna scoop";
    private static final String ACTION_TOP_UP_TEXT = "wanna top up";

    private final ActionResult actionResult;
    private final ActionType actionType;
    private final String username;
    private final GregorianCalendar dateTime;
    private final int waterAmount;

    private BarrelWaterLevelLogRecord() {
            this.dateTime = null;
            this.waterAmount = 0;
            this.username = null;
            this.actionResult = null;
            this.actionType = null;
        }

    public BarrelWaterLevelLogRecord(BarrelWaterLevelLogRecord otherRecord) {
        this.dateTime = (GregorianCalendar) otherRecord.dateTime.clone();
        this.waterAmount = otherRecord.waterAmount;
        this.username = otherRecord.username;
        this.actionResult = otherRecord.actionResult;
        this.actionType = otherRecord.actionType;
    }

    private BarrelWaterLevelLogRecord(GregorianCalendar dateTime, String username, ActionType actionType, int waterAmount, ActionResult actionResult) {
        this.dateTime = (GregorianCalendar) dateTime.clone();
        this.waterAmount = waterAmount;
        this.username = username;
        this.actionResult = actionResult;
        this.actionType = actionType;
    }

    public static BarrelWaterLevelLogRecord parse(String rawLogRecord) {
        String rawDate = rawLogRecord.substring(0, 24);

        // отбрасываем обработанную дату
        rawLogRecord = rawLogRecord.substring(27);

        String username = rawLogRecord.substring(0, rawLogRecord.indexOf('-') - 1);

        // отбрасываем полученный username
        rawLogRecord = rawLogRecord.substring(username.length() + 3);

        ActionResult actionResult;
        if (rawLogRecord.endsWith(SUCCESS_MESSAGE_TEXT)) {
            actionResult = ActionResult.SUCCESS;
            rawLogRecord = rawLogRecord.substring(0, rawLogRecord.length() - SUCCESS_MESSAGE_TEXT.length());
        } else {
            actionResult = ActionResult.FAIL;
            rawLogRecord = rawLogRecord.substring(0, rawLogRecord.length() - FAIL_MESSAGE_TEXT.length());
        } // отбрасываем результат выполнения действия

        ActionType actionType;
        if (rawLogRecord.startsWith(ACTION_SCOOP_TEXT)) {
            actionType = ActionType.SCOOP;
            rawLogRecord = rawLogRecord.substring(ACTION_SCOOP_TEXT.length());
        } else {
            actionType = ActionType.TOP_UP;
            rawLogRecord = rawLogRecord.substring(ACTION_TOP_UP_TEXT.length());
        } // отбрасываем тип действия

        // наконец, парсим количество воды, над которым выполняется действие
        int waterAmount = Integer.parseInt(rawLogRecord.replaceAll("\\D", ""));

        // парсим дату из строки в объект
        GregorianCalendar dateTime = new GregorianCalendar();
        try {
            dateTime.setTime(LOG_DATE_FORMAT.parse(rawDate));
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
        }

        return new BarrelWaterLevelLogRecord(dateTime, username, actionType, waterAmount, actionResult);
    }

    @Override
    public String toString() {
        return "BarrelWaterLevelLogRecord{" +
                "actionResult=" + actionResult +
                ", actionType=" + actionType +
                ", username='" + username + '\'' +
                ", dateTime=" + LOG_DATE_FORMAT.format(dateTime.getTime()) +
                ", waterAmount=" + waterAmount +
                '}';
    }

    public ActionResult getActionResult() {
        return actionResult;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public GregorianCalendar getDateTime() {
        return dateTime;
    }

    public int getWaterAmount() {
        return waterAmount;
    }

    public String getUsername() {
        return username;
    }
}

