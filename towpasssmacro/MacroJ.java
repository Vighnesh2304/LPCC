package towpasssmacro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MacroJ {
    // Symbol table to store macro definitions
    private static final Map<String, List<String>> macroTable = new HashMap<>();

    // Function to expand macros in the code
    private static List<String> expandMacros(List<String> code) {
        List<String> expandedCode = new ArrayList<>();

        for (String line : code) {
            // Splitting the line into tokens
            String[] tokens = line.split("\\s+", 2);
            String token = tokens[0];

            if (macroTable.containsKey(token)) {
                // Replace macro call with macro definition
                List<String> macroDefinition = macroTable.get(token);
                expandedCode.addAll(macroDefinition);
            } else {
                // Regular instruction, add to expanded code
                expandedCode.add(line);
            }
        }

        return expandedCode;
    }

    // Function to generate Intermediate code from expanded code
    private static List<String> generateIntermediateCode(List<String> code) {
        List<String> intermediateCode = new ArrayList<>();
        int lineNum = 1;

        for (String line : code) {
            // Adding line numbers to the intermediate code
            intermediateCode.add(lineNum + ": " + line);
            lineNum++;
        }

        return intermediateCode;
    }

    public static void main(String[] args) {
        // Example Macro-Expanded Code
        List<String> code = List.of(
                "LOAD J",
                "STORE M",
                "MACRO EST",
                "LOAD P",
                "ADD V",
                "MEND",
                "LOAD S",
                "MACRO ADD7 P4, P5, P6",
                "LOAD P5",
                "LOAD e",
                "ADD d",
                "MEND",
                "LOAD S",
                "MACRO SUB4 ABC",
                "LOAD U",
                "STORE ABC",
                "MEND",
                "SUB4 XYZ",
                "SUB 8",
                "SUB 2",
                "STORE P4",
                "STORE P6",
                "MEND",
                "EST",
                "ADD7 C4, C5",
                "SUB4 z",
                "END"
        );

        // Populate macro table with macro definitions
        macroTable.put("EST", List.of("LOAD P", "ADD V"));
        macroTable.put("ADD7", List.of("LOAD P5", "LOAD e", "ADD d"));
        macroTable.put("SUB4", List.of("LOAD U", "STORE ABC"));

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
