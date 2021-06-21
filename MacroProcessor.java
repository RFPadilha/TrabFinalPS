package trabfinalps2;

import java.util.*;
import java.io.*;

class MDT {//Macro name Definition counter, usada para fazer expansão de macro
    int index;
    String def;
    MDT(int i, String a) {
        this.index = i;
        this.def = a;
    }
}
class MNT {//Macro Name Table counter, usada para reconhecer nome de macro
    int index,mdtind;
    String name;
    List<String> ala;//ala stands for Argument List Array, onde ficam salvos todos os argumentos
    MNT(int i, String a, int ind, List<String> b) {
        this.index = i;
        this.name = a;
        this.mdtind = ind;
        this.ala = b;
    }
}
public class MacroProcessor {
    
    
    public static File createNewFile(String fileName){//cria arquivo de output
        try {
            File myObj = new File(fileName+".sic");
            if (myObj.createNewFile()) {
              System.out.println("File created: " + myObj.getName());
            } else {
              System.out.println("File already exists.");
              File file = new File(fileName+".sic");
              file.delete();
              createNewFile(fileName);
              System.out.println("Old file was deleted.");
            }
            return myObj;
        } catch (IOException e) {
          System.out.println("An error occurred.");
          e.printStackTrace();
        }
        return null;
    }
    
