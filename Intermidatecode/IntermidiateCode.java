




package Intermidatecode;

import java.io.File;
        import java.io.FileNotFoundException;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.Scanner;
        import java.util.StringTokenizer;
public class IntermidiateCode {
    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
    public static void main(String[] args) {
// TODO Auto-generated method stub
        HashMap<String, String[]> map = new HashMap<>();
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
        ArrayList<String[]> symbolTable = new ArrayList<>();
        ArrayList<String[]> literalTable = new ArrayList<>();
        ArrayList<int[]> poolTable = new ArrayList<>();
        StringBuilder intermediateCode = new StringBuilder();

        File file = new File("E:\\LPCC\\Intermidatecode\\ICcode.txt");
        Scanner sc = null;
        StringTokenizer st = null;
        int lc = 10, symbolCount = 1, literalCount = 1, poolCount =
                0;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        while (sc.hasNextLine()) {
            String str = sc.nextLine();
// System.out.println(str);

            String[] tokens = str.split("\\s+");

// for(String s : tokens) {
// System.out.print(s + " ");
// }

            int len = tokens.length;
            if (len == 1) {
                if (tokens[0].equals("START")) {
                    lc = 0;
                } else if (tokens[0].equals("END") ||

                        tokens[0].equals("LTORG")) {
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
            } else if (len == 2 && tokens[0].equals("START")) {
                lc = Integer.parseInt(tokens[1]);
            } else if (tokens[0].equals("ORIGIN")) {

                String spt[] = tokens[1].split("\\+");
                for (int i = 0; i < symbolTable.size(); i++) {

                    if (symbolTable.get(i)[1].equals(spt[0])) {
                        lc =

                                Integer.parseInt(symbolTable.get(i)[2]) +
                                        Integer.parseInt(spt[1]);
                        break;
                    }
                }
            } else {

                if (!map.containsKey(tokens[0])) {
                    if (tokens[1].equals("EQU")) {
                        int temp = 0;
                        for (int j = 0; j < symbolTable.size(); j++) {

                            if

                            (symbolTable.get(j)[1].equals(tokens[2])) {
                                temp =
                                        Integer.parseInt(symbolTable.get(j)[2]);
                                break;
                            }
                        }

                        symbolTable.add(new String[]

                                {String.valueOf(symbolCount++), tokens[0], String.valueOf(temp)});

                    } else {
                        symbolTable.add(new String[]

                                {String.valueOf(symbolCount++), tokens[0], String.valueOf(lc)});

                    }
                }
                if (tokens[tokens.length - 1].charAt(0) == '=') {
                    if (literalCount == 1) {
                        poolTable.add(new int[]{1, 1});
                    }
                    literalTable.add(new String[]

                            {String.valueOf(literalCount++), tokens[tokens.length - 1],
                                    String.valueOf(lc)});

                }
                if (tokens[1].equals("DS")) {
                    lc += Integer.parseInt(tokens[2]);

                } else {
                    lc++;
                }
            }
            for (int i = 0; i < tokens.length; i++) {
                StringBuilder line = new StringBuilder();
                if (map.containsKey(tokens[i])) {
                    line.append("(" + map.get(tokens[i])[0] + " " +

                            map.get(tokens[i])[1] + ") ");

                } else if (isNumeric(tokens[i])) {
                    line.append("(C," + Integer.parseInt(tokens[i]) + ") ");
                } else if (tokens[i].charAt(0) == '=') {
                    for (int j = 0; j < literalTable.size(); j++) {
                        if (literalTable.get(j)[1].equals(tokens[i])) {
                            line.append("(L," + literalTable.get(j)[0] + ") ");
                            break;
                        }
                    }
                } else if (!map.containsKey(tokens[i]) && i == 0) {
                    line.append("");
                } else if (tokens[i].equals("ORIGIN") ||

                        tokens[i].equals("EQU")) {

                    line = new StringBuilder();
                } else {
                    line.append(tokens[i] + " ");
                }
                intermediateCode.append(line);
            }
            intermediateCode.append("\n");
        }
        System.out.println("Symbol Table\n");
        System.out.println("Idx" + "\t" + "Symbol" + "\t" +
                "Address");
        for (String[] arr : symbolTable) {
            System.out.println(arr[0] + "\t" + arr[1] + "\t" + arr[2]);
        }

        System.out.println("\n\nLiteral Table\n");
        System.out.println("Idx" + "\t" + "Literal" + "\t" +
                "Address");
        for (String[] arr : literalTable) {
            System.out.println(arr[0] + "\t" + arr[1] + "\t" + arr[2]);

        }

        System.out.println("\n\nPool Table\n");
        for (int[] arr : poolTable) {
            System.out.println(arr[0] + "\t" + arr[1]);
        }
        System.out.println("\n\n\n Intermediate Code");
        System.out.println(intermediateCode);
    }
}