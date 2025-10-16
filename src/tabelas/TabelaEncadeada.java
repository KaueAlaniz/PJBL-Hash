package tabelas;

import core.HashFunction;
import core.Registro;

public class TabelaEncadeada implements TabelaHash {
    private static class No {
        int chave; No prox;
        No(int c, No p){ chave=c; prox=p; }
    }
    private final No[] baldes;
    private final int m;
    private final HashFunction h;

    public TabelaEncadeada(int m, HashFunction h){
        this.m=m; this.h=h;
        baldes=new No[m];
    }

    @Override
    public int insere(Registro r){
        int b=h.hash(r.codigo,m);
        No atual=baldes[b];
        if(atual==null){ baldes[b]=new No(r.codigo,null); return 0; }
        int tam=0;
        while(atual.prox!=null){ atual=atual.prox; tam++; }
        atual.prox=new No(r.codigo,null);
        return tam+1;
    }

    @Override
    public boolean contem(int codigo){
        int b=h.hash(codigo,m);
        No cur=baldes[b];
        while(cur!=null){
            if(cur.chave==codigo) return true;
            cur=cur.prox;
        }
        return false;
    }

    @Override public int tamanho(){ return m; }
    @Override public boolean ocupado(int idx){ return baldes[idx]!=null; }
    @Override public int tamanhoLista(int idx){
        int len=0; No cur=baldes[idx];
        while(cur!=null){ len++; cur=cur.prox; }
        return len;
    }
}
