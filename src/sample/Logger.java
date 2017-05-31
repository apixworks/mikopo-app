package sample;

/**
 * Created by Apix on 30/05/2017.
 */
import java.io.*;
import java.text.*;
import java.util.*;

public class Logger {
    protected static String defaultLogFile = "E:\\MJsyslog.txt";

    public static void write(String s) throws IOException {
        write(defaultLogFile, s);
    }

    public static void write(String f, String s) throws IOException {
        //TimeZone tz = TimeZone.getTimeZone("EST"); // or PST, MID, etc ...
        Date now = new Date();
        DateFormat df = new SimpleDateFormat ("EEE, d MMM yyyy HH:mm:ss");
        //df.setTimeZone(tz);
        String currentTime = df.format(now);

        FileWriter aWriter = new FileWriter(f, true);
        aWriter.write(currentTime + " " + s + "\r\n\r\n");
        aWriter.flush();
        aWriter.close();
    }
}

