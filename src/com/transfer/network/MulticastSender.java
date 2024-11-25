package com.transfer.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastSender {
    private final String multicastAddress;
    private final int port;

    public MulticastSender(String multicastAddress, int port) {
        this.multicastAddress = multicastAddress;
        this.port = port;
    }

    public void sendFile(String filePath) {
        File file = new File(filePath);

        if (!file.exists() || !file.isFile()) {
            System.err.println("Le fichier spécifié n'existe pas ou n'est pas un fichier valide : " + filePath);
            return;
        }

        try (MulticastSocket socket = new MulticastSocket();
             FileInputStream fis = new FileInputStream(file)) {

            InetAddress group = InetAddress.getByName(multicastAddress);
            byte[] buffer = new byte[1024];
            int bytesRead;

            System.out.println("Envoi du fichier : " + file.getName());

            // Lire le fichier et envoyer son contenu en fragments de 1024 octets
            while ((bytesRead = fis.read(buffer)) != -1) {
                DatagramPacket packet = new DatagramPacket(buffer, bytesRead, group, port);
                socket.send(packet);
                System.out.println("Envoi d'un paquet de " + bytesRead + " octets.");
            }

            System.out.println("Fichier envoyé en multicast avec succès !");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
