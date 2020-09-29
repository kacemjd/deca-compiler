public class finalTrigo {
    public static final float Pi = 3.1415926f;
    static Chebychev cheb = new Chebychev();
    static Taylor tayl = new Taylor();

    static float sin(float value){
        float seuil = Pi/64;
        if (value < seuil && value > -seuil){
            return tayl.taylorSinus(value, 7);
        }

        else {
            return cheb.sinCheb(value);
        }
    }

    static float cos(float value){
        float seuilU = 0.1f + Pi/2;
        float seuilD = -0.1f + Pi/2;

        if (value < seuilU  && value > seuilD){
            return tayl.taylorCosinus(value, 7);
        }

        else {
            return cheb.cosCheb(value);
        }
    }

    static float arctan(float value){
        if (value < 0.4f && value >-0.4f){
            return tayl.taylorArctan(value, 7);
        }

        else {
            return cheb.arctanCheb(value);
        }
    }

    static float arcsin(float value){
        return cheb.arcsinCheb(value);
    }


}

