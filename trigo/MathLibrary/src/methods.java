public class methods {

    static float racine(float x){
        if (x == 0) {
            return 0.0f;
        }
        float result = x;
        int i = 0;

        while (i < 100){
            result = 0.5f*(result + x/result);
            i = i + 1;
        }

        return result;
    }
    static float power(float value1, float value2){
        float counter = 0.0F;
        float result = 1.0F;

        if (value2 < 0.0F){
            float value4 = - value2;
            while (value4 > counter){
                result *= 1/value1;
                ++counter;

            }
            return result;
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

    static float ulp(float x){
        int e_min = -126;
        int e_max = 127;

        if (abs(x) <= power(2.0F, -127.0F)) {
            return 1.4E-45F;
        }

        else if (abs(x) > power(2.0F, 127.0F) - power(2.0F, 104.0F)) {
            return abs(x);
        }

        else {
            while(e_max - e_min > 1) {

                int median = (e_max + e_min) / 2;
                float y = power(2.0F, median);

                if (abs(x) > y) {
                    e_min = median;
                }

                else {
                    e_max = median;
                }
            }

            if (abs(x) == power(2.0F, e_max)) {
                return power(2.0F, e_max - 23);
            }

            else {
                return power(2.0F, e_max - 24);
            }
        }
    }

}
