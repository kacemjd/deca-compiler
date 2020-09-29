public class TestMethod {
    public static final float Pi = 3.1415926f;
    public static void main(String[] args) {
        methods m = new methods();
        /*int count = 0;
        for (int i = 0; i < Math.pow(2,32); i+=Math.pow(2,31)) {
            System.out.println("My ulp : " + m.ulp(i) + " ~~~~~ java ulp : " + Math.ulp(i));
            if (Math.abs(m.ulp(i) - Math.ulp(i)) > 0){
                count++;
            }
        }
        System.out.println(count);*/
        //System.out.println(Math.tan(Math.PI/32));
        //System.out.println(Pi/32);
        /*for (int i = 0; i < 9; i++) {
            System.out.println("if (i == " + i + ") {");
            System.out.println("    return " + Math.tan(2*i - Pi/32) + "f;");
            System.out.println("}");
        }*/

        /*for (int i = 0; i < 20; i++) {
            System.out.println("mine " + m.power(3, -i) + " java " + (float)Math.pow(3,-i));
        }*/
        for (float i = 0; i < 1; i+= 0.01) {
            float x = m.racine(i);
            float r = (float)Math.sqrt(i);
            float err = Math.abs(x - r)/Math.ulp(r);
            System.out.println(x + " " + r);
            System.out.println(err);



        }

    }
}
