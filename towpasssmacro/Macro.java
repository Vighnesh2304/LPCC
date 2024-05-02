package towpasssmacro;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Macro {
    static int mdtPointer = 0;

    static FileReader fr;
    static FileWriter wf;

    static HashMap<String, Integer> MNT = new HashMap<>();
    static HashMap<Integer, String> MDT = new HashMap<>();
    static HashMap<String, String> ALA = new HashMap<>();
    static StringBuilder ic = new StringBuilder();

    public static void main(String[] args) {

        boolean mstart = false;
        String prev = "";

        try {
            fr = new FileReader("D:/NOTES/sem 6/LPCC/Practical Exam/Macro MDT MNT/code.txt");
            BufferedReader bf = new BufferedReader(fr);
            String s1 = "";

            while ((s1 = bf.readLine()) != null) {
                String[] indv = s1.split("\\s+");
                if (indv[0].equals("MACRO")) {
//					System.out.println(s1);
                    prev = indv[0];
                    mstart = true;
                    continue;
                }
                if (prev.equals("MACRO")) {
                    MNT.put(indv[0], mdtPointer + 1);

                    for (int i = 1; i < indv.length; i++) {
                        ALA.put(indv[i], "#" + Integer.toString(i));
                    }
                    prev = s1;
                }
                if (mstart) {
                    if (!MNT.containsKey(indv[0])) {
                        String str = indv[0];
                        for (int i = 1; i < indv.length; i++) {
                            if (indv[i].charAt(0) == '&') {
                                str += (" " + ALA.get(indv[i]));
                            } else {
                                str += " " + indv[i];
                            }
                        }
                        MDT.put(mdtPointer + 1, str);
                        mdtPointer++;
                    }
                }
                if(!mstart) {
                    ic.append("\n" + s1);
                }
                if (s1.equals("MEND")) {
                    mstart = false;
                }
                prev = s1;
            }
            int maxMacroNameLength = 0;
            for (String macroName : MNT.keySet()) {
                maxMacroNameLength = Math.max(maxMacroNameLength, macroName.length());
            }
            System.out.println("!!-- MDT Table --!!");
            System.out.println("Address \t Defination");

            for (Map.Entry<Integer, String> entry : MDT.entrySet()) {
                System.out.println(entry.getKey() + "\t\t\t" + entry.getValue());
            }

            System.out.println("\n!!-- MNT Table --!!");
            System.out.println("MACRO Address");

            for (Map.Entry<String, Integer> entry : MNT.entrySet()) {
                String macroName = entry.getKey();
                int address = entry.getValue();
                String spaces = " ".repeat(maxMacroNameLength - macroName.length() + 1);
                System.out.println(macroName + spaces + address);
            }

            System.out.println("\n!!-- ALA Table --!!");
            System.out.println("Formal \t Positional");

            for (Map.Entry<String, String> entry : ALA.entrySet()) {
                System.out.println(entry.getKey() + "\t" + entry.getValue());
            }

            System.out.println("\n!!-- Intermediate Code --!!");
            System.out.println(ic);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
