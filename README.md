# PJBL-Hash

Disciplina: Resolução de Problemas Estruturados na Computação
Curso: Ciência da Computação
Professor: Andrey Cabral Meira
Alunos: Kaue Alaniz, Henry Mendes, Matheus Antonio, Rafael Maluf

Relatório

Uma tabela hash é uma estrutura de dados que associa chaves a valores de forma eficiente.
Ela funciona como um vetor em que o índice é calculado por uma função hash, responsável por espalhar as chaves pelo vetor.

Neste projeto, foi desenvolvido e analisado um sistema completo de implementação, experimentação e comparação entre diferentes funções hash e métodos de resolução de colisão, utilizando Java como linguagem base.
Foram aplicadas métricas automáticas de tempo e colisões (pelas classes Medidas e Relogio), e os resultados foram exportados para o arquivo resultados.csv.

Tipos de Tabelas Hash Utilizadas
 Tabela Hash Encadeada

Cada posição da tabela contém uma lista encadeada.
Quando ocorre uma colisão, o novo elemento é adicionado ao final da lista daquela posição.
Implementação: TabelaEncadeada.java

 Tabelas com Endereçamento Aberto

No endereçamento aberto, quando ocorre uma colisão, o algoritmo busca outra posição livre dentro do vetor, seguindo uma regra de sondagem específica.
Foram implementadas três variações:

Sondagem Linear — TabelaLinear.java

Sondagem Quadrática — TabelaQuadratica.java

Duplo Hash — TabelaDuploHash.java

A inclusão dessas três variações foi intencional, para tornar o estudo mais completo.
Com isso, o projeto conta com quatro tabelas no total: uma com encadeamento e três com endereçamento aberto.
O objetivo foi comparar o comportamento de diferentes estratégias de sondagem e observar como cada uma afeta o desempenho, as colisões e a distribuição das chaves dentro da tabela.

Funções Hash Implementadas

As funções foram implementadas em core/FuncoesHash.java e seguem três abordagens clássicas:

 Dobramento: divide a chave em partes, soma e tira o módulo pelo tamanho da tabela.

 Multiplicação: multiplica a chave por uma constante fracionária e usa a parte decimal como índice.

 Quadrado Médio: eleva a chave ao quadrado, pega os dígitos centrais e usa como índice.

Essas funções foram escolhidas por serem simples e apresentarem comportamentos diferentes de dispersão, o que permite analisar o impacto de cada uma no desempenho e nas colisões.

Configuração dos Experimentos

Os experimentos foram conduzidos pelo programa MainExperimentos.java, variando o tamanho das tabelas (m) e o tamanho dos conjuntos de dados (n):

Tamanho da Tabela (m)	Tamanho do Conjunto (n)
1.009 posições	100.000 registros
10.007 posições	1.000.000 registros
100.003 posições	10.000.000 registros

Essa variação segue uma proporção próxima de 1:10:100, permitindo observar o comportamento das tabelas em baixa, média e alta ocupação, analisando o impacto das colisões e do fator de carga.

Resultados e Gráficos

Os resultados foram gerados automaticamente e exportados para resultados.csv, sendo analisados por scripts auxiliares em util/AnalisaGap.java.

 Tempo de Inserção
<img width="1600" height="1000" alt="tempo_insercao_ms" src="https://github.com/user-attachments/assets/894feef3-c2f2-4c57-9f14-f5a9de85df7e" />

O encadeamento apresentou o menor tempo de inserção, já que não precisa procurar posições livres — apenas adiciona ao final da lista.
A função do Quadrado Médio teve melhor desempenho nesse contexto, pois produziu boa dispersão.

 Tempo de Busca
<img width="1600" height="1000" alt="tempo_busca_ms" src="https://github.com/user-attachments/assets/d31a6741-0252-4423-bfb2-2f107fa9ae75" />

O Encadeamento + Multiplicação obteve o melhor desempenho na busca, com listas menores e bem distribuídas.

 Número de Colisões
<img width="1600" height="1000" alt="colisoes" src="https://github.com/user-attachments/assets/cc60a277-dbba-4d38-95ea-7ac9acb82a0a" />

Os métodos de endereçamento aberto tiveram mais colisões.
O Encadeamento + Multiplicação foi o mais eficiente no geral, enquanto o Endereçamento Aberto + Multiplicação apresentou clusters e colisões mais frequentes.

 Maiores Listas Encadeadas

Com o aumento do volume de dados, as listas cresceram proporcionalmente.
As funções Multiplicação e Quadrado Médio mantiveram bom equilíbrio, enquanto Dobramento formou listas mais longas.

 Gaps no Endereçamento Aberto

A análise dos gaps (espaços vazios entre elementos) mostrou que:

Gaps muito pequenos → causam clusters e lentidão.

Gaps muito grandes → desperdiçam memória.

A função de Dobramento teve bom desempenho em tabelas menores, mas em conjuntos maiores as diferenças diminuíram.

Gráficos Principais

Os gráficos abaixo mostram os resultados experimentais.
As imagens completas estão disponíveis na pasta /img/ do repositório.

 Tempo de Inserção e Busca




 Colisões

Visão Geral dos Resultados
Métrica Principal	Melhor Combinação	Pior Combinação
Tempo de Inserção	Encadeamento + Quadrado Médio	Endereçamento Aberto + Multiplicação
Tempo de Busca	Encadeamento + Multiplicação	Endereçamento Aberto + Quadrado Médio
Menor Colisão	Encadeamento + Multiplicação	Endereçamento Aberto + Dobramento

De forma geral:

O Encadeamento foi o método mais eficiente em praticamente todos os cenários.

A função de Multiplicação foi a mais equilibrada entre simplicidade e desempenho.

A função de Dobramento apresentou a pior dispersão, resultando em mais colisões.

Conclusão

Com base nos resultados:

O encadeamento se mostrou mais robusto e estável em grandes volumes de dados.

A função de multiplicação teve o melhor equilíbrio entre desempenho e simplicidade.

O endereçamento aberto perde eficiência com alto fator de carga, devido à formação de clusters.

A função do quadrado médio apresentou bons resultados e dispersão consistente.

Essas análises ajudam a identificar a melhor estratégia conforme o contexto:

Para alta performance, recomenda-se Encadeamento + Multiplicação.

Para simplicidade e menor uso de memória, Endereçamento Aberto + Quadrado Médio é uma opção viável.
