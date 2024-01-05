package org.vspaz.kpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class Kpl {

    static boolean hasError = false;
    private final String[] args;

    public Kpl(final String[] args) {
        this.args = args;
    }

    private void executeFile(String filePath) throws IOException {
        final byte[] fileContents = Files.readAllBytes(Paths.get(filePath));
        parse(new String(fileContents, Charset.defaultCharset()));
    }

    private void parse(String sourceCode) {
        Scanner scanner = new Scanner(sourceCode);
        for (Token token : scanner.scanTokens()) {
            System.out.println(token);
        }
    }

    private void runPrompt() throws IOException {
        final var in = new InputStreamReader(System.in);
        final var reader = new BufferedReader(in);

        for (;;) {
            System.out.println("> ");
            String line = reader.readLine();
            if (Objects.isNull(line)) break;
            parse(line);
            hasError = false;
        }
    }

    public void run() {
        try {
            if (args.length == 1) {
                executeFile(args[0]);
            } else if (args.length > 1) {
                System.out.println("kpl [file]");
                System.exit(-1);
            }
            runPrompt();
        }
        catch (Exception e) {
            System.out.printf("error occurred %s,\nexiting...\n", e);
        }
    }

    private static void reportError(int lineNumber, String where, String message) {
        hasError = true;
        System.out.printf("[line %d] Error %s: %s", lineNumber, where, message);

    }

    static void error(int lineNo, String where, String message) {
        reportError(lineNo, where, message);
    }
}
