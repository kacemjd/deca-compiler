public class TestTaylor {
    private final float halfPi = 1.570796F;
    static final float Pi = 3.141593F;

    public void testSinus(int degre, int seuilUlp){
        Taylor method = new Taylor();
        int count = 0;
        for (float i = (float)-Math.PI/64; i < Math.PI/64; i+= 0.001) {

            float sin = method.taylorSinus(i, degre);
            float realsin = (float) Math.sin(i);
            float err = Math.abs(sin - realsin)/Math.ulp(realsin);
            System.out.println(" My sinus : "+ sin + " ~~~~ Real sinus : "+ realsin);
            System.out.println(" the error ulp is : " + err);

            if (err > seuilUlp){
                count += 1;
            }
        }
        System.out.println(count);
    }

    public void testCosinus(int degre, int seuilUlp){
        Taylor method = new Taylor();
        int count = 0;
        for (float i = (float)(halfPi - 0.1); i < halfPi + 0.1; i+= 0.01) {

            float cos = method.taylorCosinus(i, degre);
            float realcos = (float) Math.cos(i);
            float err = Math.abs(cos - realcos)/Math.ulp(realcos);
            System.out.println(" My cosinus : "+ cos + " ~~~~ Real cosinus : "+ realcos);


            if (err > seuilUlp){
                System.out.println(" the error ulp is : " + err);
                count += 1;
            }
        }
        System.out.println(count);
    }

    public void testActan(int degre, int seuilUlp){
        Taylor method = new Taylor();
        /**
         * Test for big / small numbers
         */

        int count = 0;
        for (float i = -0.4f; i < 0.4; i+=0.01) {
            //float j =(float)Math.pow(2,-i);
            float actan = method.taylorArctan(i, degre);
            float realactan = (float) Math.atan(i);
            float err = Math.abs(actan - realactan)/Math.ulp(realactan);
            System.out.println(" My actan : "+ actan + " ~~~~ Real actan : "+ realactan);
            System.out.println(" the error ulp is : " + err);

            if (err > seuilUlp){
                count += 1;
            }
        }
        /*for (float i = 0; i < 0.4; i+= 0.00001) {

            float actan = method.taylorArctan(i, degre);
            float realactan = (float) Math.atan(i);
            float err = Math.abs(actan - realactan)/Math.ulp(realactan);
            System.out.println(" My sinus : "+ actan + " ~~~~ Real sinus : "+ realactan);
            System.out.println(" the error ulp is : " + err);

            if (err > seuilUlp){
                count += 1;
            }
        }*/
        System.out.println(count);
    }

    public void testAsin(int degre, int seuilUlp){
        Taylor method = new Taylor();
        int count = 0;
        for (float i = 0; i < 1; i+= 0.01) {

            float asin = method.taylorArcsin(i, degre);
            float realasin = (float) Math.asin(i);
            float err = Math.abs(asin - realasin)/Math.ulp(realasin);
            System.out.println(" My sinus : "+ asin + " ~~~~ Real sinus : "+ realasin);
            System.out.println(" the error ulp is : " + err);

            if (err > seuilUlp){
                System.out.println("we are here : " + i);

            }
        }
        System.out.println(count);
    }

    public static void main(String[] args) {
        TestTaylor test = new TestTaylor();
        Taylor t = new Taylor();
        //test.testSinus(7,1);

        //test.testCosinus(7,1);
        //System.out.println(t.taylorCosinus(Pi/2, 7));

        test.testActan(7,2);

        //test.testAsin(33, 1);

    }
}
