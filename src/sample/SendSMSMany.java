package sample;

import sample.backend.DatabaseHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Apix on 21/06/2017.
 */
public class SendSMSMany {
    static boolean success = false;
    public static boolean sendSms(List<String> phones, int id,String msg) {
        try {
            // Construct data
            String user = "username=" + "hewlettpin@hotmail.com"; //youremail@address.com
            String hash = "&hash=" + "f99aba0694ce5c14770010f52cf22a5aa03ec510694d416d7dddc1b54a087ac2"; //Your API hash
            String sender = "&sender=" + "MJ Limited";
            String numbers = "&numbers=" + phones;

            String message;
            if(id!=0){
                DatabaseHandler db = new DatabaseHandler();
                message = "&message=" + db.viewSms(id);
            }else{
                message = "&message=" + msg;
            }

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("http://api.txtlocal.com/send/?").openConnection();
            String data = user + hash + numbers + message + sender;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }
            rd.close();

            System.out.println(stringBuffer.toString());
            success = true;
        } catch (Exception e) {
            System.out.println("Error SMS "+e);
            success = false;
        }
        return success;
    }
}
