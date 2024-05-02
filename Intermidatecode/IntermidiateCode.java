package Intermidatecode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

public class IntermidiateCode {

    // Function to check if a string is numeric
    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    public static void main(String[] args) {

        // HashMap to store instruction mappings (instruction -> [instruction type, opcode])
        HashMap<String, String[]> map = new HashMap<>();
        // Adding instructions and their opcodes to the map
        map.put("STOP", new String[]{"IS", "00"});
        map.put("ADD", new String[]{"IS", "01"});
        map.put("SUB", new String[]{"IS", "02"});
        map.put("MULTI", new String[]{"IS", "03"});
        map.put("MOVER", new String[]{"IS", "04"});
        map.put("MOVEM", new String[]{"IS", "05"});
        map.put("COMP", new String[]{"IS", "06"});
        map.put("BC", new String[]{"IS", "07"});
        map.put("DIV", new String[]{"IS", "08"});
        map.put("READ", new String[]{"IS", "09"});
        map.put("PRINT", new String[]{"IS", "10"});
        map.put("START", new String[]{"AD", "01"});
        map.put("END", new String[]{"AD", "02"});

        map.put("ORIGIN", new String[]{"AD", "03"});
        map.put("EQU", new String[]{"AD", "04"});
        map.put("LTORG", new String[]{"AD", "05"});
        map.put("DL", new String[]{"DL", "01"});
        map.put("DS", new String[]{"DL", "02"});
        // Add other instructions similarly...

        // ArrayLists to store symbol, literal, and pool tables
        ArrayList<String[]> symbolTable = new ArrayList<>();
        ArrayList<String[]> literalTable = new ArrayList<>();
        ArrayList<int[]> poolTable = new ArrayList<>();
        // StringBuilder to store intermediate code
        StringBuilder intermediateCode = new StringBuilder();

        // Reading input from file
        File file = new File("E:\\LPCC\\Intermidatecode\\ICcode.txt");
        Scanner sc = null;
        StringTokenizer st = null;
        int lc = 10, symbolCount = 1, literalCount = 1, poolCount = 0;

        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Processing each line of input
        while (sc.hasNextLine()) {
            String str = sc.nextLine();
            String[] tokens = str.split("\\s+");
            int len = tokens.length;

            // Handling single-token lines (START, END, LTORG)
            if (len == 1) {
                if (tokens[0].equals("START")) {
                    lc = 0;
                } else if (tokens[0].equals("END") ||
                        tokens[0].equals("LTORG")) {
                    // If END or LTORG, add pool count and update literal addresses
                    if (poolCount > 0) {
                        poolTable.add(new int[]{poolCount, literalCount - 1});
                    }
                    if (poolTable.size() != 0) {
                        int idx = poolTable.get(poolCount++)[1] - 1;
                        for (int j = idx; j < literalTable.size(); j++) {
                            literalTable.get(j)[2] = String.valueOf(lc++);
                        }
                    }
                }
            // Handling START with specified initial location
            } else if (len == 2 && tokens[0].equals("START")) {
                lc = Integer.parseInt(tokens[1]);
            // Handling ORIGIN instruction
            } else if (tokens[0].equals("ORIGIN")) {
                String spt[] = tokens[1].split("\\+");
                for (int i = 0; i < symbolTable.size(); i++) {
                    if (symbolTable.get(i)[1].equals(spt[0])) {
                        lc = Integer.parseInt(symbolTable.get(i)[2]) + Integer.parseInt(spt[1]);
                        break;
                    }
                }
            } else {
                // Processing instructions and operands
                if (!map.containsKey(tokens[0])) {
                    if (tokens[1].equals("EQU")) {
                        // Handling EQU directive
                        int temp = 0;
                        for (int j = 0; j < symbolTable.size(); j++) {
                            if (symbolTable.get(j)[1].equals(tokens[2])) {
                                temp = Integer.parseInt(symbolTable.get(j)[2]);
                                break;
                            }
                        }
                        symbolTable.add(new String[]{String.valueOf(symbolCount++), tokens[0], String.valueOf(temp)});
                    } else {
                        // Adding new symbols to the symbol table
                        symbolTable.add(new String[]{String.valueOf(symbolCount++), tokens[0], String.valueOf(lc)});
                    }
                }
                // Handling literals
                if (tokens[tokens.length - 1].charAt(0) == '=') {
                    if (literalCount == 1) {
                        poolTable.add(new int[]{1, 1});
                    }
                    literalTable.add(new String[]{String.valueOf(literalCount++), tokens[tokens.length - 1], String.valueOf(lc)});
                }
                // Updating location counter (lc)
                if (tokens[1].equals("DS")) {
                    lc += Integer.parseInt(tokens[2]);
                } else {
                    lc++;
                }
            }

            // Generating intermediate code for each line
            for (int i = 0; i < tokens.length; i++) {
                StringBuilder line = new StringBuilder();
                // Generating intermediate code based on instruction mapping
                if (map.containsKey(tokens[i])) {
                    line.append("(" + map.get(tokens[i])[0] + " " + map.get(tokens[i])[1] + ") ");
                // Generating intermediate code for constants
                } else if (isNumeric(tokens[i])) {
                    line.append("(C," + Integer.parseInt(tokens[i]) + ") ");
                // Generating intermediate code for literals
                } else if (tokens[i].charAt(0) == '=') {
                    for (int j = 0; j < literalTable.size(); j++) {
                        if (literalTable.get(j)[1].equals(tokens[i])) {
                            line.append("(L," + literalTable.get(j)[0] + ") ");
                            break;
                        }
                    }
                // Generating intermediate code for symbols
                } else if (!map.containsKey(tokens[i]) && i == 0) {
                    line.append("");
                } else if (tokens[i].equals("ORIGIN") || tokens[i].equals("EQU")) {
                    // Ignoring ORIGIN and EQU in intermediate code
                    line = new StringBuilder();
                } else {
                    // Adding symbols to intermediate code
                    line.append(tokens[i] + " ");
                }
                intermediateCode.append(line);
            }
            // Adding new line after each line of intermediate code
            intermediateCode.append("\n");
        }

        // Printing symbol table
        System.out.println("Symbol Table\n");
        System.out.println("Idx" + "\t" + "Symbol" + "\t" + "Address");
        for (String[] arr : symbolTable) {
            System.out.println(arr[0] + "\t" + arr[1] + "\t" + arr[2]);
        }

        // Printing literal table
        System.out.println("\n\nLiteral Table\n");
        System.out.println("Idx" + "\t" + "Literal" + "\t" + "Address");
        for (String[] arr : literalTable) {
            System.out.println(arr[0] + "\t" + arr[1] + "\t" + arr[2]);
        }

        // Printing pool table
        System.out.println("\n\nPool Table\n");
        for (int[] arr : poolTable) {
            System.out.println(arr[0] + "\t" + arr[1]);
        }

        // Printing intermediate code
        System.out.println("\n\n\n Intermediate Code");
        System.out.println(intermediateCode);
    }
}
