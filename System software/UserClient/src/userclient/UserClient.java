package userclient;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;


public class UserClient extends JFrame{
    
    public static String received_id;
   
    public static List<String> connected_weather_clients = new LinkedList<String>();
    
    public static JPanel log_in_panel = new JPanel();
    public static JPanel auto_show_details_panel = new JPanel();
    public static JPanel show_details_panel = new JPanel();    
    public static JPanel button_panel = new JPanel(); 
    public static JPanel auto_details_panel = new JPanel();
    public static JPanel container_panel = new JPanel();  
    public static JPanel connection_status = new JPanel();
    public static JPanel connection_icon = new JPanel();
    public static JPanel requested_details = new JPanel();
    
    public static JLabel connection_info;
    public static JLabel info;
    public static JLabel seperate_label;
    public static JLabel Login_instruction;
    
    public static JButton btn = new JButton();
    public static JButton log_in_btn = new JButton();
    
    public static JTextField username = new JTextField();
    public static JPasswordField password = new JPasswordField();

    
    public static BevelBorder raisedBevel = (BevelBorder) BorderFactory.createBevelBorder(BevelBorder.RAISED);
    public static BevelBorder loweredBevel = (BevelBorder) BorderFactory.createBevelBorder(BevelBorder.LOWERED);
    public static Border border = BorderFactory.createCompoundBorder(raisedBevel, loweredBevel);
    

   
  public static void main(String[] args) throws SocketException, UnknownHostException, IOException {
      
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
    public void run() {
            createAndShowGUI();
        }
    });
    
    //reading from db file
    int n = 0;
    int p = 1;
    String user_line ;
    String pass_line ;
    
    user_line = Files.readAllLines(Paths.get("filename.txt")).get(n);    
    pass_line = Files.readAllLines(Paths.get("filename.txt")).get(p);
       
    
    Login_instruction = new JLabel("Enter details to login:");
    connection_info = new JLabel("Connection to server");
    info = new JLabel("This is an automatic display of field details:");
    seperate_label = new JLabel("  ");
    
    btn.setText("request fields details");
    btn.setBounds(50,90,250,30);
 
    
    log_in_btn = new JButton("log in");
    log_in_btn.setAlignmentX(log_in_btn.CENTER_ALIGNMENT);
    log_in_btn.setSize(200,200);
    
    
    container_panel.add(connection_info);
    container_panel.add(connection_icon);
    connection_icon.setBackground(Color.red);
    
    
    String host = "localhost";
    int port = 9090;  
    
    try (Socket serverSocket = new Socket(host, port);
    PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true);
    BufferedReader st = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
        System.out.println("Connected to server");

        out.println("user");       
        out.flush();
        
        
        if(st.readLine()!=null){      
            container_panel.add(connection_info);
            container_panel.add(connection_icon);            
            connection_icon.setBackground(Color.green);           
        }
        else{
            container_panel.add(connection_info);           
            container_panel.add(connection_icon);
            connection_icon.setBackground(Color.red);      
        }
        
        
        auto_show_details_panel.add(info);
        auto_show_details_panel.add(seperate_label);
        info.setAlignmentX(info.CENTER_ALIGNMENT);
        
        
        for(int i=0; i<6;++i){          
            JLabel field_details;
            field_details = new JLabel();
            
            auto_show_details_panel.add(field_details);
            field_details.setText(st.readLine());
            field_details.setAlignmentX(field_details.CENTER_ALIGNMENT);
                        
            out.println("user");
        }    
        auto_show_details_panel.add(button_panel);                
    }
      
    btn.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
        try (Socket serverSocket = new Socket(host, port);
                    
            PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true);
            BufferedReader st = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
                System.out.println("Connected to server");                        

                out.println("user");  
                              
                requested_details.add(seperate_label);
                
                for(int i=0; i<7;++i){
                JLabel label;
                label = new JLabel();
                label.setBounds(50,90,250,30);
                requested_details.add(label);
                              
                label.setText(st.readLine());
                label.setAlignmentX(label.CENTER_ALIGNMENT);

                out.println("user");
            
            }
            
        }
        catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            System.exit(1);
        }
        catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + host);
            System.exit(1);
        }
        }
    });
    
    
    log_in_btn.addActionListener(new ActionListener() {  
    
    @Override
    public void actionPerformed(ActionEvent arg0) {
        String username_input = username.getText();
        String password_input = password.getText();
        System.out.println(username_input);
        System.out.println(password_input);
      
        if(username_input.equals(user_line) && password_input.equals(pass_line) ){
            auto_show_details_panel.setVisible(true);
            show_details_panel.setVisible(true);
        }
        else{
            infoBox(username_input, username_input);
        }
    }});
    
  }
  
    public static void createAndShowGUI() {
        JFrame frame = new JFrame();
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.setSize(800, 220);
        
        addComponentsToLoginPane(frame.getContentPane());
        addComponentsToPane(frame.getContentPane());
        
        
        frame.setVisible(true);
  }

        public static void addComponentsToPane(Container pane)
        {
           
            GridLayout auto_show_grid_layout = new GridLayout(1,3);
            pane.setLayout(auto_show_grid_layout);
            
            auto_show_details_panel.setLayout(auto_show_grid_layout);
            auto_show_details_panel.setLayout((new BoxLayout(auto_show_details_panel, BoxLayout.Y_AXIS)));             
            container_panel.setLayout((new BoxLayout(container_panel, BoxLayout.X_AXIS)));
            requested_details.setLayout((new BoxLayout(requested_details, BoxLayout.Y_AXIS)));
            
                              
            pane.add(auto_show_details_panel);
            pane.add(show_details_panel);
                    
            auto_show_details_panel.add(auto_details_panel);  
            auto_show_details_panel.add(button_panel);
            show_details_panel.add(connection_status);
            show_details_panel.add(container_panel);
            show_details_panel.add(connection_status);
            show_details_panel.add(requested_details);
            button_panel.add(btn);

            auto_show_details_panel.setVisible(false);
            show_details_panel.setVisible(false); 
            
            auto_show_details_panel.setBorder(border);
            show_details_panel.setBorder(border);
            
            username.setPreferredSize(new Dimension(200, 30));
            password.setPreferredSize(new Dimension(200, 30));
    }
        public static void addComponentsToLoginPane(Container loginPane)
        {
            
            loginPane.add(log_in_panel);
            
            Box box = Box.createVerticalBox();
            box.add(Box.createVerticalGlue());
            box.add(Login_instruction);
            box.add(Box.createRigidArea(new Dimension(5,20)));
            box.add(username);
            box.add(Box.createRigidArea(new Dimension(5,20)));
            box.add(password);
            box.add(Box.createRigidArea(new Dimension(5,20)));
            box.add(Box.createVerticalGlue());
            
            log_in_panel.add(box);
            box.add(log_in_btn);          
        }
        
        public static void infoBox(String infoMessage, String titleBar)
        {
            JOptionPane.showMessageDialog(null, "Your login credentials don't match an account", "Warning: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
        }
}
