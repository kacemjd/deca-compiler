public class TestrangeReduction {
    static ChebychevValues values = new ChebychevValues();

    static int getIndexActan(float value){
        int i = 1;
        int j = 1;
        while (i < 9){
            if(value < values.ChebyValuesActan(i) && values.ChebyValuesActan(i - 1) < value){
                System.out.println("break i : " + i);
                j = i;
                //j = i;
                //break;
            }
            //System.out.println(" i : " + i);
            i += 1;
        }
        //System.out.println(" j : " + j);
        if (j > 1){
            return j;
        }
        return 8;

    }
    public static void main(String[] args) {
        rangeReduction reduction = new rangeReduction();
        /*for (int i = 0; i < 3000; i++) {
            float n = (float) Math.pow(2, i);
            float first = reduction.FirstReductionSin(i * (float)Math.PI);
            float second = reduction.SecondReduction(i * (float)Math.PI);
            System.out.println("My reduction : " + first + " ~~~ Ali reduction : " + second);
            //System.out.println(Math.cos(first) + " " + Math.cos(second) + " " + Math.cos(2* Pi));
            //System.out.println(Math.abs(Math.cos(first) - Math.cos(2 *Pi))/Math.ulp(Math.cos(2 * Pi)));
        }*/
        //System.out.println(2/3);
        for (int i = 0; i < 127; i++) {
            float x = (float)Math.pow(2, i);
            int k = getIndexActan(x);
            System.out.println(i + " " + k);
            //System.out.println(i);
        }
        //float x = (float)Math.pow(2, 10);
        //System.out.println("the result is : " + getIndexActan(x));

    }
}
