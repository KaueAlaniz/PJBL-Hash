package core;

public class Relogio {
    private long inicio;
    public void comeca(){ inicio = System.nanoTime(); }
    public long tempoNano(){ return System.nanoTime() - inicio; }
}
