import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;

public class Utils
{
    public static InputStream getResource (String name)
    {
        InputStream is = ClassLoader.getSystemResourceAsStream (name);
        if (is == null)
        {
            System.out.println ("could not load: "+name);
            return null;
        }
        return new BufferedInputStream (is);
    }

// --Commented out by Inspection START (12/30/2021 4:53 AM):
//    public static URL getResourceAsURL(String name)
//    {
//        return ClassLoader.getSystemResource(name);
//    }
// --Commented out by Inspection STOP (12/30/2021 4:53 AM)
}
