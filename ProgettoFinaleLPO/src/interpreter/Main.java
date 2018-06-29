package interpreter;


import interpreter.parser.*;
import interpreter.parser.StreamTokenizer;
import interpreter.parser.ast.Prog;
import interpreter.visitors.evaluation.Eval;
import interpreter.visitors.evaluation.EvaluatorException;
import interpreter.visitors.typechecking.TypeCheck;
import interpreter.visitors.typechecking.TypecheckerException;

import java.io.*;
import java.util.Arrays;
import java.util.Iterator;

import static java.lang.System.err;

public class Main {

    public static final String INPUT_OPT = "-i";
    public static final String OUTPUT_OPT = "-o";
    private static String inputPath = null, outputPath = null;

    private static void argError() {
        System.err.println("Illegal argument");
        System.exit(1);
    }

    private static String readNext(Iterator<String> it) {
        if (!it.hasNext())
            argError();
        return it.next();
    }

    private static void processArgs(String[] args) {
        Iterator<String> it = Arrays.asList(args).iterator();
        while (it.hasNext()) {
            String curr = it.next();
            if (curr.equals(INPUT_OPT))
                inputPath = readNext(it);
            else if (curr.equals(OUTPUT_OPT))
                outputPath = readNext(it);
            else
                argError();
        }
    }

    private static Reader tryOpenInput() throws FileNotFoundException {
        return inputPath == null ? new InputStreamReader(System.in) : new FileReader(inputPath);
    }

    private static PrintStream  tryOpenOutput() throws FileNotFoundException {
        if (outputPath != null)
            return new PrintStream(new FileOutputStream(new File(outputPath)), true);
        return System.out;
    }

    public static void main(String[] args) {
        processArgs(args);
        try (Tokenizer tokenizer = new StreamTokenizer(tryOpenInput());
             PrintStream out = tryOpenOutput()){
            System.setOut(out);
            Parser parser = new StreamParser(tokenizer);
            Prog prog = parser.parseProg();
            prog.accept(new TypeCheck());
            prog.accept(new Eval());
        } catch (ParserException e) {
            err.println("Syntax error: " + e.getMessage());
        } catch (IOException e) {
            err.println("I/O error: " + e.getMessage());
        } catch (TypecheckerException e) {
            err.println("Static error: " + e.getMessage());
        } catch (EvaluatorException e) {
            err.println("Dynamic error: " + e.getMessage());
        } catch (Throwable e) {
            err.println("Unexpected error.");
            e.printStackTrace();
        }
    }
}
