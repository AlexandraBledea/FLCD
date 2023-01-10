%{
#include <stdio.h>
#include <stdlib.h>

#define YYDEBUG 1 
%}

%token INT
%token STRING
%token CHAR
%token READ
%token WRITE
%token IF
%token ELSE
%token FOR
%token WHILE
%token RETURN
%token START
%token ARRAY

%token plus
%token minus
%token mul
%token division
%token mod
%token lessOrEqual
%token moreOrEqual
%token less
%token more
%token equal
%token different
%token eq



%token leftCurlyBracket
%token rightCurlyBracket
%token leftRoundBracket
%token rightRoundBracket
%token leftBracket
%token rightBracket
%token colon
%token semicolon
%token comma
%token apostrophe
%token quote

%token IDENTIFIER
%token NUMBER_CONST
%token STRING_CONST
%token CHAR_CONST

%start program

%%

program : START compound_statement

statement : declaration semicolon | assignment_statement | return_statement semicolon | iostmt semicolon | if_statement | while_statement | for_statement

statement_list : statement | statement statement_list

compound_statement : leftCurlyBracket statement_list rightCurlyBracket

expression : expression plus term | expression minus term | term

term : term mul factor | term division factor | term mod factor | factor 

factor : leftRoundBracket expression rightRoundBracket | IDENTIFIER | constant

constant : NUMBER_CONST | STRING_CONST | CHAR_CONST 

iostmt : READ leftRoundBracket IDENTIFIER rightRoundBracket | WRITE leftRoundBracket IDENTIFIER rightRoundBracket | WRITE leftRoundBracket constant rightRoundBracket

simple_type : INT | STRING | CHAR

array_declaration : ARRAY simple_type IDENTIFIER leftBracket rightBracket

declaration : simple_type IDENTIFIER | array_declaration 

assignment_statement : IDENTIFIER eq expression semicolon

if_statement : IF leftRoundBracket condition rightRoundBracket compound_statement | IF leftRoundBracket condition rightRoundBracket compound_statement ELSE compound_statement

while_statement : WHILE leftRoundBracket condition rightRoundBracket compound_statement

return_statement : RETURN expression 

for_statement : FOR for_header compound_statement

for_header : leftRoundBracket INT assignment_statement condition assignment_statement rightRoundBracket

condition : expression relation expression

relation : less | lessOrEqual | equal | different | moreOrEqual | more


%%

yyerror(char *s)
{	
	printf("%s\n",s);
}

extern FILE *yyin;

main(int argc, char **argv)
{
	if(argc>1) yyin :  fopen(argv[1],"r");
	if(argc>2 && !strcmp(argv[2],"-d")) yydebug: 1;
	if(!yyparse()) fprintf(stderr, "\tProgram is syntactically correct.\n");
}