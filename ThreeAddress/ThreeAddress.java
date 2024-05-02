import java.io.*;

public class ThreeAddress {

    // Define operator precedence
    private static final char[][] precedence = {
            {'/', '1'},
            {'*', '1'},
            {'+', '2'},
            {'-', '2'}
    };

    // Function to get precedence of an operator
    private static int precedenceOf(String t) {
        char token = t.charAt(0);
        for (int i = 0; i < precedence.length; i++) {
            if (token == precedence[i][0]) {
                return Integer.parseInt(precedence[i][1] + "");
            }
        }
        return -1; // Default value if operator not found
    }

    // Main function
    public static void main(String[] args) throws Exception {
        int i, j, opc = 0;
        char token;
        boolean processed[];
        String[][] operators = new String[10][2];
        String expr = "", temp;

        // Input expression from the user
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("\nEnter an expression for calculating Address codes: ");
        expr = in.readLine();

        // Initialize processed array to keep track of processed characters
        processed = new boolean[expr.length()];
        for (i = 0; i < processed.length; i++) {
            processed[i] = false;
        }

        // Identify operators in the expression and store them in the operators array
        for (i = 0; i < expr.length(); i++) {
            token = expr.charAt(i);
            for (j = 0; j < precedence.length; j++) {
                if (token == precedence[j][0]) {
                    operators[opc][0] = token + "";
                    operators[opc][1] = i + ""; // Store the position of the operator in the expression
                    opc++;
                    break;
                }
            }
        }

        // Print the operators found in the expression along with their locations
        System.out.println("\nOperators: \nOperators \tLocation number\n");
        for (i = 0; i < opc; i++) {
            System.out.println(operators[i][0] + "\t\t" + operators[i][1]);
        }

        // Sort the operators based on their precedence
        for (i = opc - 1; i >= 0; i--) {
            for (j = 0; j < i; j++) {
                if (precedenceOf(operators[j][0]) > precedenceOf(operators[j + 1][0])) {
                    // Swap positions if the precedence of the current operator is higher than the next one
                    temp = operators[j][0];
                    operators[j][0] = operators[j + 1][0];
                    operators[j + 1][0] = temp;
                    temp = operators[j][1];
                    operators[j][1] = operators[j + 1][1];
                    operators[j + 1][1] = temp;
                }
            }
        }

        // Print the sorted operators along with their locations
        System.out.println("\nOperators sorted in their precedence: \nOperators \tLocation number\n");
        for (i = 0; i < opc; i++) {
            System.out.println(operators[i][0] + "\t\t" + operators[i][1]);
        }
        System.out.println();

        // Generate three-address code for the expression
        for (i = 0; i < opc; i++) {
            j = Integer.parseInt(operators[i][1] + ""); // Get the position of the current operator
            String op1 = "", op2 = "";
            if (processed[j - 1] == true) {
                if (precedenceOf(operators[i - 1][0]) == precedenceOf(operators[i][0])) {
                    op1 = "t" + i; // If the previous operator has the same precedence, use the same temporary variable
                } else {
                    // Otherwise, find the temporary variable for the left operand
                    for (int x = 0; x < opc; x++) {
                        if ((j - 2) == Integer.parseInt(operators[x][1])) {
                            op1 = "t" + (x + 1) + "";
                        }
                    }
                }
            } else {
                op1 = expr.charAt(j - 1) + ""; // If the left operand is not processed, use it directly
            }
            if (processed[j + 1] == true) {
                // If the right operand is processed, find the temporary variable for it
                for (int x = 0; x < opc; x++) {
                    if ((j + 2) == Integer.parseInt(operators[x][1])) {
                        op2 = "t" + (x + 1) + "";
                    }
                }
            } else {
                op2 = expr.charAt(j + 1) + ""; // If the right operand is not processed, use it directly
            }
            // Print the three-address code for the operation
            System.out.println("t" + (i + 1) + " = " + op1 + operators[i][0] + op2);
            processed[j] = processed[j - 1] = processed[j + 1] = true; // Mark the operands as processed
        }
    }
}
