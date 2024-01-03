package org.vspaz.kpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class Kpl {
    private final String[] args;

    public Kpl(final String[] args) {
        this.args = args;
    }

    private void executeFile(String filePath) throws IOException {
        final byte[] fileContents = Files.readAllBytes(Paths.get(filePath));
        parse(new String(fileContents, Charset.defaultCharset()));
    }

    private void parse(String sourceCode) {

    }

    private void runPrompt() throws IOException {
        final var in = new InputStreamReader(System.in);
        final var reader = new BufferedReader(in);

        for (;;) {
            System.out.println("> ");
            String line = reader.readLine();
            if (Objects.isNull(line)) break;
            parse(line);
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
}
