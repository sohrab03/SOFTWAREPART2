import java.io.FileWriter;
import java.io.IOException;

public class Log {
    private static Log instance; // Singleton
    private StringBuilder logBuilder; // To save the log events

    private Log() {
        logBuilder = new StringBuilder();
    }

    public static Log getInstance() {
        if (instance == null) {
            instance = new Log();
        }
        return instance;
    }

    public void addEvent(String event) {
        logBuilder.append(event).append("\n");
    }

    public void saveLogToFile(String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(logBuilder.toString());
            System.out.println("Log saved to " + fileName);
        } catch (IOException e) {
            System.err.println("Error saving log to file: " + e.getMessage());
        }
    }

    public void printLog() {
        System.out.println(logBuilder.toString());
    }
}
