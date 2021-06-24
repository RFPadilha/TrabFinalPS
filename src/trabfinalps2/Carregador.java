package trabfinalps2;
import java.io.*;
import java.util.*;

public class Carregador extends Instrucoes{
    private Map<String, Integer> enderecos;
    private Memoria memoria;
    
    public Carregador(){
        enderecos = new HashMap<>();
    }//Carregador
    
    
    public Memoria carrega(String arq) throws FileNotFoundException, IOException {
        int contador; 
        int contador2; 
        int aux;
        boolean fim = false; // indica o stop
        FileWriter arqObj = new FileWriter("arquivo_carregado.txt"); // Arquivo criado pelo carregado
        String[] linhas = arq.split("\n"); // divide arquivo de entrada em linhas
        String[] linha_unica; // usado para dividir a linha em palavras
        String palavra;
        int palavra_inteiro; // Valor inteiro da palavra
        int label1; // Valores dos labels
        int label2 = 0; // Valores dos labels
        int[] location_counter = new int[linhas.length]; // localização do inicio de cada linha
        memoria = new Memoria(); // Memoria utilizada
        
        // atualiza as localizações
        aux = 0;
        for(contador = 0; contador < location_counter.length; contador++){
            location_counter[contador] = aux;
            linha_unica = linhas[contador].split("\\s+");
            aux += linha_unica.length;
        }// for
        
        // atravessa o código o executando
        for(contador = 0; contador < linhas.length; contador++){
            // Divide as opções das linhas
            linha_unica = linhas[contador].split("\\s+");

            // Resultado das operações
            int resultado;
            
            // Chegou no fim
            if(linha_unica.length == 1)
                break;
            
            
            // pegar a primeira parte da instrução
            palavra = linha_unica[1];
            palavra_inteiro = Integer.parseInt(palavra, 10);
            
            // Maior que 2000, é endereço
            if(palavra_inteiro >= 2000){
              // Se o endereço não foi utilizado, inicializa com valor 0
              if(enderecos.get(palavra) == null){
                  enderecos.put(palavra, 0);
              }
              // valor 1 recebe valor do endereço
              label1 = enderecos.get(palavra);
            }//if
            // Entre 2000 e 7, é valor
            else if (palavra_inteiro >= 8){
                label1 = palavra_inteiro;
            }// else if
            // Registradores
            else{
                palavra = memoria.getD(palavra_inteiro);
                label1 = Integer.parseInt(palavra, 2);
            }//else
            
            // Se a operação usar 2 operadores, pega esses valores
            if(linha_unica.length > 2){
                // pegar a primeira parte da instrução
                palavra = linha_unica[2];
                palavra_inteiro = Integer.parseInt(palavra, 10);

                // Maior que 2000, é endereço
                if(palavra_inteiro >= 2000){
                  // Se o endereço não foi utilizado, inicializa com valor 0
                  if(enderecos.get(palavra) == null){
                      enderecos.put(palavra, 0);
                  }
                  // valor 1 recebe valor do endereço
                  label2 = enderecos.get(palavra);
                }
                // Entre 2000 e 7, é valor
                else if (palavra_inteiro >= 8){
                    label2 = palavra_inteiro;
                }
                // Registradores
                else{
                    palavra = memoria.getD(palavra_inteiro);
                    label2 = Integer.parseInt(palavra, 2);
                }//else
            }//if
            
            
            // Decide qual operação vai ser utilizada
            switch (linha_unica[0]){
                // ADD
                case "0010":
                            resultado = add(label1, label2);
                            entrega_resultado(resultado, palavra_inteiro);
                            break;
                // BR
                case "00":
                            resultado = br(label1);
                            entrega_resultado(resultado, palavra_inteiro);
                            break;
                // BRNEG
                case "101":
                            if(label1<0){
                                resultado = br(label1);
                                entrega_resultado(resultado, palavra_inteiro);
                            }
                            break;
                            
                // BRPOS
                case "01":
                            if(label1>0){
                                resultado = br(label1);
                                entrega_resultado(resultado, palavra_inteiro);
                            }
                            break;
                //BRZERO
                case "100":
                            if(label1==0){
                                resultado = br(label1);
                                entrega_resultado(resultado, palavra_inteiro);
                            }
                            break;
                // CALL
                case "10101":
                            resultado = call(label1);
                            entrega_resultado(resultado, palavra_inteiro);
                            break;
                // COPY
                case "10011":
                            resultado = copy(label1, label2);
                            entrega_resultado(resultado, palavra_inteiro);
                            break;
                // DIVIDE
                case "10000":
                            resultado = divide(label1, label2);
                            entrega_resultado(resultado, palavra_inteiro);
                            break;
                // LOAD
                case "11":
                            resultado = load(label1);
                            entrega_resultado(resultado, palavra_inteiro);
                            break;
                // MULT
                case "10100":
                            resultado = mult(label1, label2);
                            entrega_resultado(resultado, palavra_inteiro);
                            break;
                // READ
                case "010010":
                            resultado = read(label1);
                            entrega_resultado(resultado, palavra_inteiro);
                            break;
                // RET
                case "10110":
                            resultado = ret(label1);
                            entrega_resultado(resultado, palavra_inteiro);
                            break;
                // STOP
                case "10001":
                            stop();
                // STORE
                case "111":
                            resultado = store(label1, label2);
                            entrega_resultado(resultado, palavra_inteiro);
                            break;
                // SUB
                case "110":
                            resultado = sub(label1, label2);
                            entrega_resultado(resultado, palavra_inteiro);
                            break;
            }// switch
            
            if(fim){
                break;
            }//if
        }//for
        
        for(contador = 0; contador < linhas.length; contador++){
            linha_unica = linhas[contador].split("\t");
            for(contador2 = 0; contador2 < linha_unica.length; contador2++){
                if(contador2 == 0){
                    arqObj.write(linha_unica[contador2] + " ");
                }
                else{
                    int x = Integer.parseInt(linha_unica[contador2], 10);
                    arqObj.write(Integer.toBinaryString(x) + " ");
                }
            }
            arqObj.write("\n");
        }//for
        
        arqObj.close();
        // retorna a memória depois da execução
        conserta_memoria();
        return memoria;
    }//carrega
    
    public void entrega_resultado(int resultado, int palavra){
        String x = Integer.toBinaryString(resultado);
        // Endereços
        if (palavra >= 8){
            enderecos.put(Integer.toBinaryString(palavra), resultado);
        }//if
        // Registradores
        else{
            memoria.setD(palavra, x);
        }
        
    }//entrega_resultado
    
    public void imprime_memoria(Memoria m){
        int i;
        String x;
        for(i = 0; i < 8; i++){
            x = m.getD(i);
            System.out.println("D" + i + ": " + Integer.parseInt(x, 2));
        }
    }//imprime_memoria
    
    public void conserta_memoria(){
        int i;
        String x;
        for(i = 0; i < 8; i++){
            x = memoria.getD(i);
            while(x.length() <= 16){
                x = "0" + x;
            }//while
            
            memoria.setD(i, x);
        }//for
    }
}//Carregador