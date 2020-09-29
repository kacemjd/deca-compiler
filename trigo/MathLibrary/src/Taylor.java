import javax.crypto.MacSpi;
import java.awt.*;

public class Taylor {

    public methods m = new methods();

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

    public int factoriel(int n){
        if (n == 0){
            return 1;
        }
        return n * factoriel(n - 1);
    }

    public float coefTaylorSinus(int n, float x){
        return (float)(m.power(-1, n) * m.power(x, 2*n + 1)/factoriel(2*n+1));
    }

    public float taylorSinus(float x, int n){
        float s = 0;
        for (int i = 0; i < n; i++) {
            s += coefTaylorSinus(i, x);
        }
        return s;
    }

    public float coefTaylorCosinus(int n, float x){
        return (float)(Math.pow(-1, n) * Math.pow(x, 2*n)/factoriel(2*n));
    }

    public float taylorCosinus(float x, int n){
        float s = 0;
        for (int i = 0; i < n; i++) {
            s += coefTaylorCosinus(i, x);
        }
        return s;
    }

    public float coefTaylorArctan(int n, float x){
        return (float)(Math.pow(-1, n) * Math.pow(x, 2*n + 1)/(2*n + 1));
    }

    public float taylorArctan(float x, int n){
        float s = 0;
        for (int i = 0; i < n; i++) {
            s += coefTaylorArctan(i, x);
        }
        return s;
    }

    public float coefTaylorArcsin(int n, float x){
        return (float)(Math.pow(-1, n) * Math.pow(x, 2*n + 1) * factoriel(2*n)/((2*n+1) * Math.pow(2, 2*n) * Math.pow(factoriel(n), 2)));
    }

    public float taylorArcsin(float x, int n){
        float s = 0;
        for (int i = 0; i < n; i++) {
            s += coefTaylorArcsin(i, x);
        }
        return s;
    }

}
