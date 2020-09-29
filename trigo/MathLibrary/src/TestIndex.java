public class TestIndex {
    public static methods m = new methods();
    public static final float Pi = 3.1415926f;

    static class couple {
        int x;
        int y;
    }

    static float breakPoint(int j, int k){
        return m.power(2, -j)*(1 + k/8);
    }

    static float BPOptVal(float value){
        float BP = breakPoint(1, 0);
        int j = 1;
        int k = 0;
        while (j < 5){
            while (k < 8){
                if(m.abs(BP - value) > m.abs(breakPoint(j, k) - value)){
                    BP = breakPoint(j, k);

                }
                k += 1;
            }
            k = 0;
            j += 1;
        }

        return BP;
    }


    static couple BPOptValIndex(float value){
        couple c = new couple();
        float BP = breakPoint(1, 0);
        int temoin = 0;
        int j = 1;
        int k = 0;
        while (j < 5){
            while (k < 8){
                if(m.abs(BP - value) > m.abs(breakPoint(j, k) - value)){
                    temoin = 1;
                    c.x = j;
                    c.y = k;
                    BP = breakPoint(j, k);
                }
                //System.out.println(j + " " + k);
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

    public static void main(String[] args) {
        for (float i = (Pi/64); i < 2* Pi; i+=0.01) {
            float x = (float)Math.sin(BPOptVal(i));
            couple c = new couple();
            c = BPOptValIndex(i);
            System.out.println(" la valeur est : " + x + " ~~~~ le j,k correspondant est : " + c.x + " " + c.y + " ~~~~ la valeur correspondant a j,k est : "+ Math.sin(breakPoint(c.x, c.y)));
        }
    }
}
