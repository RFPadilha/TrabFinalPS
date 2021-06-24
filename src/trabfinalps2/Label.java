package trabfinalps2;

public class Label {
    private String nome;
    private int ocorrencias;
    
    public Label(String nome){
        this.nome = nome;
        this.ocorrencias = 0;
    }
    
    public String getNome(){
        return this.nome;
    }
    
    public int getOcorrencias(){
        return this.ocorrencias;
    }
    
    public void addOcorrencia(){
        this.ocorrencias++;
    }
}
