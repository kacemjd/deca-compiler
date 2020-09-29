public class Chebychev {
    static ChebychevValues values = new ChebychevValues();

    static class couple {
        int x;
        int y;
    }

    public static final float Pi = 3.1415926f;

    static methods m = new methods();
    static rangeReduction range = new rangeReduction();

    static float breakPoint(int j, int k){
        return m.power(2, -j)*(1 + k/8);
    }

    static couple BPOptVal(float value){
        couple c = new couple();
        float BP = breakPoint(1, 0);
        int temoin = 0;
        int j = 1;
        int k = 0;
        while (j < 5){
            while (k < 8){
                if(m.abs(BP - value) > m.abs(breakPoint(j, k) - value)){
                    temoin = 1;
                    BP = breakPoint(j, k);
                    c.x = j;
                    c.y = k;
                }
                k += 1;
            }
            k = 0;
            j += 1;
        }
        if (temoin == 0){
            c.x = 1;
            c.y = 0;
        }

        return c;
    }

    static float ChebychevAsin(float x){

        float c1 = 33142.80705762475181175671894f;
        float c2 = -128066.0512368817884381034778f;
        float c3 = 199867.794259025636248746017f;
        float c4 = -160898.1401115471412474213471f;
        float c5 = 70587.79646485083616338063247f;
        float c6 = -16296.65287133180160701231392f;
        float c7 = 1722.97933573217713648777444f;
        float c8 = -56.31103577261976331726284f;

        float q1 = 33142.8070576247518117630951f;
        float q2 = -133589.8524131525804115032837f;
        float q3 = 219647.0591318958772010675923f;
        float q4 = -188966.3339699968602415716419f;
        float q5 = 90565.54579921697109829452696f;
        float q6 = -23706.86441732617409892181527f;
        float q7 = 3057.97388270644406224989327f;
        float q8 = -148.55841631636781233532611f;
        float q9 = 1.0f;

        return (c1 * x + c2 * m.power(x, 3) + c3 * m.power(x, 5) + c4 * m.power(x, 7) + c5 * m.power(x, 9) + c6 * m.power(x, 11)+ c6 * m.power(x, 13) + c7 * m.power(x, 15) + c8 * m.power(x, 17))
                /(q1 + q2 * m.power(x, 2) + q3 * m.power(x, 4) + q4 * m.power(x, 6) + q5 * m.power(x, 8)+ q6 * m.power(x, 10) + q7 * m.power(x, 12) + q8 * m.power(x, 14) + q9 * m.power(x, 16));
    }

    static float ChebychevActan(float x){

        float c1 = 137.772398556992967072803545f;
        float c2 = 311.6092089800060900355248812f;
        float c3 = 243.2657933898208264206733648f;
        float c4 = 76.1179448121611233450964866f;
        float c5 = 8.1327059582002624490996398f;
        float c6 = 0.1342077928230059190697038f;
        float q1 = 137.7723985569929670728035493f;
        float q2 = 357.5333418323370790597777492f;
        float q3 = 334.8890942892012593679882767f;
        float q4 = 135.9227450335766811043244001f;
        float q5 = 22.23061618444266557469256796f;

        return (c1 * x + c2 * m.power(x, 3) + c3 * m.power(x, 5) + c4 * m.power(x, 7) + c5 * m.power(x, 9) + c6 * m.power(x, 11))
                /(q1 + q2 * m.power(x, 2) + q3 * m.power(x, 4) + q4 * m.power(x, 6) + q5 * m.power(x, 8));
    }

    static float ChebychevSinus(float r ){

        float a1 = -0.16666666666666666666421f;
        float a2 = 0.008333333333333312907f;
        float a3 = -0.0001984126983563939f;
        float a4 = 0.00000275566861f;

        return r + a1*m.power(r, 3) + a2*m.power(r, 5) + a3*m.power(r, 7)+ a4*m.power(r, 9);
    }

    static float ChebychevCosinus(float r ){

        float a1 = -0.49999999999999999999999995425696f;
        float a2 = 0.0416666666666666666660027496f;
        float a3 = -0.001388888888885759632f;
        float a4 = 0.0000248015872951505636f;
        float a5 = -0.000000275567182072f;

        return 1 + a1*m.power(r, 2) + a2*m.power(r, 4) + a3*m.power(r, 6) + a4*m.power(r, 8) + a5*m.power(r, 10);
    }


    static int getIndexActan(float value){
        int i = 1;
        while (i < 9){
            if(value < values.ChebyValuesActan(i) && values.ChebyValuesActan(i - 1) < value){
                return i;
            }
            i += 1;
        }
        return i;

    }
    static float arctanCheb(float value){
        float tanCoef = 0.09849140335716425f;
        float Pi32 = 0.09817477042468103f;
        if(value < 0){
            return -arctanCheb(-value);
        }
        else if (value >= 0 && value <= tanCoef){
            return ChebychevActan(value);
        }
        else {
            int i = getIndexActan(value);
            float x = values.otherActanVal(i - 1);
            float t = 1/x - (m.power(x, -2) + 1)/(m.power(x, -1) + value);
            return (2 * i - 2) * Pi32 + arctanCheb(t);
        }
    }

    static float arcsinCheb(float value){
        float newVal = value / m.racine(1 - m.power(value, 2));
        float result = arctanCheb(newVal);
        return result;
    }

    static float sinCheb(float value){
        if (value <= Pi/4 && value >= 0){

            if(m.abs(value) <= 0.03125f){
                return ChebychevSinus(value);
            }

            couple c = BPOptVal(value);
            float BP = breakPoint(c.x, c.y);
            float r = value - BP;
            return values.ChebyValuesSin(c.x, c.y)*ChebychevCosinus(r) + values.ChebyValuesCos(c.x,c.y)*ChebychevSinus(r);
        }

        else if (value < 0){
            return -sinCheb(-value);
        }

        else {
            if (value > Pi){
                float newValue = range.FirstReductionSin(value);
                return sinCheb(newValue);
            }

            else if (value > Pi/2){
                return cosCheb(Pi/2 - value);
            }

            else {
                return 2 * sinCheb(value/2) * cosCheb(value/2);
            }
        }

    }

    static float cosCheb(float value){

        if (value <= Pi/4 && value >= 0){
            if(m.abs(value) <= 0.03125f){
                return ChebychevCosinus(value);
            }

            couple c = BPOptVal(value);
            float BP = breakPoint(c.x, c.y);
            float r = value - BP;
            return values.ChebyValuesCos(c.x, c.y)*ChebychevCosinus(r) - values.ChebyValuesSin(c.x, c.y)*ChebychevSinus(r);
        }

        else if (value < 0){
            return cosCheb(-value);
        }

        else {
            if (value > 2* Pi){
                float newValue = range.FirstReductionCos(value);
                return cosCheb(newValue);
            }
            else if (value > Pi && value < 2*Pi){
                return -cosCheb(value - Pi);
            }

            else if (value > Pi/2){
                return sinCheb(Pi/2 - value);
            }

            else {
                return 1 - 2 * m.power(sinCheb(value/2), 2);
            }

        }

    }

    public static void main(String[] args) {
        int count = 0;
        for (float i = 0.4f; i < 1000; i+= 1) {
            //float j = (float) m.power(2, -i);
            float actan = arctanCheb(i);
            float realactan = (float)Math.atan(i);
            float err = Math.abs(actan - realactan)/Math.ulp(realactan);
            System.out.println(" my arctan is : " + actan + " ~~~~ Java arctan is : " + realactan);
            if (err > 1){
                count ++;
            }
            System.out.println(err);
        }
        System.out.println(count);

        //int count = 0;
        /*for (float i = -1.0f; i < 1.0f; i+= 0.01) {
            float asin = arcsinCheb(i);
            float realasin = (float)Math.asin(i);
            float err = Math.abs(asin - realasin)/Math.ulp(realasin);
            System.out.println(" my arctan is : " + asin + " ~~~~ Java arctan is : " + realasin);
            if (err > 1){
                count ++;
            }
            System.out.println(err);
        }
        System.out.println(count);*/

        /*for (float i = 0; i < 1.0/32.0; i+= 0.001) {
            float asin = ChebychevAsin(i);
            float realasin = (float)Math.asin(i);
            float err = Math.abs(asin - realasin)/Math.ulp(realasin);
            System.out.println(" my asin is : " + asin + " ~~~~ Java asin is : " + realasin);
            System.out.println(err);
        }*/

        /*for (float i = (Pi/64); i < 2* Pi; i+=0.01) {
            float mine = sin(i);
            float real = (float) Math.sin(i);
            float err = Math.abs(real - mine)/Math.ulp(real);
            System.out.println(" my sinus is : " + mine + " their sinus is : " + real);
            if (err > 5){
                count++;
                System.out.println(" the error is : " + err);
            }

        }
        System.out.println(count);*/

        /*for (float i = Pi/2; i < Pi/2 + 0.1; i+=0.001) {
            float mine = cosCheb(i);
            float real = (float) Math.cos(i);
            float err = Math.abs(real - mine)/Math.ulp(real);
            System.out.println(" my cosinus is : " + mine + " their cosinus is : " + real);
            if (err > 5){
                count++;
                System.out.println(" the error is : " + err);
            }

        }
        System.out.println(count);*/
        /*float target = 3.140625f;
        float x = (float)(10 * target);
        System.out.println(rangeReduction(x) + " " + target);*/

    }

}
