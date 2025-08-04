package hexlet.code;

import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(
        name = "gendiff",
        description = "Compares two configuration files and shows a difference.",
        mixinStandardHelpOptions = true,  // Автоматически добавляет --help и --version
        version = "gendiff 1.0"           // Версия для опции --version
)

public class App implements Runnable {

    @Parameters(index = "0", description = "path to first file")
    private String filepath1;

    @Parameters(index = "1", description = "path to second file")
    private String filepath2;

    @Option(names = { "-f", "--format" },
            description = "output format [default: stylish]",
            defaultValue = "stylish"
    )
    private String format;

    public static void main(String[] args) {
        // Важно: используем execute(), а не просто new CommandLine()
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }


    @Override
    public void run() {
        System.out.println("Using format: " + format);
    }
}
