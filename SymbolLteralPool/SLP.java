package SymbolLteralPool;

import java.util.*;

public class SLP {
    private HashMap<String, Integer> symbolTable;
    private ArrayList<String[]> literalTable;
    private List<Integer> poolTable;
    private int locationCounter;
    private int poolStart;
    private int literalIndex;

    public SLP(){
        symbolTable = new HashMap<>();
        literalTable = new ArrayList<>();
        poolTable = new ArrayList<>();
        locationCounter = 0;
        poolStart = 1;
        literalIndex = 0;
    }

    public void firstPass(String[] sourceCode){
        for(String line : sourceCode){
            line = line.trim(); //Remove whitespaces from start and end of line

            // Start location counter from given address
            if(line.startsWith("START")) locationCounter = Integer.parseInt(line.split("\\s+")[1]);


            else if(line.startsWith("END")) {
                // We do not assign address to END
                locationCounter--;
                // Add literals in literal table...
                processLTORG(line);
                break;
            }

            else if(!line.isEmpty()){
                if(line.contains("='") && line.split("\\s+").length>3){
                    processLabel(line);
                    processLiteral(line);
                }
                else if(line.contains("='")){
                    processLiteral(line);
                }
                else if(line.contains("LTORG")){
                    processLTORG(line);
                }
                else{
                    processLabel(line);
                }
                processInstruction(line);
            }
        }
    }

    private void processLiteral(String line){
        String literal = line.split("='")[1].split("'")[0];
        literalTable.add(new String[]{literal, Integer.toString(locationCounter)});
    }


    // When LTORG is encountered,
    // Add literals in literal in literal table...
    // Their addresses will be from next location counter...
    private void processLTORG(String line){
        for(int i=literalIndex; i<literalTable.size(); i++){
            String[] temp = {literalTable.get(i)[0], Integer.toString(++locationCounter)};
            literalTable.set(i, temp);
        }
        literalIndex += literalTable.size();
        locationCounter++;

        // Pool Table will start from poolStart, initially #1
        poolTable.add(poolStart);

        //For next pool, pool table will contain entry of next literal (if it exsists)...
        poolStart = literalTable.size() + 1;
    }

    // If label is found, add to symbol table
    private void processLabel(String line){
        String[] parts = line.split("\\s+");
        if(parts.length > 1 && !isInstruction(parts[0])){
            symbolTable.put(parts[0], locationCounter);
        }
        else if(parts.length > 2 && parts[1].equals("DS")){
            symbolTable.put(parts[0], locationCounter);
        }

    }

    // Check if the line starts with an Instruction ...
    private boolean isInstruction(String label){
        return label.equals("MOVER") || label.equals("MOVEM") ||
                label.equals("ADD") || label.equals("SUB") ||
                label.equals("COMP") || label.equals("BC") ||
                label.equals("READ") || label.equals("PRINT") ||
                label.equals("STOP");
    }


    //Increment Location Counter by 1 everytime...
    private void processInstruction(String line){
        if (!line.startsWith("DS") && !line.startsWith("LTORG")) {
            locationCounter++;
        }
    }

    public void printSymbolTable(){
        System.out.println("Symbol Table:");
        System.out.println("Label\tAdderss");
        for(String label : symbolTable.keySet()){
            int address = symbolTable.get(label);
            System.out.println(label + "\t" + address);
        }
        System.out.println();
    }

    public void printLiteralTable() {
        System.out.println();
        System.out.println("Literal Table");
        System.out.println("Literal\tAdderss");
        for(String[] entry : literalTable){
            System.out.println(entry[0] + "\t" + entry[1]);
        }
    }

    public void printPoolTable() {

        System.out.println();
        System.out.println("Pool Table");
        for(Integer entry : poolTable){
            System.out.println("#" + entry);
        }
    }

    public static void main(String[] args) {
        String[] sourceCode = {

//PROBLEM NO 1
//
//                "START 180",
//                "READ M",
//                "READ N",
//                "LOOP MOVER AREG, M",
//                "MOVER BREG, N",
//                "COMP BREG, =’200’",
//                "BC GT, LOOP",
//                "BACK SUB AREG, M",
//                "COMP AREG, =’500’",
//                "BC LT, BACK",
//                "STOP",
//                "M DS 1",
//                "N DS 1",
//                "END"



// Problem no 2
//                "START 100",
//                "READ A",
//                "READ B",
//                "MOVER AREG, ='50'",
//                "MOVER BREG, =’60’",
//                "ADD AREG, BREG",
//                "LOOP MOVER CREG, A",
//                "ADD CREG, ='10'",
//                "COMP CREG, B",
//                "BC LT, LOOP",
//                "NEXT SUB AREG, ='10'",
//                "COMP AREG, B",
//                "BC GT, NEXT",
//                "STOP",
//                "A DS 1",
//                "B DS 1",
//                "END"



//problem no 3
                "START 100",
                "READ A",
                "MOVER AREG, ='1'",
                "MOVEM AREG, B",
                "MOVER BREG, ='6'",
                "ADD AREG, BREG",
                "COMP AREG, A",
                "BC GT, LAST",
                "LTORG",
                "NEXT SUB AREG, ='1'",
                "MOVER CREG, B",
                "ADD   CREG, ='8'",
                "MOVEM CREG, B",
                "PRINT B",
                "LAST  STOP",
                "A	DS	1",
                "B	DS	1",
                "END"
        };

        SLP assembler = new SLP();
        assembler.firstPass(sourceCode);
        assembler.printSymbolTable();
        assembler.printLiteralTable();
        assembler.printPoolTable();
    }
}