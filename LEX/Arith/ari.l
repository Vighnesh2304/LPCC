%{
#include <stdio.h>

%}

%%

"int"          { printf("Keyword: int\n"); }
"printf"          { printf("Keyword: int\n"); }
"="            { printf("Assignment operator\n"); }
";"            { printf("Semicolon\n"); }
"("            { printf("Left parenthesis\n"); }
")"            { printf("Right parenthesis\n"); }
"-"            { printf("Subtraction operator\n"); }
"%d"            { printf("Format Specifier\n"); }
[0-9]+         { printf("Integer: %s\n", yytext); }
[a-zA-Z_][a-zA-Z0-9_]*  { printf("Identifier: %s\n", yytext); }
[ \t\n]        ; /* Ignore whitespace and newline */
.              { printf("Unrecognized token: %s\n", yytext); }

%%
int yywrap(){
    return 1;
}
int main() {
    yyin = fopen("input.txt", "r");
    yylex();
    fclose(yyin);
    return 0;
}
