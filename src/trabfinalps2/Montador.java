package trabfinalps2;

import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class Montador {

    private int locationCounter; // contador de localizacao
    private Map<String, Integer> tabSimbolo; // tabela de simbolos
    private Map<String, Integer> labels; // rotulos
    private ArrayList<TabelaDefinicoes> definicoes = new ArrayList<> ();
    private ArrayList<TabelaDeUso> uso = new ArrayList<> ();


    public Montador() {
        locationCounter = 0;
        tabSimbolo = new HashMap<>();
        labels = new HashMap<>();
    }
  
    public void monta(String arq) throws FileNotFoundException, IOException {
        BufferedReader bf = new BufferedReader(new FileReader(arq + ".txt"));//lê arquivo de entrada
        FileWriter arqObj = new FileWriter("objeto" + "_" + arq + ".txt");//cria arquivo de saída

        String linha = bf.readLine();//lê linha a linha

        String[] idInstrucao = linha.split("\\s+");//separa linha por qualquer quantidade de espaços

        TabelaDefinicoes tempDef;// Cria tabelas temporarias, usadas para adicionar facilmente às permanentes
        TabelaDeUso tempUse;
        
        while (!idInstrucao[0].equals("START")) {// Enquanto nao encontra o inicio das instrucoes
            if(idInstrucao[0].equals("EXTR")){ //Declara label como símbolo externo referenciado no módulo
               tempDef = new TabelaDefinicoes();
               tempDef.setSimbolo(idInstrucao[1]);
               tempDef.setRelocabilidade("R");
               tempDef.setEndereco(Integer.parseInt(idInstrucao[3].substring(1)));
               
               definicoes.add(tempDef);// Guarda na tabela de definicoes
               labels.put(idInstrucao[1], Integer.parseInt(idInstrucao[3].substring(1)));// Guarda no mapa de rotulos
            }
            else if (idInstrucao[0].equals("EXTDEF")){ // Declara símbolo global para acesso externo, isto é, para referências a partir de outros módulos
               tempUse = new TabelaDeUso();
               tempUse.setSimbolo(idInstrucao[1]);
               tempUse.setOperacao("+");
               tempUse.setRelocabilidade("R");
               tempUse.setLocationCounter(0);
               
               uso.add(tempUse);// Guarda na tabela de uso
               labels.put(idInstrucao[1], Integer.parseInt("0"));// Guarda no mapa de rotulos
            }
            
            linha = bf.readLine();//lê próxima linha
            idInstrucao = linha.split("\\s+");//separa linha por um ou mais espaços
        }//encerrado enquanto não encontrar início das instruções
        
        // atualiza locationCounter de acordo com o endereco inicial das instrucoes
        idInstrucao[1] = idInstrucao[1].substring(1);
        locationCounter = Integer.parseInt(idInstrucao[1]);
        
        linha = bf.readLine();// Le primeira instrucao

        idInstrucao = linha.split("\\s+");//separa linha por um ou mais espaços
       
        
        while (!idInstrucao[0].equals("END")) {//até terminar as instruções

            verificaOperacao(idInstrucao[0], arqObj);//identifica operação e quantidade de operandos
            
            
            for(int i=0;i<uso.size();i++){//para todos os itens na tabela de uso
                if(uso.get(i).getSimbolo().equals(idInstrucao[1])){//se símbolo na tabela de uso é igual ao id da instrução
                    tempUse=uso.get(i);
                    tempUse.setLocationCounter(locationCounter);
                    uso.set(i, tempUse);//atualiza endereço de ocorrência
                    break;//encerra busca na tabela
                }                            
            }
            locationCounter += 4;//atualiza contador

            switch (idInstrucao[1].charAt(0)) {//no primeiro caracter do primeiro operando
                case '#'://identifica numerais
                    switch (idInstrucao[1].charAt(1)) {//no segundo caracter da instrução
                        case '$':
                            // hexadecimal
                            idInstrucao[1] = idInstrucao[1].substring(2);
                            arqObj.write((new BigInteger(idInstrucao[1], 16) + "\t"));
                            break;
                        case '@':
                            // octal
                            idInstrucao[1] = idInstrucao[1].substring(2);
                            arqObj.write(new BigInteger(idInstrucao[1], 8) + "\t");
                            break;
                        case '%':
                            // binario
                            idInstrucao[1] = idInstrucao[1].substring(2);
                            arqObj.write(idInstrucao[1] + "\t");
                            break;
                        default:
                            // decimal
                            idInstrucao[1] = idInstrucao[1].substring(1);
                            arqObj.write(Integer.parseInt(idInstrucao[1]) + "\t");
                            break;
                    }
                    break;
                case 'D'://identifica endereço
                    arqObj.write(Integer.parseInt(idInstrucao[1].substring(1)) + "\t");
                    break;
                default:
                    arqObj.write(labels.get(idInstrucao[1]) + "\t");//escreve operando1
                    break;
            }
            locationCounter += 4;//atualiza contador

            if (idInstrucao.length == 3) {//se houver um segundo operando
                switch (idInstrucao[2].charAt(0)) {//no primeiro caracter da instrução
                    case '#'://identifica numerais
                        switch (idInstrucao[2].charAt(1)) {
                            case '$':
                                // hexadecimal
                                idInstrucao[2] = idInstrucao[2].substring(2);
                                arqObj.write(new BigInteger(idInstrucao[2], 16) + "\t");
                                break;
                            case '@':
                                // octal
                                idInstrucao[2] = idInstrucao[2].substring(2);
                                arqObj.write(new BigInteger(idInstrucao[2], 8) + "\t");
                                break;
                            case '%':
                                // binario
                                idInstrucao[2] = idInstrucao[2].substring(2);
                                arqObj.write(idInstrucao[2] + "\t");
                                break;
                            default:
                                // decimal
                                idInstrucao[2] = idInstrucao[2].substring(1);
                                arqObj.write(Integer.parseInt(idInstrucao[2]) + "\t");
                                break;
                        }
                        break;
                    case 'D'://identifica endereço
                        arqObj.write((Integer.parseInt(idInstrucao[2].substring(1))) + "\t");
                        break;
                    default:
                        for(int i=0;i<uso.size();i++){//busca na tabela de uso
                            if(uso.get(i).getSimbolo().equals(idInstrucao[1])){//se símbolo lido é igual ao id da instrução
                                tempUse=uso.get(i);
                                tempUse.setLocationCounter(locationCounter);
                                uso.set(i, tempUse);//atualiza local de uso
                                break;//encerra busca
                            }                            
                        }
                        arqObj.write((labels.get(idInstrucao[2])) + "\t");//escreve operando2
                        break;
                }
                locationCounter += 4;
            }

            arqObj.write("\n");//cria nova linha
            linha = bf.readLine();// Le nova linha
            idInstrucao = linha.split("\\s+");// Separa a linha por qualquer quantidade de espaços

        }//término das instruções "END"

        arqObj.close();//encerra escrita no arquivo
    }

    public void verificaOperacao(String palavra, FileWriter arq) throws IOException {//identifica operações
        switch (palavra) {
            case "ADD":
                arq.write("0010" + "\t");//2 em hex
                break;
            case "BR":
                arq.write("00" + "\t");//0 em hex
                break;
            case "BRNEG":
                arq.write("101" + "\t");//5 em hex
                break;
            case "BRPOS":
                arq.write("01" + "\t");//1 em hex
                break;
            case "BRZERO":
                arq.write("100" + "\t");//4 em hex
                break;
            case "CALL":
                arq.write("10101" + "\t");//15 em hex
                break;
            case "COPY":
                arq.write("10011" + "\t");//13 em hex
                break;
            case "DIVIDE":
                arq.write("10000" + "\t");//10 em hex
                break;
            case "LOAD":
                arq.write("11" + "\t");//3 em hex
                break;
            case "MULT":
                arq.write("10100" + "\t");//14 em hex
                break;
            case "READ":
                arq.write("010010" + "\t");//12 em hex
                break;
            case "RET":
                arq.write("10110" + "\t");//16 em hex
                break;
            case "STOP":
                arq.write("10001" + "\t");//11 em hex
                break;
            case "STORE":
                arq.write("111" + "\t");//7 em hex
                break;
            case "SUB":
                arq.write("110" + "\t");//6 em hex
                break;
            case "WRITE":
                arq.write("1000");//8 em hex
                break;
        }
    }
      
    public ArrayList<TabelaDefinicoes> GetTabelaDeDefinicoes(){
        return definicoes;
    }   
     
    public ArrayList<TabelaDeUso> GetTabelaDeUso(){
        return uso;
    }
}

