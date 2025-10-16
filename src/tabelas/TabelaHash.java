package tabelas;

import core.Registro;

public interface TabelaHash {
    int insere(Registro r);
    boolean contem(int codigo);

    int tamanho();
    boolean ocupado(int idx);
    int tamanhoLista(int idx);
}
