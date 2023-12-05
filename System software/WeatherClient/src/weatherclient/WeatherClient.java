package weatherclient;


import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class WeatherClient {
  public static void main(String[] args) throws InterruptedException { 
      
    String host = "localhost";
    int port = 9090;
    try (Socket serverSocket = new Socket(host, port);
        PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true);
        BufferedReader st = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Connected to server");
            
            out.println("weather");
            out.flush();
            System.out.println("server: " + st.readLine()); //Reads intro line
            System.out.println("server: " + st.readLine()); //Reads intro line
            
            String received_id = st.readLine();           
            out.println(received_id);
            out.flush();
            System.out.println(received_id); 
            
            System.out.println("Enter GPS positioning, temperature and humidity. (GPS,TEMP,HUM)");
            int test = 1;
            //while ((userInput = in.readLine()) != null) { 
            
            while (test != 0) {
                try ( // Weather client's actions
                    DatagramSocket uClient = new DatagramSocket()) {
                    InetAddress add = InetAddress.getByName("localhost");
                    //GPS POSITIONING GPS, TEMPERATURE TEMP, HUMIDITY HUM
                    Scanner data = new Scanner(System.in);
                    String GpsTempHum = data.nextLine();
                    
                    
                    String str = GpsTempHum;
                    byte[] buf = str.getBytes();
                    DatagramPacket p = new DatagramPacket(buf,buf.length,add,9090);
                    uClient.send(p);
                    //out.println(userInput);
                    uClient.close();
                    //System.out.println("echo: " + st.readLine());
                    //System.out.println("Sending to server...");
                    Thread.sleep(10000);
                }
                catch(IOException e){
                    System.err.println("Could not send packet to " + host);
                }
            }
        }
        catch (UnknownHostException ex) {
            System.err.println("Don't know about host " + host);
            System.exit(1);
        }
        catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + host);
            System.exit(1);
        } 
    }

}
