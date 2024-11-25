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

            // Envoyer le nom du fichier avant son contenu
            String fileName = file.getName();
            byte[] fileNameBytes = fileName.getBytes();
            DatagramPacket namePacket = new DatagramPacket(fileNameBytes, fileNameBytes.length, group, port);
            socket.send(namePacket);

            // Lire le fichier et envoyer son contenu en fragments
            while ((bytesRead = fis.read(buffer)) != -1) {
                DatagramPacket packet = new DatagramPacket(buffer, bytesRead, group, port);
                socket.send(packet);
                System.out.println("Envoi d'un paquet de " + bytesRead + " octets.");
            }

            // Indiquer la fin du fichier
            byte[] endSignal = "END".getBytes();
            DatagramPacket endPacket = new DatagramPacket(endSignal, endSignal.length, group, port);
            socket.send(endPacket);

            System.out.println("Fichier envoyé en multicast avec succès !");
        } catch (IOException e) {
            System.err.println("Erreur lors de l'envoi du fichier : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
