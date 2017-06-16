package sample;

/**
 * Created by Apix on 15/06/2017.
 */
public class Checker {
    public static boolean isStringInt(String s)
    {
        try
        {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex)
        {
            return false;
        }
    }
}
