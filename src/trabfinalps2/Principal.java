package trabfinalps2;

import java.io.*;
import java.util.*;
import javax.swing.*;
import java.text.Normalizer;


public class Principal {

    private String arq;
    private String arq1;
    private String inArq = "";
    private String outArq = "";
    private String ligado = "";
    private String carregado = "";
    private String regA = "";
    private String regD = "";
    private String arqM = "";
    private String MacroDef = "";//string que define as macros
    private String MacroExp = "";//string que expande as macros
    
    public Principal(String Arq) throws IOException{
        arq = Arq;//receberá o nome do arquivo de entrada
        arq1 = arq.concat("1");
        if(arq != null){//se não recebeu nome na chamada
            arqM = JOptionPane.showInputDialog(null, "Digite o nome do arquivo de macro sem '.txt'", "Arquivo", JOptionPane.QUESTION_MESSAGE);
            
            if(arqM.equals("")) {//se não existe arquivo de macro
                JOptionPane.showMessageDialog(null, "Sem macros para analizar", "Alerta!", 1, null);
            }//retorna erro de falta de macros
            
            else{//se existe arquivo de macro
                ProcessadorMacros macro = new ProcessadorMacros(arqM);
                macro.processa();//processa macros
                
                DefMDef();//MacroDef recebe o arquivo de definição de macros
                DefMExp();//MacroExp recebe arquivo de expansão de macros
            }
            //inicializa objetos
            Montador mont = new Montador();
            Montador mont1 = new Montador();
            Memoria mem = new Memoria();
            Ligador linker = new Ligador();
            Carregador loader = new Carregador();
            
            try {
                
                InArq(arq);//recebe todo arquivo de entrada 1
                mont.monta(arq);//montador executa sobre o arquivo de entrada 1
                OutArq(arq);//escreve arquivo de saida 1
                InArq(arq1);//escreve arquivo de entrada 2
                mont1.monta(arq1);//montador executa sobre o arquivo de entrada 2
                OutArq(arq1);//escreve arquivo de saida 2
                
                linker.liga(mont.GetTabelaDeDefinicoes(),mont1.GetTabelaDeDefinicoes(),
                            mont.GetTabelaDeUso(),mont1.GetTabelaDeUso(),arq,arq1);//ligador executa sobre as tabelas do montador, após montador executar sobre arquivos de entrada
                Ligado();//varíavel "ligado" recebe arquivo "codigo_ligado.txt"
                
                mem = loader.carrega(ligado);//carregador executa sobre variável "ligado"
                Carregado();//variável "carregado" recebe "arquivo_carregado.txt"
                
                defRegA(mem);//inicializa registradores
                defRegD(mem);
                
                JOptionPane.showMessageDialog(null, "Codigo executado com exito", "Executado", 1, null);
            } catch (IOException ex) {
               JOptionPane.showMessageDialog(null, "Arquivo invalido", "Erro!", JOptionPane.ERROR_MESSAGE, null);
            }
        }
        else{
            arq = JOptionPane.showInputDialog(null, "Sem resultados obtidos", "Resultados", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    //funções abaixo transferem conteúdo de arquivos para as variáveis desta classe
    
    public void InArq(String arq) throws FileNotFoundException, IOException{
        FileReader arqPath = new FileReader(arq + ".txt");//recebe caminho do arquivo
        BufferedReader arqObj = new BufferedReader(arqPath);//leitor do arquivo recebido

        while(arqObj.ready()){//enquanto arquivo puder ser lido
          this.inArq += arqObj.readLine() + "\n";//transfere todo o conteudo do arquivo para inArq
        }
        
        arqObj.close();
    }
    
    public void OutArq(String arq) throws FileNotFoundException, IOException{
        FileReader arqPath = new FileReader("objeto" + "_" + arq + ".txt");
        BufferedReader arqObj = new BufferedReader(arqPath);

        while(arqObj.ready()){//enquanto arquivo puder ser lido
          this.outArq += arqObj.readLine() + "\n";//transfere conteudo do arquivo para outArq
        }
        
        arqObj.close();
    }
    
    public void Ligado() throws FileNotFoundException, IOException{
        FileReader arqPath = new FileReader("codigo_ligado.txt");
        BufferedReader arqObj = new BufferedReader(arqPath);

        while(arqObj.ready()){//enquanto arquivo puder ser lido
          this.ligado += arqObj.readLine() + "\n";//transfere conteudo para variável "ligado"
        }
        
        arqObj.close();
    }
    
     public void Carregado() throws FileNotFoundException, IOException{
        FileReader arqPath = new FileReader("arquivo_carregado.txt");
        BufferedReader arqObj = new BufferedReader(arqPath);

        while(arqObj.ready()){//enquanto arquivo puder ser lido
          this.carregado += arqObj.readLine() + "\n";//transfere conteudo para variável "carregado"
        }
        
        arqObj.close();
    }
     
     public void DefMDef() throws FileNotFoundException, IOException{
        FileReader arqPath = new FileReader(arqM + ".txt");//recebe arquivo de macros
        BufferedReader arqObj = new BufferedReader(arqPath);//leitor do arquivo de macros

        while(arqObj.ready()){//enquanto arquivo puder ser lido
          this.MacroDef += arqObj.readLine() + "\n";//recebe linha a linha do arquivo de macros
        }
        
        arqObj.close();
    }
     
    public void DefMExp() throws FileNotFoundException, IOException{
        FileReader arqPath = new FileReader("MacroProcessada.txt");
        BufferedReader arqObj = new BufferedReader(arqPath);

        while(arqObj.ready()){//enquanto arquivo puder ser lido
          this.MacroDef += arqObj.readLine() + "\n";
        }
        
        arqObj.close();
    }
    
    //funções acima transferem conteúdo de arquivos para as variáveis desta classe
    
    
    
    public String getInArq(){
        return this.inArq;
    }
    
    public String getOutArq(){
        return this.outArq;
    }
    
    public String getLigado(){
        return this.ligado;
    }
    
    public String getCarregado(){
         return this.carregado;
    }
    
    public void defRegA(Memoria mem){
        int i;
        regA = "| ";
        for(i=0; i<8; i++){
            regA = regA.concat(mem.getA(i));
            regA = regA.concat(" | ");
        }
    }
    
    public void defRegD(Memoria mem){
        int i;
        regD = "| ";
        for(i=0; i<8; ++i){
            regD = regD.concat(mem.getD(i));
            regD = regD.concat(" | ");
        }
    }
    
    public String getRegA(){//recebe registrador de endereços
        return regA;
    }
    
    public String getRegD(){//recebe registrador de dados
        return regD;
    }
    
    public String getMacroDef(){//recebe definição de macros
        return MacroDef;
    }
    
    public String getMacroExp(){//recebe expansão de macros
        return MacroExp;
    }

}