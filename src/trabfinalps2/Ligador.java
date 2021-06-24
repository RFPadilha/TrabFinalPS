package trabfinalps2;

import java.io.*;
import java.util.*;

public class Ligador {
    public  ArrayList<TabelaSimbolosGlobais> SimbolosGlobais = new ArrayList<> ();
    
    public void liga(ArrayList<TabelaDefinicoes> tabDef1, ArrayList<TabelaDefinicoes> tabDef2,
                     ArrayList<TabelaDeUso> tabUso1, ArrayList<TabelaDeUso> tabUso2,String obj1,String obj2) throws IOException {
        
        int Endereco=0;//contador de endereço
        String linhaObjeto;
        Boolean achou=false;//indica se instrução existe na tabela de símbolos globais
        
        FileWriter arqObj = new FileWriter("codigo_ligado"+ ".txt");//Cria o arquivo com o codigo ligado
        //arqObj é um arquivo que contém os valores numéricos das operaçoes e dos operandos
        
        // Leitura dos codigos fonte e objetos
        BufferedReader bf = new BufferedReader(new FileReader("objeto_" + obj1 + ".txt"));
        BufferedReader bf2 = new BufferedReader(new FileReader("objeto_" + obj2 + ".txt"));
        BufferedReader bf3 = new BufferedReader(new FileReader(obj1 + ".txt"));
        BufferedReader bf4 = new BufferedReader(new FileReader(obj2 + ".txt"));
        //bf e bf3, assim como bf2 e bf4, serão processados juntos
        
        TabelaSimbolosGlobais temp;//usada para constriur a tabela de símbolos globais  
        
        for(int i=0;i<tabDef1.size();i++){
            temp = new TabelaSimbolosGlobais();
            temp.setSimbolo(tabDef1.get(i).getSimbolo());
            temp.setEndereco(tabDef1.get(i).getEndereco());
            temp.setRelocabilidade(tabDef1.get(i).getRelocabilidade());
            SimbolosGlobais.add(temp);//adiciona tabela de definições 1 à tabela de símbolos globais
        }
        Endereco=tabDef1.get(tabDef1.size()-1).getEndereco();//marca endereço como o final da tabela de definições 1
        
        for(int i=0;i<tabDef2.size();i++){
            temp = new TabelaSimbolosGlobais();//reseta temp
            temp.setSimbolo(tabDef2.get(i).getSimbolo());
            if(tabDef2.get(i).getEndereco()!=0)
                temp.setEndereco(tabDef2.get(i).getEndereco() + Endereco);//posiciona tabelas no endereço correto
            temp.setRelocabilidade(tabDef2.get(i).getRelocabilidade());
            SimbolosGlobais.add(temp);//adiciona tabela de definições 2 à tabela de símbolos globais
        }
        
        
        
       // Lê primeiro codigo fonte e objeto
        String linha = bf3.readLine();//lê linha a linha do codigo fonte 1
        String[] idInstrucao = linha.split("\\s+");
        while (!idInstrucao[0].equals("START")) { // Avança até encontrar inicio das instruções
             linha = bf3.readLine();
             idInstrucao = linha.split("\\s+");
        }
        linha = bf3.readLine();//lê primeira linha de instruções
        idInstrucao = linha.split("\\s+");
        
        while (true) { // Enquanto não termina de ler instruções
            linhaObjeto=bf.readLine(); // lê o codigo objeto 1
            
            for(int i=0;i<SimbolosGlobais.size();i++){//percorre tabela de símbolos globais
                if(SimbolosGlobais.get(i).getSimbolo().equals(idInstrucao[1])){//se operando 1 está na tabela global
                    idInstrucao = linhaObjeto.split("\\s+");//separamos por espaços quando lemos
                    arqObj.write(idInstrucao[0] + "\t");//escrevemos separando com "tab"
                    arqObj.write(SimbolosGlobais.get(i).getEndereco() + "\t");//saida do ligador recebe endereco atualizado
                    if (idInstrucao.length == 3){//se instrução possui 2 operandos
                        arqObj.write(idInstrucao[2] + "\t");
                    }
                    achou=true;
                    break;
              }           
                else if(idInstrucao.length == 3){//se instrução tem 2 operandos
                    if(SimbolosGlobais.get(i).getSimbolo().equals(idInstrucao[2])){//se segundo operando está na tabela global
                    idInstrucao = linhaObjeto.split("\\s+");//idInstrucao recebe linha lida
                    arqObj.write(idInstrucao[0] + "\t");
                    arqObj.write(idInstrucao[1] + "\t");
                    arqObj.write(SimbolosGlobais.get(i).getEndereco() + "\t");//escreve símbolo global no arquivo de saída
                    achou=true;
                    break;
                    }
                }
            }             
            if(achou==false && !(linhaObjeto.contains("0100111001110010"))){//se não encontrou instrução, e não é uma instrução "STOP"
                arqObj.write(linhaObjeto + "\t");//escreve normalmente, provavelmente é operando ou valor
            }                     
            linha = bf3.readLine();//lê próxima linha           
            idInstrucao = linha.split("\\s+");
            achou=false;
            if(idInstrucao[0].equals("END"))
                break;
            else
                arqObj.write("\n");  
        }
        
        
        
        
        
        
        
        
        
        // Lê segundo codigo fonte e objeto
        linha = bf4.readLine();//lê código fonte 2
        idInstrucao = linha.split("\\s+");
        while (!idInstrucao[0].equals("START")) {
             linha = bf4.readLine();
             idInstrucao = linha.split("\\s+");//salva todas as instruções, operandos e labels em idInstrucao enquanto programa nao inicia
        }
        linha = bf4.readLine();
        idInstrucao = linha.split("\\s+");//le primeira palavra
        
        while (!idInstrucao[0].equals("END")) {//enquanto nao chega ao fim do codigo objeto 2
            linhaObjeto=bf2.readLine();//lê próxima linha do código objeto 2
            
            for(int i=0;i<SimbolosGlobais.size();i++){//percorre tabela global
                if(SimbolosGlobais.get(i).getSimbolo().equals(idInstrucao[1])){//se primeiro operando está na tabela de símbolos
                    idInstrucao = linhaObjeto.split("\\s+");
                    arqObj.write(idInstrucao[0] + "\t");
                    arqObj.write(SimbolosGlobais.get(i).getEndereco() + "\t");
                    if (idInstrucao.length == 3)//se possui 2 operandos
                        arqObj.write(idInstrucao[2] + "\t");
                    achou=true;
                    break;
              }
                else if(idInstrucao.length == 3){//se possui 2 operandos
                    if(SimbolosGlobais.get(i).getSimbolo().equals(idInstrucao[2])){//se segundo operando está na tabela de simbolos
                    idInstrucao = linhaObjeto.split("\\s+");
                    arqObj.write(idInstrucao[0] + "\t");
                    arqObj.write(idInstrucao[1] + "\t");
                    arqObj.write(SimbolosGlobais.get(i).getEndereco() + "\t");//escreve no arquivo de saida
                    achou=true;
                    break;
                    }
                }
            }                
            if(achou==false){//se linha lida não contém uma instrução da tabela
                if(linhaObjeto.contains("0100111001110010"))//se contém a instrução "STOP"
                    arqObj.write("0100111001110010" + "\t");//escreve STOP
                else
                    arqObj.write(linhaObjeto + "\t");//senão escreve a linha normalmente
            }
            arqObj.write("\n");//pula 1 linha
           
            linha = bf4.readLine();//lê a próxima linha do código fonte 2           
            idInstrucao = linha.split("\t");
            achou=false;            
        }        
     
        arqObj.close();
        bf.close();
        bf2.close();
        bf3.close();
        bf4.close();//fecha arquivos
    }
}





