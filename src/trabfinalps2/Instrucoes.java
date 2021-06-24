package trabfinalps2;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Instrucoes {

    public int add(int acc, int opd) {
        return acc + opd;
    }
    
    public int br(int opd) {//resolver BRNEG, BRPOS, e BRZERO antes da chamada da instrução
        return opd;//retorna sempre para ProgramCounter
    }

    public int call(int opd){//call recebe um operando que é um endereço de sub-rotina
        return opd;//retorna para o programCounter que irá imediatamente executar a subrotina
    }
    
    public int copy(int opd1, int opd2){
        opd1 = opd2;
        return opd1;
    }
    
    public int divide(int acc, int opd) {//retorna para o acumulador
        return acc / opd;
    }

    public int load(int opd) {//retorna para o acumulador
        int acc = opd;
        return acc;
    }
    
    public int mult(int acc, int opd){//retorna para o acumulador
        acc *= opd;
        return acc;
    }
    
    public int read(int opd){//operando recebe input de inteiros do console
        Scanner sc=new Scanner(System.in);
        opd = sc.nextInt();
        return opd;
    }
    
    public int ret(int opd){
        //retorna posição de memória para o program counter
        return opd;//operando é o endereço de retorno de uma instrução "call"
    }
    
    public void stop(){
        System.exit(1);//encerra execução do programa
    }
    
    public int store(int opd, int acc){
        opd = acc;
        return opd;
    }

    public int sub(int acc, int opd) {//reap as sub
        return opd - acc;
    }
}
