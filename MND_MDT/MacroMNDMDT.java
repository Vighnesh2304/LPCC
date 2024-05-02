package MND_MDT;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MacroMNDMDT {
    static int mdtPointer = 0;

    static FileReader fr;
    static FileWriter wf;

    // Macro Name Table (MNT): Stores the name of macros and their corresponding addresses
    static HashMap<String, Integer> MNT = new HashMap<>();

    // Macro Definition Table (MDT): Stores the expanded definitions of macros
    static HashMap<Integer, String> MDT = new HashMap<>();

    // Argument List Array (ALA): Stores the formal parameters and their corresponding positional parameters
    static HashMap<String, String> ALA = new HashMap<>();

    // Intermediate Code
    static StringBuilder ic = new StringBuilder();

    public static void main(String[] args) {
        // Flag to indicate whether the parsing is inside a macro definition
        boolean mstart = false;

        // Variable to keep track of the previous line read
        String prev = "";

        try {
            // Reading the input file containing macro definitions
            fr = new FileReader("E:\\LPCC\\MND_MDT\\MNDMDT.txt");
            BufferedReader bf = new BufferedReader(fr);
            String s1 = "";

            // Parsing the file line by line
            while ((s1 = bf.readLine()) != null) {
                // Splitting the line into individual tokens
                String[] indv = s1.split("\\s+");

                // Handling macro definitions
                if (indv[0].equals("MACRO")) {
                    prev = indv[0];
                    mstart = true;
                    continue;
                }

                // Processing macro definition lines
                if (prev.equals("MACRO")) {
                    // Storing macro name and its address in MNT
                    MNT.put(indv[0], mdtPointer + 1);

                    // Storing formal parameters and their positional parameters in ALA
                    for (int i = 1; i < indv.length; i++) {
                        ALA.put(indv[i], "#" + Integer.toString(i));
                    }
                    prev = s1;
                }

                // Processing lines inside a macro definition
                if (mstart) {
                    // If the line does not already exist in MNT, add it to MDT
                    if (!MNT.containsKey(indv[0])) {
                        String str = indv[0];
                        for (int i = 1; i < indv.length; i++) {
                            // If the token is a formal parameter, replace it with its positional parameter
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

                // If not inside a macro definition, append the line to the intermediate code
                if(!mstart) {
                    ic.append("\n" + s1);
                }

                // Checking for the end of macro definition
                if (s1.equals("MEND")) {
                    mstart = false;
                }
                prev = s1;
            }

            // Finding the maximum length of macro names for formatting
            int maxMacroNameLength = 0;
            for (String macroName : MNT.keySet()) {
                maxMacroNameLength = Math.max(maxMacroNameLength, macroName.length());
            }

            // Printing the Macro Definition Table (MDT)
            System.out.println("!!-- MDT Table --!!");
            System.out.println("Address \t Definition");
            for (Map.Entry<Integer, String> entry : MDT.entrySet()) {
                System.out.println(entry.getKey() + "\t\t\t" + entry.getValue());
            }

            // Printing the Macro Name Table (MNT)
            System.out.println("\n!!-- MNT Table --!!");
            System.out.println("MACRO \t Address");
            for (Map.Entry<String, Integer> entry : MNT.entrySet()) {
                String macroName = entry.getKey();
                int address = entry.getValue();
                String spaces = " ".repeat(maxMacroNameLength - macroName.length() + 1);
                System.out.println(macroName + spaces + address);
            }

            // Printing the Argument List Array (ALA)
            System.out.println("\n!!-- ALA Table --!!");
            System.out.println("Formal \t Positional");
            for (Map.Entry<String, String> entry : ALA.entrySet()) {
                System.out.println(entry.getKey() + "\t" + entry.getValue());
            }

            // Printing the Intermediate Code
            System.out.println("\n!!-- Intermediate Code --!!");
            System.out.println(ic);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
