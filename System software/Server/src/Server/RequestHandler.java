package Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

public class RequestHandler implements Runnable {
  
    private final Socket client;
    ServerSocket serverSocket = null;
    
    public static List<String> connected_weather_client = new LinkedList<String>();
    public static String received_id;
    
    public static JPanel pane = new JPanel();
    public static JPanel intro = new JPanel();
    public static JPanel connected_weathers_panel = new JPanel();
    public static JLabel welcome = new JLabel();
    public static JLabel status = new JLabel();
    public static JLabel seperate = new JLabel();
    public static JButton show = new JButton();
    public static JComboBox connected_weathers = new JComboBox();
    
    public static BevelBorder raisedBevel = (BevelBorder) BorderFactory.createBevelBorder(BevelBorder.RAISED);
    public static BevelBorder loweredBevel = (BevelBorder) BorderFactory.createBevelBorder(BevelBorder.LOWERED);
    public static Border border = BorderFactory.createCompoundBorder(raisedBevel, loweredBevel);
    

    public RequestHandler(Socket client) {
      this.client = client;
    }

    @Override
    public void run() {      
	try (
              
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));) {
	        System.out.println("Thread started with name:" + Thread.currentThread().getName());
	        String userInput;
                switch (in.readLine()){
                        case "user" -> {
                            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                            Random rand = new Random();
                            int field_details = rand.nextInt(10) + 1 ;
                            LocalDate date = LocalDate.now();
                            out.println("Hello user client");
                            if(field_details < 5){                                     
                                    out.println("Date:" + date);
                                    out.println("Water supply: working \n Field surface: 3.6 ha\n Dominant phylum:potatoes, lettuce \n Ecpected field harvest: 1 ton " );
                                    writer.newLine();
                                    writer.flush();
                                }
                                else{                                   
                                    out.println("Date:" + date);
                                    out.println("Water supply: not working \n Field surface: 3.6 ha \n Dominant phylum:potatoes, lettuce \n Ecpected field harvest: 0.8 ton" );                               
                                    writer.newLine();
                                    writer.flush();                                
                                }                                                      
                            while ((userInput = in.readLine()) != null) {                                
                                if(field_details < 5){                                     
                                    out.println("Date:" + date);
                                    out.println("Water supply:working \n Field surface: 3.6 ha \n Dominant phylum:potatoes, lettuce\n Ecpected field harvest: 1 ton " );
                                    writer.newLine();
                                    writer.flush();
                                }
                                else{                                   
                                    out.println("Date:" + date);
                                    out.println("Water supply: not working \n Field surface: 3.6 ha\n Ecpected field harvest: 0.8 ton" );                                
                                    writer.newLine();
                                    writer.flush();                                
                                }                               
                            }
                        }
                        case "weather" -> {
                            PrintWriter outWeather = new PrintWriter(client.getOutputStream(), true);
                            Random rand = new Random();
                            int ID = rand.nextInt(500) + 1;
                            outWeather.println("Hello weather client\nRegistered with ID: " + ID);
                            outWeather.println(ID);

                            
                            received_id = in.readLine();
                            System.out.println(received_id);
                            
                            writer.newLine();
                            writer.flush();
                            
                            //DatagramSocket socket = new DatagramSocket(9090);
                            byte[] buf = new byte[1024];
                            DatagramPacket packet = new DatagramPacket(buf,buf.length);
                                    
                            //while ((userInput = in.readLine()) != null) {
                            while (packet != null) {
                                DatagramSocket socket = new DatagramSocket(9090);
                                 socket.receive(packet);
                                 buf = packet.getData();
                                 socket.close();
                                 String response = new String(buf);
                                 System.out.println("Response Data from " + ID + " : " + response);
                                 Thread.sleep(10000);
                           }
                        }
                    }
            } 
        catch (IOException e) {
	    System.out.println("I/O exception: " + e);
	} 
        catch (Exception ex) {
	    System.out.println("Exception in Thread Run. Exception : " + ex);
	}
    }
    
        public static void createAndShowGUI() {
            
        JFrame frame = new JFrame();
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.setSize(600, 220);
        
        frame.setVisible(true);
        
        frame.setContentPane(pane);
        

        pane.setLayout((new BoxLayout(pane, BoxLayout.Y_AXIS)));    
        intro.setLayout((new BoxLayout(intro, BoxLayout.Y_AXIS)));
        connected_weathers_panel.setLayout((new BoxLayout(connected_weathers_panel, BoxLayout.Y_AXIS)));

        welcome = new JLabel("Welcome ");
        status = new JLabel("Waiting for connections ...");
        seperate = new JLabel("   ");

        pane.add(intro);
        pane.add(connected_weathers_panel);
        intro.add(welcome);
        intro.add(status);
        intro.add(seperate);
        intro.add(show);
        connected_weathers_panel.add(connected_weathers);
        connected_weathers.setVisible(false);

        welcome.setAlignmentX(welcome.CENTER_ALIGNMENT);
        status.setAlignmentX(status.CENTER_ALIGNMENT);
        show.setAlignmentX(show.CENTER_ALIGNMENT);

        connected_weathers_panel.setBorder(border);

        show.setText("Show Connected stations");

        show.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
            connected_weathers.setVisible(true);
            connected_weather_client.add(received_id);
            if((((DefaultComboBoxModel)connected_weathers.getModel()).getIndexOf(received_id) == -1) && connected_weather_client.contains(received_id)) {                 
                connected_weathers.addItem(received_id);                         
            }
        }
        });
    }
}
