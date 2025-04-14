package org.alexstk.safka.consumer;

import java.io.IOException;

public class SafkaConsumer {
    public static void main(String[] args) throws IOException {
        MessageConsumer client = new MessageConsumer("localhost", 7500);
        client.startMenu();
    }
}
