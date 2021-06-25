package trabfinalps2;
import java.util.*;
import java.util.regex.*;

public class Macro {
    private final String nome;
    
    private int parNum;
    private ArrayList<String> parametros;
    private ArrayList<String> instrucoes;
    
    public Macro(final String nome, final int parNum){//nome da macro e número de parâmetros
        this.nome = nome;
        this.parNum = parNum;
        this.parametros = new ArrayList<>();
        this.instrucoes = new ArrayList<>();
    }

    public void addInstrucao(String linha){//recebe uma linha de instrução
        this.instrucoes.add(linha);//adiciona na lista de instruções da macro
    }
    
    public void addParametros(String linha){ //recebe linha da chamada da macro      
        // Pega todas as entradas de parametros da chamada da macro
        Matcher m;
        m = Pattern.compile("\\w\\d").matcher(linha);//procura por padrão definido pela linha da chamada
        //\w implica em "word character"[a-z;A-Z;0-9], \d implica dígito 0-9
        while (m.find()) {//enquanto encontrar padrão definido
            String[] a;
            a = m.group().split("\n");//separa por line-break
            for (int i=0; i <= m.groupCount(); i++){
                parametros.add(a[i]);//adiciona parâmetros relevantes
            }
        }
    }
    
     public String getNome(){//auto-explicativo
        return this.nome;
    }
    
    public ArrayList<String> getInstrucoes(){//retorna lista de instruções em forma de string
        return this.instrucoes;
    }

    public ArrayList<String> getParametros(){//retorna lista de parâmetros em forma de string
        return this.parametros;
    }
    
    public int getNumParametros(){//retorna o número de parâmetros
        return this.parNum;
    }
    
    public void setInstrucoes(ArrayList<String> instrucoes){//define lista de instruções
        this.instrucoes = instrucoes;
    }
    
    public void substituiParametros(){
        String aux;
        
        for(int i=0; i<this.instrucoes.size();i++){// Para cada linha de instrucoes
            aux = this.instrucoes.get(i);//string auxiliar para a linha sendo analisada
            
            for(int j=0; j < this.parNum; j++){//para cada parâmetro
                String a = "";
                int x;
                x = j+1;
                a = a + x;
                aux = aux.replaceAll(a, this.parametros.get(j));//substitui chamada de parâmetros pelos parâmetros corretos
            }
            this.instrucoes.set(i, aux);//subsitui as instruções de fato
        }
    }
}
