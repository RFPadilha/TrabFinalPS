# Montador Completo
## Equipe:
- Gabriel Schubert Marten
- Luísa Borges Lima
- Rafael Nunes Siqueira
- Ricardo Ferreira Padilha

## Descrição:
O projeto tinha como objetivo construir um simulador completo de uma máquina virtual, construindo em cima do que foi feito no trabalho 1.

### Componentes:
- Macro: classe auxiliar para o processamento de macros
- Memoria: classe auxiliar para a manipulação de memória
- Instrucoes: classe auxiliar, contém definição de instruções
- Label: classe auxiliar, conta quantas vezes a label aparece
- Ligador: classe auxiliar, lê todos os arquivos de entrada e os salva como variáveis, também combina tabelas de simbolos
- Save: classe auxiliar para realizar o output através da interface
- TabelaDeUso: classe auxiliar para manipulação de tabela
- TabelaDefinicoes: classe auxiliar para manipulação de tabela
- TabelaSimbolosGlobais: classe auxiliar para manipulação de tabela
----------------
- ProcessadorMacros: primeiro objeto a ser chamado, define macros baseado no arquivo de entrada
- Carregador: executa instruções e manipula memória
- Montador: Utiliza das tabelas e das macros processadas para gerar um arquivo objeto, onde existem somente binários
----------------
- Principal: o objeto que usa todos os outros para executar o programa inteiro
- JFrame: definição da interface
