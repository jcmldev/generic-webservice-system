
package dd;

import java.util.Date;


public class Timestamp 
{

    public static long now()
    {
        return (new Date()).getTime();
    }

    /*
    //debug version of NOW
    private static long     s_now = 0;
    
    public static long now()
    {
        return s_now;
    }
    
    public static void setnow(long newnow)
    {
        s_now = newnow;
    }
    */
}