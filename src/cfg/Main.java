package cfg;

import cfg.cli.AppContext;
import cfg.cli.CommandRouter;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        AppContext context = new AppContext();
        CommandRouter router = new CommandRouter(context);

        System.out.println("Context-Free Grammar Tool");
        System.out.println("Type 'help' for a list of commands.");
        System.out.println();

        Scanner scanner = new Scanner(System.in);

        while (context.isRunning()) {
            System.out.print("> ");
            if (!scanner.hasNextLine()) break;
            String line = scanner.nextLine();
            router.dispatch(line);
        }

        scanner.close();
    }
}
