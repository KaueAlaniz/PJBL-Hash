import core.*;
import tabelas.*;
import util.*;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Locale;

public class MainExperimentos {

    
    static final int[] M = {1009, 10007, 100003};                 
    static final int[] N = {100_000, 1_000_000, 10_000_000};      
    static final long[] SEEDS = {42L, 43L, 44L};                  
    static final String[] HFS   = {"DIV", "MUL", "MIX"};          
    static final String[] TIPOS = {"ENC", "LIN", "QUAD", "DUP"};  

    static final boolean FORCAR_OA   = true;   
    static final double  LIMITE_CARGA = 0.85;  
    static final int REPS = 3;                 
    static final int PASSO_PROGRESSO = 100_000; 

    
    static final int TOTAL_CENARIOS = 27;
    static int contador = 0;
    

    private static TabelaHash criaTabela(String tipo, int m, HashFunction h) {
        switch (tipo) {
            case "ENC": return new TabelaEncadeada(m, h);
            case "LIN": return new TabelaLinear(m, h);
            case "QUAD": return new TabelaQuadratica(m, h, 1, 3);
            case "DUP": return new TabelaDuploHash(m, h);
            default: throw new IllegalArgumentException("tipo errado: " + tipo);
        }
    }

    private static HashFunction escolheHF(String nome) {
        switch (nome) {
            case "DIV": return funcoesHash.divisao;
            case "MUL": return funcoesHash.multiplicativa;
            case "MIX": return funcoesHash.mix64;
            default: throw new IllegalArgumentException("hash errada: " + nome);
        }
    }

    public static void main(String[] args) throws Exception {
        new File("out").mkdirs();

        try (PrintWriter pw = new PrintWriter(
                Files.newBufferedWriter(
                        Paths.get("out/resultados.csv"),
                        StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING
                ),
                true
        )) {
            pw.println("tipo;hash;m;n;seed;tempoIns_ms;tempoBusca_ms;colisoes;top1;top2;top3;gapMin;gapMax;gapMed");

            for (int ni = 0; ni < N.length; ni++) {
                int n = N[ni];
                long seed = SEEDS[ni];
                int[] dados = GeradorDados.gera(n, seed);

                for (int m : M) {
                    for (String hf : HFS) {
                        HashFunction func = escolheHF(hf);

                        
                        contador++;

                        for (String tipo : TIPOS) {
                            boolean ehOA = !tipo.equals("ENC");

                            
                            if (ehOA && n >= m) {
                                System.out.printf("[%d/%d] N/A (OA impossivel) -> tipo=%s hash=%s m=%d n=%d%n",
                                        contador, TOTAL_CENARIOS, tipo, hf, m, n);
                                continue;
                            }

                            
                            if (!FORCAR_OA && ehOA && n > (int) (LIMITE_CARGA * m)) {
                                System.out.printf("[%d/%d] Skip (OA carga alta) -> tipo=%s hash=%s m=%d n=%d%n",
                                        contador, TOTAL_CENARIOS, tipo, hf, m, n);
                                continue;
                            }

                            
                            long somaInsNs = 0, somaBuscaNs = 0, somaCol = 0;
                            int bestTop1 = 0, bestTop2 = 0, bestTop3 = 0, bestGapMin = 0, bestGapMax = 0;
                            double bestGapMed = 0;

                            for (int r = 1; r <= REPS; r++) {
                                System.out.printf("[%d/%d] Rodando: tipo=%s hash=%s m=%d n=%d (rep %d/%d)%n",
                                        contador, TOTAL_CENARIOS, tipo, hf, m, n, r, REPS);

                                TabelaHash tab = criaTabela(tipo, m, func);

                                
                                Relogio rel = new Relogio();
                                rel.comeca();
                                long colisoes = 0;
                                if (n >= 500_000) {
                                    int c = 0;
                                    for (int x : dados) {
                                        colisoes += tab.insere(new Registro(x));
                                        if (++c % PASSO_PROGRESSO == 0) {
                                            System.out.printf("  inseridos %d/%d%n", c, n);
                                        }
                                    }
                                } else {
                                    for (int x : dados) colisoes += tab.insere(new Registro(x));
                                }
                                long insNs = rel.tempoNano();

                                
                                rel.comeca();
                                if (n >= 500_000) {
                                    int c = 0;
                                    for (int x : dados) {
                                        tab.contem(x);
                                        if (++c % PASSO_PROGRESSO == 0) {
                                            System.out.printf("  buscados %d/%d%n", c, n);
                                        }
                                    }
                                } else {
                                    for (int x : dados) tab.contem(x);
                                }
                                long buscaNs = rel.tempoNano();

                                
                                int t1 = 0, t2 = 0, t3 = 0;
                                for (int i = 0; i < tab.tamanho(); i++) {
                                    int len = tab.tamanhoLista(i);
                                    if (len > t1) { t3 = t2; t2 = t1; t1 = len; }
                                    else if (len > t2) { t3 = t2; t2 = len; }
                                    else if (len > t3) { t3 = len; }
                                }
                                int[] gap = util.AnalisaGap.gaps(tab);
                                double gapMed = gap[2] / 1000.0;

                                somaInsNs += insNs;
                                somaBuscaNs += buscaNs;
                                somaCol += colisoes;
                                bestTop1 = t1; bestTop2 = t2; bestTop3 = t3;
                                bestGapMin = gap[0]; bestGapMax = gap[1]; bestGapMed = gapMed;
                            }

                            double insMs = (somaInsNs / (double) REPS) / 1e6;
                            double buscaMs = (somaBuscaNs / (double) REPS) / 1e6;
                            long colMed = somaCol / REPS;

                            pw.printf(Locale.US,
                                    "%s;%s;%d;%d;%d;%.3f;%.3f;%d;%d;%d;%d;%d;%d;%.3f\r\n",
                                    tipo, hf, m, n, seed,
                                    insMs, buscaMs, colMed,
                                    bestTop1, bestTop2, bestTop3,
                                    bestGapMin, bestGapMax, bestGapMed
                            );
                            pw.flush();
                        }
                    }
                }
            }
        }

        System.out.println("✅ Resultados salvos em out/resultados.csv");
    }
}
