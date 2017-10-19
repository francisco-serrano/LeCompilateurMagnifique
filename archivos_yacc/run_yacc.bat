yacc -J gramatica.y

copy ParserVal.java temp.java
echo.package parser; > ParserVal.java
type temp.java >> ParserVal.java
del temp.java

copy "./Parser.java" "../src/main/java/parser/Parser.java"
copy "./ParserVal.java" "../src/main/java/parser/ParserVal.java"
timeout 60