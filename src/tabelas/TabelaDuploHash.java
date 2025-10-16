package tabelas;

import core.HashFunction;
import core.Registro;

public class TabelaDuploHash implements TabelaHash {
    private final int m;
    private final int[] tabela; // -1 = vazio
    private int usados = 0;
    private final HashFunction h; // hash base (h0)

    public TabelaDuploHash(int m, HashFunction h){
        this.m = m;
        this.h = h;
        this.tabela = new int[m];
        for(int i=0;i<m;i++) tabela[i] = -1;
    }

    private int h2(int key){
        int k = key & 0x7fffffff;
        // m é primo (1009, 10007, 100003), então 1 + (k % (m-1)) é coprimo de m
        return 1 + (k % (m - 1));
    }

    @Override
    public int insere(Registro r){
        if(usados >= m) throw new IllegalStateException("tabela cheia");
        int k = r.codigo;
        int h0 = h.hash(k, m);
        int step = h2(k);
        int col = 0;
        for(int i=0;i<m;i++){
            int pos = (int)((h0 + (long)i * step) % m);
            if(tabela[pos] == -1){
                tabela[pos] = k;
                usados++;
                return col;
            } else {
                col++;
            }
        }
        return col;
    }

    @Override
    public boolean contem(int codigo){
        int h0 = h.hash(codigo, m);
        int step = h2(codigo);
        for(int i=0;i<m;i++){
            int pos = (int)((h0 + (long)i * step) % m);
            int cur = tabela[pos];
            if(cur == -1) return false;
            if(cur == codigo) return true;
        }
        return false;
    }

    @Override public int tamanho(){ return m; }
    @Override public boolean ocupado(int idx){ return tabela[idx] >= 0; }
    @Override public int tamanhoLista(int idx){ return ocupado(idx) ? 1 : 0; }
}
