package tabelas;

import core.HashFunction;
import core.Registro;

public class TabelaLinear implements TabelaHash {
    private final int m;
    private final int[] tabela; // -1 = vazio
    private int usados = 0;
    private final HashFunction h;

    public TabelaLinear(int m, HashFunction h){
        this.m = m;
        this.h = h;
        this.tabela = new int[m];
        for(int i=0;i<m;i++) tabela[i] = -1;
    }

    @Override
    public int insere(Registro r){
        if(usados >= m) throw new IllegalStateException("tabela cheia");
        int k = r.codigo;
        int h0 = h.hash(k, m);
        int col = 0;
        for(int i=0;i<m;i++){
            int pos = (h0 + i) % m;
            if(tabela[pos] == -1){
                tabela[pos] = k;
                usados++;
                return col;
            } else {
                col++;
            }
        }
        return col; // não deve chegar aqui
    }

    @Override
    public boolean contem(int codigo){
        int h0 = h.hash(codigo, m);
        for(int i=0;i<m;i++){
            int pos = (h0 + i) % m;
            int cur = tabela[pos];
            if(cur == -1) return false; // parou no vazio
            if(cur == codigo) return true;
        }
        return false;
    }

    @Override public int tamanho(){ return m; }
    @Override public boolean ocupado(int idx){ return tabela[idx] >= 0; }
    @Override public int tamanhoLista(int idx){ return ocupado(idx) ? 1 : 0; }
}
