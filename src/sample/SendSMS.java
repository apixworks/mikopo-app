package sample;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Apix on 13/05/2017.
 */
public class SendSMS {
    boolean success = false;
    public boolean sendSms(String phone) {
        try {
            // Construct data
            String user = "username=" + "hewlettpin@hotmail.com"; //youremail@address.com
            String hash = "&hash=" + "f99aba0694ce5c14770010f52cf22a5aa03ec510694d416d7dddc1b54a087ac2"; //Your API hash
            String message = "&message=" + "Habari ndugu mteja, unakumbushwa kuja kulipa deni lako la mwezi uliyopita. Asante.";
            String sender = "&sender=" + "MJ Limited";
            String numbers = "&numbers=" + phone;

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