    public static void appendStrToFile(File fileName, String text){//escreve no arquivo sem criar outro
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true));
            out.write(text);
            out.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    
    public static int searchmnt(List<MNT> a, String b) {//procura na tabela de macros
        int i,pos = -1;
        for(i = 0; i < a.size(); i++) {//percorre lista
            MNT x = a.get(i);
            if(x.name.equals(b)) { pos = i; break; }//retorna string b, pos retorna índice
        }
        return pos;//se -1 é porque não foi encontrado
    }
    public static void main(String[] args) throws Exception {
        File output = createNewFile("macroProcessorOutput");//arquivo onde será salvo o output do processador de macros
        //lê arquivo de entrada
        //substituir endereço local abaixo por onde o arquivo de texto com as definições de macro a serem testadas
        BufferedReader r = new BufferedReader(new FileReader("./input.txt"));
        BufferedReader r1 = new BufferedReader(new InputStreamReader(System.in));
        //variáveis
        List<MDT> mdt = new Vector<MDT>();
        List<MNT> mnt = new Vector<MNT>();
        List<String> isc = new Vector<String>();
        String d;
        boolean flag = false;//flag que identifica início da definição da macro
        
        /* PASS 1 */
        while((d = r.readLine()) != null) {
            d = d.trim();
            if(d.isEmpty()) continue;
            String temp[] = d.split("\\s+");//separando por 1 ou mais caracteres de espaço
            if(d.contains("MACRO")) flag = true; //se encontrar a sub-string "MACRO"
            if(flag) {//inicia a composição da macro
                d = r.readLine(); 
                String t[] = d.split("\\s+");//separando instruções por caracteres de espaço
                String t1[] = t[1].split("\\,");//separa por vírgulas em caso de mais de 1 argumento
                
                List<String> arg = new Vector<String>();//lista onde ficarão armazenados os argumentos encontrados na macro
                for(int i = 0; i < t1.length; i++) {
                    String p = t1[i];
                    if(t1[i].contains("=")) p = t1[i].substring(0,t1[i].indexOf('='));
                    
                    arg.add(p);//armazena argumentos, parando em "=" se necessário
                }
                mnt.add(new MNT(mnt.size() + 1, t[0], mdt.size() + 1, arg));//salva argumento na tabela MNT[índice, nome, índice na mdt, argumentos]
                mdt.add(new MDT(mdt.size() + 1, d));//adiciona linha lida a tabela de definição de macros[índice, linha]
                d = r.readLine();//lê próxima linha
                while(!d.equals("MEND")) {//até que seja encontrado o fim da definição da macro
                    if(d.contains("&")) {//detecta & na string 
                        int i = d.indexOf('&');
                        MNT h = mnt.get(mnt.size() - 1);
                        int j;
                        for(j = 0; j < h.ala.size(); j++) {
                            if(d.substring(i).equals(h.ala.get(j)))
                                break;
                        }
                        mdt.add(new MDT(mdt.size() + 1, d.substring(0,i)+"#"+(j+1)));//adiciona substring com # e índice na mnt
                    }
                    else mdt.add(new MDT(mdt.size() + 1, d));//adiciona substring normalmente
                    d = r.readLine();
                }
                if(d.equals("MEND")) mdt.add(new MDT(mdt.size() + 1, d));//detecta encerramento da definição da macro
                flag = false;
            }
            else {
                isc.add(d);//isc contém tudo que não é macro
            }
        }
        //retorna resultados
        System.out.println("\nPASS 1\n");
        appendStrToFile(output, "PASS 1\n");
        System.out.println("MDT");
        appendStrToFile(output, "\nMDT");
        for(int i = 0; i < mdt.size(); i++) {
            MDT t = mdt.get(i);
            System.out.println(t.index+" "+t.def);
            appendStrToFile(output, "\n"+t.index+" "+t.def);
        }
        System.out.println("\nMNT");
        appendStrToFile(output, "\n\nMNT\n");
        for(int i = 0; i < mnt.size(); i++) {
            MNT t = mnt.get(i);
            System.out.print(t.index+" "+t.name+" "+t.mdtind+"\tALA: ");
            appendStrToFile(output, t.index+" "+t.name+" "+t.mdtind+"\tALA: ");
            for(int j = 0; j < t.ala.size(); j++) {
                System.out.print(t.ala.get(j)+" ");
                appendStrToFile(output,t.ala.get(j)+" ");
            } 
            System.out.println();
            appendStrToFile(output,"\n");
        }
        //retorna código intermediário
        System.out.println("\nIntermediate code");
        appendStrToFile(output,"\nIntermediate code");
        for(int i = 0; i < isc.size(); i++) {
            appendStrToFile(output,"\n"+isc.get(i));
            System.out.println(isc.get(i));
        }
        
        
        
        
        System.out.println("\n--------------------------------\nPASS 2\n");
        appendStrToFile(output,"\n\n--------------------------------\nPASS 2\n");
        /* PASS 2 */
        for(int i = 0; i < isc.size(); i++) {//com todo o código que sobrou fora da definição de macro
            String temp[] = isc.get(i).split("\\s+");//separando por caracteres de espaço
            int pos1 = searchmnt(mnt,temp[0]);//procura se a primeira substring é uma definição de macro
            if(pos1 == -1){
                System.out.println(isc.get(i));//se não for, retorna índice
                appendStrToFile(output,isc.get(i)+"\n");
            }
            else if(pos1 != -1) {//se for uma definição de macro conhecida
                MNT x = mnt.get(pos1);
                int mdt_ind = x.mdtind;//recebe índice na mdt
                String ala1[] = temp[1].split("\\,");//separa argumentos na vírgula
                System.out.println(ala1[0] + " " + ala1[1]);
                appendStrToFile(output,ala1[0] + " " + ala1[1]+"\n");
                StringBuffer ss = new StringBuffer();
                for(int j = mdt_ind; j < mdt.size(); j++) {//partindo do índice na mdt até o fim
                    flag = false;
                    String def = mdt.get(j).def;//procura definição na tabela mdt
                    if(def.equals("MEND")) break;//encerra loop "for" quando encontrar fim da macro
                    else {//enquanto não encontra fim da macro
                        for(int k = 0; k < def.length(); k++) {
                            if(!flag && def.charAt(k) != '#'){
                                System.out.print(def.charAt(k));//imprime instrução até o caracter "#"
                                appendStrToFile(output,String.valueOf(def.charAt(k)));
                            }
                            else if(!flag && def.charAt(k) == '#') flag = true;//ou encontra início da macro
                            else if(flag && def.charAt(k) == ',') {
                                //System.out.println(ss.toString());
                                int pos = Integer.parseInt(ss.toString());
                                System.out.print(ala1[pos - 1]);//imprime instrução, encerrando antes da vírgula
                                appendStrToFile(output,ala1[pos-1]);
                                ss.delete(0, ss.length());//deleta o que existe em ss
                                flag = false;
                            }
                            else if(flag) {//enquanto estiver encontrando uma macro, adiciona caracter a caracter ao buffer ss
                                ss.append(def.charAt(k));
                            }
                        }
                        if(ss.length() > 0) {//termina de imprimir argumentos
                            int pos = Integer.parseInt(ss.toString());
                            System.out.print(ala1[pos - 1]);
                            appendStrToFile(output,ala1[pos-1]);
                            System.out.println();
                            appendStrToFile(output,"\n");
                            ss.delete(0, ss.length());
                        }
                    }
                }
            }
        }
    }
}