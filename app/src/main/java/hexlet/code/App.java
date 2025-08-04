package hexlet.code;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
        name = "gendiff",
        description = "Compares two configuration files and shows a difference.",
        mixinStandardHelpOptions = true,  // Автоматически добавляет --help и --version
        version = "gendiff 1.0"           // Версия для опции --version
)

public class App implements Runnable {
    public static void main(String[] args) {
        // Важно: используем execute(), а не просто new CommandLine()
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        System.out.println("Hello, World!");
    }
}
