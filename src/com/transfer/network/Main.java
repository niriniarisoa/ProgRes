package com.transfer.network;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String multicastAddress = "230.0.0.0"; // Adresse multicast
        int port = 4446;

        // Lancer le receiver dans un thread séparé
        MulticastReceiver receiver = new MulticastReceiver(multicastAddress, port);
        new Thread(receiver::startReception).start();

        // Demander à l'utilisateur s'il veut envoyer un fichier
        Scanner scanner = new Scanner(System.in);
        System.out.println("Voulez-vous envoyer un fichier ? (oui/non)");
        String response = scanner.nextLine();

        if (response.equalsIgnoreCase("oui")) {
            // Chemin vers le fichier à envoyer
            System.out.println("Entrez le chemin complet du fichier à envoyer : ");
            String filePath = scanner.nextLine();

            MulticastSender sender = new MulticastSender(multicastAddress, port);

            // Lancer l'envoi du fichier
            sender.sendFile(filePath);
        }

        scanner.close();
    }
}
