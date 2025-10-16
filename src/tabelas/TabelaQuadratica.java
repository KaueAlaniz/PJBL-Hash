package tabelas;

import core.HashFunction;
import core.Registro;

public class TabelaQuadratica implements TabelaHash {
    private final int m;
    private final int[] tabela; // -1 = vazio
    private int usados = 0;
    private final HashFunction h;
    private final int c1, c2;

    public TabelaQuadratica(int m, HashFunction h, int c1, int c2){
        this.m = m;
        this.h = h;
        this.c1 = c1;
        this.c2 = c2;
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
            long step = (long)h0 + (long)c1*i + (long)c2*i*i;
            int pos = (int)(step % m);
            if(pos < 0) pos += m;
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
        for(int i=0;i<m;i++){
            long step = (long)h0 + (long)c1*i + (long)c2*i*i;
            int pos = (int)(step % m);
            if(pos < 0) pos += m;
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
