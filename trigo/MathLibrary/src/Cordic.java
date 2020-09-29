import java.io.FileNotFoundException;
import java.io.FilePermission;
import java.io.PrintStream;

public class Cordic {
    private ActanValues lookup;
    private final int iterations = 32;
    private final float K = 0.607253F;
    private final float halfPi = 1.570796F;
    static final float Pi = 3.141593F;

    public Cordic(){
        this.lookup = new ActanValues();
    }

    public float sin(float theta) {
        if (theta > 0){
            return trigoSinCos(theta).y;
        }
        return -trigoSinCos(theta).y;
    }

    public float cos(float theta) {
        return trigoSinCos(theta).x;
    }
    //fonction permettant de calculer asin et atan en meme temps;
    public float Cordic2(float x, float y, float z, float mode, int val){
        float t=1.0f;
        for (int i=0;i<iterations;i++){
            float x1;
            if (mode >=0 && y< mode || mode <0 && z>=0){
                x1=x-(y*t);
                y=y+(x*t);
                z = z-lookup.Val(i);
            }
            else{
                x1=x+(y*t);
                y=y-(x*t);
                z=z+ lookup.Val(i);
            }
            x=x1;
            t=t/2;
        }
        if (val == 1){
            return x;
        }
        if (val == 2){
            return y;
        }
        if (val == 3){
            return z;
        }
        else {
            return 9999f;
        }

    }

    public float atan(float a){
        float z=Cordic2(1,a,0,0,3);
        return z;
    }

    //fonction utilisée dans asin()
    public float gain1Cordic(){
        return Cordic2(1,0,0,-1,1);
    }
    public float asin(float a){
        float x = 1/gain1Cordic();
        float z=0;
        boolean f =true;
        if (a<0){
            a = -a;
            f=false;
        }
        z = Cordic2(x, 0, 0, a,3);
        if (f) {
            z= -z;
        }
        return z;
    }

    public doubleComp trigoSinCos(float theta) {
        doubleComp comp = new doubleComp();
        float x = K;
        float y = 0;
        float z = theta;
        float v = 1.0F;
        for (int i = 0; i < iterations; i++) {
            float d = (z >= 0)? +1 : -1;
            float tx = x - d * y * v;
            float ty = y + d * x * v;
            float tz = z - d * lookup.Val(i);
            x = tx; y= ty; z = tz;
            v *= 0.5;
        }

        comp.x = x;

        if (theta > 0){
            comp.y = y;
        }
        else {
            //System.out.println("valeur de sin est : " + y);
            comp.y = -y;
        }

        return comp;
    }


    static float power(float value1, float value2){
        float counter = 0.0F;
        float result = 1.0F;
        if (value2 < 0.0F){
            return power(1/value1, -value2);
        }
        else {
            while (value2 > counter){
                result *= value1;
                ++counter;
            }
            return result;
        }

    }

    static float abs(float x){
        if (x > 0.0F){
            return x;
        }
        return -x;
    }

    static float maximum(float x, float y){
        if (x < y){
            return y;
        }
        return x;
    }

    static float ulp(float var1) {
        float var2 = abs(var1);
        int var3 = -126;
        int var4 = 127;
        power(2.0F, (float)var3);
        power(2.0F, (float)var4);
        if (var2 <= power(2.0F, -127.0F)) {
            return 1.4E-45F;
        } else if (var2 > power(2.0F, 127.0F) - power(2.0F, 104.0F)) {
            return var2;
        } else {
            while(var4 - var3 > 1) {
                int var7 = (var4 + var3) / 2;
                float var8 = power(2.0F, (float)var7);
                if (var2 > var8) {
                    var3 = var7;
                } else {
                    var4 = var7;
                }
            }

            if (var2 == power(2.0F, (float)var4)) {
                return power(2.0F, (float)(var4 - 23));
            } else {
                return power(2.0F, (float)(var4 - 24));
            }
        }
    }

    /*static float ulp2Base(float x){
        float e_minima = -126.0F;
        float e_maxima = 127.0F;
        float precision = 23.0F;
        float current = 1.0F;
        int counter = 0;
        if (x == 0.0F){
            return power(2.0F, e_minima - precision + 1);
        }
        else {
            while (x > current){

                current *= 2.0F;
                ++counter;
            }
            return power(2.0F, maximum(counter, e_minima) - precision + 1);
        }

    }*/

    public static float erreurQuad(float x, float y){
        return (float) abs((abs(x)-abs(y)))/abs(y);
    }
    public static void main(String[] args) {
        //Decommenter la ligne si besoin sinon l'affichage sera merdique//
        /*File file = new File("/home/redouane/plot/test.txt");
        FileOutputStream fos = new FileOutputStream(file);
        PrintStream ps = new PrintStream(fos);
        System.setOut(ps);*/
        Cordic c = new Cordic();

        for (int i=0; i<58; i ++) {
            float rad = i * (Pi / 180);
            //System.out.format("Sin: %.1f (rad: %.6f) CORDIC: %.6f / java: %.6f %n", i, rad, c.sin(rad), Math.sin(rad) );
            //System.out.format("%.6f %.6f %.6f %n", rad, c.sin(rad), Math.sin(rad) );
            //System.out.format("%.6f %.6f %.6f %n", rad, c.atan(rad), Math.atan((double)rad));
            float err = (float) (Math.abs(c.atan(rad) - Math.atan(rad))/Math.ulp(Math.atan(rad)));
            System.out.format("rad = %.6f  ||  atanCordic (%.9f)  ||  atanJava(%.9f)  || erreur(%e) %n", rad, c.atan(rad), Math.atan((double)rad), err);

            //System.out.format("rad = %.6f  ||  sinCordic (%.9f)  ||  sinJava(%.9f)  || erreur(%e) %n", rad, c.asin(rad), Math.asin((double)rad), erreurQuad(c.asin(rad),(float) Math.asin((double)rad)));


        }

        /*int count = 0;
        for (int i = 130000000; i < 140000000; i++) {
            System.out.println(" my actan is : " + c.atan(i) + " ~~~~~~ Java actan is : " + Math.atan(i));

            float err = (float) (Math.abs(c.atan(i) - Math.atan(i))/Math.ulp(Math.atan(i)));
            if (err > 1){
                count++;
                System.out.println(" The error is : " + err);
            }

        }
        System.out.println(count);*/

        // float x = 11000 * Pi;
        // while (x > 0){
        //     x -= 100 * Pi;
        // }

        // System.out.println("x vaut : " + x);
        // System.out.format("%.6f %.6f", c.sin(x), Math.sin(x));
        // System.out.println();
        // System.out.format("%.6f %.6f", c.cos(x), Math.cos(x));

    }
}
