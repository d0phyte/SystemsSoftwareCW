package Server;

import static Server.RequestHandler.createAndShowGUI;
import java.net.*;   
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) throws SocketException, IOException {
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
        
        System.out.println("Start of main");
        int portNumber = 9090;
        ExecutorService executor = null;
        try (ServerSocket serverSocket = new ServerSocket(portNumber);) {
            executor = Executors.newFixedThreadPool(5);
            System.out.println("Waiting for clients");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                Runnable worker = new RequestHandler(clientSocket);
                executor.execute(worker);
            }
        }
        catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        } 
        finally {
            if (executor != null) {
            executor.shutdown();
            }
        }

        

    }
}

