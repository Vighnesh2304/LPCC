%{
#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>

// Function prototypes
int yylex(void); // Lexical analyzer function
void yyerror(const char *); // Error handling function

// Define YYSTYPE as a double
#define YYSTYPE double

%}

%token NUM // Define NUM as a token representing numbers

%left '+' '-' // Define precedence and associativity for addition and subtraction
%left '*' '/' // Define precedence and associativity for multiplication and division

%%

expression:  
expr 
{
	printf("Result: %.2f\n", $1); // Print the result of the expression
 	return 0; // Return success
}
    ;
    
expr: expr '+' expr     { $$ = $1 + $3; } // Addition operation
    | expr '-' expr     { $$ = $1 - $3; } // Subtraction operation
    | expr '*' expr     { $$ = $1 * $3; } // Multiplication operation
    | expr '/' expr     { $$ = $1 / $3; } // Division operation
    | '(' expr ')'      { $$ = $2; } // Parenthesized expression
    | NUM               { $$ = $1; } // Number
    ;


%%

int yylex() {
    int c;
    do {
        c = getchar();
    } while (c == ' ' || c == '\t'); // Skip whitespace characters
    if (isdigit(c) || c == '.') { // Check if the character is a digit or a decimal point
        ungetc(c, stdin); // Put the character back into the input stream
        scanf("%lf", &yylval); // Read a double from the input stream and store it in yylval
        return NUM; // Return NUM token
    }
    return c; // Return the character read
}

void yyerror(const char *s) {
    fprintf(stderr, "%s\n", s); // Print error message to stderr
}

int main() {
    printf("\nEnter Any Arithmetic Expression :");
    yyparse(); // Parse the input expression
    return 0;
}
