import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class caca {
    String ins;
    StringTokenizer st;

    void yyerror(String s)
    {
        System.out.println("par:"+s);
    }

    boolean newline;
    int yylex()
    {
        String s;
        int tok;
        Double d;
        //System.out.print("yylex ");
        if (!st.hasMoreTokens())
            if (!newline)
            {
                newline=true;
                return '\n'; //So we look like classic YACC example
            }
            else
                return 0;
        s = st.nextToken();
        //System.out.println("tok:"+s);
        try
        {
            d = Double.valueOf(s);/*this may fail*/
            yylval = new ParserVal(d.doubleValue()); //SEE BELOW
            tok = NUM;
        }
        catch (Exception e)
        {
            tok = s.charAt(0);/*if not float, return char*/
        }
        return tok;
    }

    void dotest()
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("BYACC/J Calculator Demo");
        System.out.println("Note: Since this example uses the StringTokenizer");
        System.out.println("for simplicity, you will need to separate the items");
        System.out.println("with spaces, i.e.: '( 3 + 5 ) * 2'");
        while (true)
        {
            System.out.print("expression:");
            try
            {
                ins = in.readLine();
            }
            catch (Exception e)
            {
            }
            st = new StringTokenizer(ins);
            newline=false;
            yyparse();
        }
    }

    public static void main(String args[])
    {
        Parser par = new Parser(false);
        par.dotest();
    }
}