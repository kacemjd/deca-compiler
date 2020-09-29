public class rangeReduction {

    static float Pi = 3.1415926f;
    static float restePi = 1.509957E-7f;
    static float reste2Pi = 9.592326932761353E-14f;
    static float pi2 = Pi/2;

    public float FirstReductionSin(float x){
        float C1 = 3.140625f;
        float C2 = 0.000967653589793f;
        int k = (int) (x /Pi);
        float result = x - k * C1;
        result = result - k * C2;
        if (k % 2 == 0){
            return result;
        }
        return -result;

    }

    public float FirstReductionCos(float x){
        float C1 = 3.140625f;
        float C2 = 0.000967653589793f;
        int k = (int) (x /Pi);
        float result = x - k * C1;
        result = result - k * C2;
        return result;
    }

    public float SecondReduction(float x){
        if (x > 0){
            while (pi2 < x){
                x -= Pi;
                x -= restePi;
                x -= reste2Pi;
            }
        }
        else if (x < 0){
            while (x < - pi2){
                x += Pi;
                x += restePi;
                x += reste2Pi;
            }
        }
        return x;
    }

}