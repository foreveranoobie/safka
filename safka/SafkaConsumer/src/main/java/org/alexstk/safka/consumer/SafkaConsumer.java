package org.alexstk.safka.consumer;

import java.io.IOException;

public class SafkaConsumer {

    public static void main(String[] args) throws IOException {
        MessageConsumer client = new MessageConsumer(getOrchestratorHostFromArgs(args),
            getOrchestratorPortFromArgs(args));
        client.startMenu();
    }

    private static String getOrchestratorHostFromArgs(String[] args) {
        if (args.length == 0) {
            System.out.println(
                "Orchestrator's host is not given in arguments, using localhost instead");
            return "localhost";
        } else {
            return args[0];
        }
    }

    private static int getOrchestratorPortFromArgs(String[] args) {
        {
            if (args.length < 2 || !args[1].matches("[0-9]+")) {
                System.out.println(
                    "Orchestrator port has not been provided. Trying to use port 7500");
                return 7500;
            } else {
                return Integer.parseInt(args[1]);
            }
        }
    }
}
