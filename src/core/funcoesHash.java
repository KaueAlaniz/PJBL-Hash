package core;

public final class funcoesHash {

    // Divisão
    public static final HashFunction divisao = (key, m) -> {
        int k = key & 0x7fffffff;
        return k % m;
    };

    // Multiplicativa
    public static final HashFunction multiplicativa = (key, m) -> {
        double A = (Math.sqrt(5.0) - 1.0) / 2.0; // ~0.618
        int k = key & 0x7fffffff;
        double frac = (k * A) % 1.0;
        return (int)(m * frac);
    };

    // Mix 64-bit simples
    public static final HashFunction mix64 = (key, m) -> {
        long z = (long)key ^ 0x9e3779b97f4a7c15L;
        z = (z ^ (z >>> 30)) * 0xbf58476d1ce4e5b9L;
        z = (z ^ (z >>> 27)) * 0x94d049bb133111ebL;
        z = z ^ (z >>> 31);
        if(z < 0) z = -z;
        return (int)(z % m);
    };

    private funcoesHash() {} // construtor privado só pra evitar instanciar
}
