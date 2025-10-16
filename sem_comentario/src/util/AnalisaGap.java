package util;

import tabelas.TabelaHash;

public class AnalisaGap {
    public static int[] gaps(TabelaHash t){
        int m=t.tamanho();
        int min=Integer.MAX_VALUE,max=0,soma=0,qtd=0;
        int inicio=-1;
        for(int i=0;i<m;i++) if(t.ocupado(i)){inicio=i;break;}
        if(inicio==-1) return new int[]{0,0,0};
        int i=(inicio+1)%m, atual=0;
        while(i!=inicio){
            if(!t.ocupado(i)){atual++;}
            else{
                min=Math.min(min,atual);
                max=Math.max(max,atual);
                soma+=atual; qtd++; atual=0;
            }
            i=(i+1)%m;
        }
        if(qtd==0) return new int[]{0,0,0};
        int mediaMil=(int)Math.round((soma*1000.0)/qtd);
        return new int[]{min,max,mediaMil};
    }
}
