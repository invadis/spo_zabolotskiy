import java.util.ArrayList;
import java.util.List;

public class Lexer {

    public static void main(String[] args) {
        StringBuilder input = new StringBuilder(lookupInput(args));
        List<Lexeme> lexemes = new ArrayList<>();

        while (input.charAt(0) != '$') {
            Lexeme lexeme = NextLexeme(input);
            lexemes.add(lexeme);
            input.delete(0, lexeme.getValue().length());
        }
        print(lexemes);
    }
    private static final List<Terminal> TERMINALS = List.of(
            new Terminal("VAR", "[a-zA-Z][a-zA-Z0-9]*"),
            new Terminal("WHILE_KEYWORD", "while", 1),
            new Terminal("OP", "[+-/*]"),
            new Terminal("WS", "\\s+"),
            new Terminal("IF_KEYWORD", "if", 1),
            new Terminal("ELSE_KEYWORD", "else", 1),
            new Terminal("LBR", "\\("),
            new Terminal("RBR", "\\)"),
            new Terminal("LOGICAL_OP", "<|>|==|<=|>="),
            new Terminal("ASSIGN", "="),
            new Terminal("SC", ";"),
            new Terminal("NUMBER", "[0-9]+"),
            new Terminal("L_S_BR", "\\{"),
            new Terminal("R_S_BR", "\\}"),
            new Terminal("DO_KEYWORD", "do", 1)
    );

    private static Lexeme NextLexeme(StringBuilder input) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(input.charAt(0));

        if (TerminalMatches(buffer)) {
            while (TerminalMatches(buffer) && buffer.length() != input.length()) {
                buffer.append(input.charAt(buffer.length()));
            }
            buffer.deleteCharAt(buffer.length() - 1);

            List<Terminal> terminals = lookupTerminals(buffer);

            return new Lexeme(getPriorityTerminal(terminals), buffer.toString());
        } else {
            throw new RuntimeException("Unexpected symbol " + buffer);
        }
    }

    private static Terminal getPriorityTerminal(List<Terminal> terminals) {
        Terminal priorityTerminal = terminals.get(0);

        for (Terminal terminal : terminals) {
            if (terminal.getPriority() > priorityTerminal.getPriority()) {
                priorityTerminal = terminal;
            }
        }
        return priorityTerminal;
    }

    private static boolean TerminalMatches(StringBuilder buffer) {
        return lookupTerminals(buffer).size() != 0;
    }

    private static List<Terminal> lookupTerminals(StringBuilder buffer) {
        List<Terminal> terminals = new ArrayList<>();

        for (Terminal terminal : TERMINALS) {
            if (terminal.matches(buffer)) {
                terminals.add(terminal);
            }
        }
        return terminals;
    }

    private static String lookupInput(String[] arrays) {
        if (arrays.length == 0) {
            throw new IllegalArgumentException("Input string not found");
        }
        return arrays[0];
    }

    private static void print(List<Lexeme> lexemes) {
        for (Lexeme lexeme : lexemes) {
            System.out.printf("[%s, %s]%n",
                    lexeme.getTerminal().getIdentifier(),
                    lexeme.getValue());
        }
    }
}
