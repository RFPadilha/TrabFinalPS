package trabfinalps2;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class ProcessadorMacros {

    private ArrayList<Macro> macros;
    private ArrayList<Macro> chamadas;
    private ArrayList<Label> labelsMacros;

    private final String input;

    public ProcessadorMacros(String input) {
        this.input = input;
        this.macros = new ArrayList<>();
        this.chamadas = new ArrayList<>();
    }

    public void processa() throws FileNotFoundException, IOException {
        // PRIMEIRA PASSAGEM
        try {
            // Lê arquivo de entrada
            BufferedReader bf = new BufferedReader(new FileReader(input + ".txt"));
            String linha;
            String[] code;
            
            linha = bf.readLine();//lê linha a linha
            
            while (linha != null) {//será null somente ao fim do arquivo de input
                linha = linha.replaceAll(";.*", "");// Remove os comentários da linha se houver
                code = linha.split("\\s+");// separa a linha quando encontra qualquer quantidade de espaços
                
                if (!(linha.contains("MACRO"))) {// Se encontrar a definicao de uma macro
                    linha = bf.readLine();
                } else {
                    bf.mark(1000000);//marca onde a macro começou a ser definida
                    String nomeMacro = code[0];//define nome da macro

                    ArrayList<String> parametros = new ArrayList<>();// Cria uma lista para os parametros
                    
                    Pattern p;//variáveis usadas para encontrar e definir padrões de detecção
                    Matcher m;

                    while (!(linha.contains("MEND"))) {
                        // Remove os comentários da linha se houver
                        linha = linha.replaceAll(";.*", "");
                        
                        p = Pattern.compile("&");// Procura por \ seguido de digito (sintaxe dos parametros), \d indica que procuramos por dígitos 0-9
                        m = p.matcher(linha);//verifica padrão
                        while (m.find()) {//encontra próxima ocorrência do padrão
                            String[] a;
                            a = m.group().split("\n");//a recebe padrões encontrados, separados por line-break
                            for (int i = 0; i <= m.groupCount(); i++) {// Adiciona todos parametros na lista
                                int s = 0;
                                // procura se ja existe o parametro
                                for(String parametro : parametros){
                                    if(parametro.equals(a[i])){
                                        s = 1;
                                        break;
                                    }
                                }
                                // Caso nao exista, adiciona
                                if(s == 0)
                                    parametros.add(a[i]);
                            }
                        }
                        linha = bf.readLine();//lê próxima linha
                    }
                    
                    
                    Macro novo = new Macro(nomeMacro, parametros.size());//define macro nova com o número certo de parâmetros
                    
                    bf.reset();// Volta para o inicio da definicao da macro, onde chamamos "bf.mark()"
                    linha = bf.readLine();
                    while (!(linha.contains("MEND"))) {//adiciona instruções à definição da macro
                        linha = linha.replaceAll(";.*", "");// Remove os comentários da linha se houver
                        novo.addInstrucao(linha);
                        linha = bf.readLine();
                    }
                    // Adiciona macro na lista de definicoes
                    this.macros.add(novo);
                }
            }
            bf.close();//encerra leitura de arquivo
        } catch (FileNotFoundException e) {
            System.err.printf("Arquivo não encontrado: %s.\n", e.getMessage());//se arquivo que tentou ser lido não existe, mostra erro
        } 

        
        
        
        
        
        
        // SEGUNDA PASSAGEM
        try {
            BufferedReader bf2 = new BufferedReader(new FileReader(input + ".txt"));//lê arquivo de entrada
            FileWriter arqSaida = new FileWriter("MacroProcessada.txt");// Cria arquivo de saída
            String linha2;
            String[] code2;//vetor que armazena instruções, registradores, labels e chamadas de macro
            int f;
            linha2 = bf2.readLine();//lê input linha a linha

            while (linha2 != null) {//enquanto não chegar no fim do arquivo
                
                linha2 = linha2.replaceAll(";.*", "");// remove comentários
                
                code2 = linha2.split("\\s+");// Quebra a linha em qualquer quantidade de espaços
                f = 0;
                
                if (linha2.contains("MACRO")) {// se linha contem definicao de macro
                    while (!(linha2.contains("ENDM"))) {// ignora definicao de macro inteira
                        linha2 = bf2.readLine();
                    }
                    linha2 = bf2.readLine();//le depois da definição da macro
                } else {
                    for (Macro macro : macros) {// a cada linha, testa se existe chamada de macro
                        if (macro.getNome().equals(code2[0])) {//encontra macro se já tiver sido definida
                            f = 1;
                            Macro nova;// Cria um novo macro
                            nova = new Macro(macro.getNome(), macro.getNumParametros());//inicializa
                            nova.setInstrucoes(macro.getInstrucoes());//define instruções
                            this.chamadas.add(nova);// Adiciona a lista de chamadas de macro

                            nova.addParametros(linha2);// adiciona parametros da chamada
                            nova.substituiParametros();// substitui parametros nas instrucoes

                            ArrayList<String> expansao;
                            expansao = nova.getInstrucoes();// Pega as instrucoes atualizadas da macro expandida

                            for (int i = 0; i < expansao.size(); i++) {// Escreve macros expandidas no arquivo de saida
                                arqSaida.write(expansao.get(i) + "\n");
                            }
                        }
                    }
                    
                    if (f == 0) {// se zero implica que nao houve uma chamada de macro
                        arqSaida.write(linha2 + "\n");
                    }
                    linha2 = bf2.readLine();// Le a proxima linha
                }
            }
            bf2.close();//encerra leitura de arquivo
            arqSaida.close();//encerra escrita de output
        } catch (FileNotFoundException e) {
            System.err.printf("Arquivo não encontrado: %s.\n", e.getMessage());
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }    
    }
}
