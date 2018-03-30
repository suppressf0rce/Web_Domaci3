package server;

import model.Contact;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {

    public static void main(String[] args) {

        List<Contact> addressBook = Collections.synchronizedList(new ArrayList<>());

        try {
            ServerSocket ss = new ServerSocket(6789);

            while(true){
                Socket s = ss.accept();
                Thread thread = new Thread(new ServerThread(addressBook, s));
                thread.start();
                System.out.println("Client connected");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}