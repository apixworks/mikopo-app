package sample;

import java.io.IOException;

/**
 * Created by Apix on 10/07/2017.
 */
public class CheckInternetConnection {

    public static int check() throws IOException,InterruptedException{
        Process process = java.lang.Runtime.getRuntime().exec("ping www.google.com");
        return  process.waitFor();
    }

}
