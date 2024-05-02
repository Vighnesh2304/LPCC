package towpasssmacro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Macro {
    // Symbol table to store macro definitions
    private static Map<String, List<String>> macroTable = new HashMap<>();

    // Function to expand macros in the given code
    private static List<String> expandMacros(String code) {
        List<String> expandedCode = new ArrayList<>();
        String[] lines = code.split("\n");

        // Iterate through each line of the code
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();

            // Skip empty lines
            if (line.isEmpty()) {
                continue;
            }

            // Split the line into tokens
            String[] tokens = line.split("\\s+");
            String token = tokens[0];

            // Check if the line contains a macro definition
            if (token.equals("MACRO")) {
                // Extract macro name and parameters
                String macroName = tokens[1];
                List<String> macroDefinition = new ArrayList<>();
                i++;
                // Collect lines until MEND is encountered and store in macroTable
                while (!lines[i].trim().equals("MEND")) {
                    macroDefinition.add(lines[i].trim());
                    i++;
                }
                macroTable.put(macroName, macroDefinition);
            } else if (macroTable.containsKey(token)) {
                // If a macro call is detected, expand the macro
                List<String> macroDefinition = macroTable.get(token);
                expandedCode.addAll(macroDefinition);
            } else {
                // If it's a normal instruction, add it to the expanded code
                expandedCode.add(line);
            }
        }

        return expandedCode;
    }

    // Function to generate Intermediate code from expanded macro code
    private static List<String> generateIntermediateCode(List<String> expandedCode) {
        List<String> intermediateCode = new ArrayList<>();
        int lineNum = 1;

        // Iterate through each line of the expanded code
        for (String line : expandedCode) {
            line = line.trim();

            // Skip empty lines
            if (line.isEmpty()) {
                continue;
            }

            // Split the line into tokens
            String[] tokens = line.split("\\s+");
            String token = tokens[0];

            // Check if the line contains an instruction
            if (token.equals("LOAD") || token.equals("STORE") || token.equals("ADD") || token.equals("SUB")
                    || token.equals("MULT") || token.equals("END")) {
                // Process instructions directly into intermediate code
                intermediateCode.add(lineNum + ": " + line);
                lineNum++;
            }
        }

        return intermediateCode;
    }

    // Main method
    public static void main(String[] args) {
        // Example Macro-Expanded Code
        String code = "LOAD A\n" +
                "MACRO ABC\n" +
                "LOAD p\n" +
                "SUB q\n" +
                "MEND\n" +
                "STORE B\n" +
                "MULT D\n" +
                "MACRO ADD1 ARG\n" +
                "LOAD X\n" +
                "STORE ARG\n" +
                "MEND\n" +
                "LOAD B\n" +
                "MACRO ADD5 A1, A2, A3\n" +
                "STORE A2\n" +
                "ADD1 5\n" +
                "ADD1 10\n" +
                "LOAD A1\n" +
                "LOAD A3\n" +
                "MEND\n" +
                "ADD1 t\n" +
                "ABC\n" +
                "ADD5 D1, D2, D3\n" +
                "END";

        // Expand macros in the code
        List<String> expandedCode = expandMacros(code);

        // Generate Intermediate code from expanded code
        List<String> intermediateCode = generateIntermediateCode(expandedCode);

        // Print Intermediate code
        System.out.println("Intermediate Code:");
        for (String line : intermediateCode) {
            System.out.println(line);
        }
    }
}
