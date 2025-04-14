package org.alexstk.safka.orchestrator;

public class SafkaOrchestrator {
    public static void main(String[] args){
        MessageOrchestrator orchestrator = new MessageOrchestrator(7500);
        orchestrator.startListening();
    }
}
