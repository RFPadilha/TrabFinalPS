package trabfinalps2;

import java.io.*;

public class Save {
    private String namePath;//caminho de onde arquivo será salvo
    
    public Save(String obj, String objPath) throws IOException{
        namePath = objPath;
        String name = objPath + ".txt";//define formato do arquivo
        File arquivo = new File(name);//cria novo arquivo
        
        FileWriter grava = new FileWriter(arquivo);
        PrintWriter escreve = new PrintWriter(grava);
        escreve.println(obj);//escreve linha obj recebida como parâmetro no arquivo
        
        escreve.close();
        grava.close();//fecha arquivos
    }
    
    public String fileName(){
        return namePath;
    }
}
