package util;

import java.util.Random;

public class GeradorDados {
    public static int[] gera(int n, long seed){
        Random r=new Random(seed);
        int[] v=new int[n];
        for(int i=0;i<n;i++) v[i]=r.nextInt(1_000_000_000);
        return v;
    }
}
