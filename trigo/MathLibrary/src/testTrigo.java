import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class testTrigo {
    public static final float Pi = 3.1415926f;
    static final finalTrigo f = new finalTrigo();

    void testSin(){
        for (float i = -10000; i < 10000; i+= 10) {
            float mine = f.sin(i);
            float real = (float)Math.sin(i);
            float err = Math.abs(mine - real)/Math.ulp(real);
            System.out.println("pour i = " + i + " l erreur est : " + err);
            //System.out.println(i + " " + err);
        }
    }

    public void testCos(){
        for (float i = 0.0f; i < Pi/2; i+= 0.01) {
            float mine = f.cos(i);
            float real = (float)Math.cos(i);
            float err = Math.abs(mine - real)/Math.ulp(real);
            System.out.println("pour i = " + i + " l erreur est : " + err);
            //System.out.println(i + " " + err);
        }
    }

    public void testAtan(){
        for (float i = -10000; i < 10000; i++) {
            float mine = f.arctan(i);
            float real = (float)Math.atan(i);
            float err = Math.abs(mine - real)/Math.ulp(real);
            System.out.println("pour i = " + i + " l erreur est : " + err);
            //System.out.println(i + " " + err);
        }
    }

    public void testAsin(){
        for (float i = -1.0f; i < 1.0; i+= 0.01) {
            float mine = f.arcsin(i);
            float real = (float)Math.asin(i);
            float err = Math.abs(mine - real)/Math.ulp(real);
            System.out.println("pour i = " + i + " l erreur est : " + err);
            //System.out.println(i + " " + err);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        testTrigo t = new testTrigo();
        System.out.println("Test asin ");
        t.testAsin();
        System.out.println("Test atan ");
        t.testAtan();
        System.out.println("Test cos ");
        t.testCos();
        System.out.println("Test sin ");
        t.testSin();
    }
}
