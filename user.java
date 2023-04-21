import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
public class user extends JFrame{

    Socket socket;
    BufferedReader br;
    PrintWriter out;
    private JLabel heading = new JLabel("User Area");
    private JTextArea messageArea= new JTextArea();
    private JTextField messageInput= new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20);
    public user(){
        try {
            System.out.println("Sending request to server");
            socket= new Socket("127.0.0.1",8328);
            System.out.println("Connection done.");
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out= new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvents();
            startReading();
            // startWriting();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }
    /**
     * 
     */
    private void handleEvents(){
       messageInput.addKeyListener(new KeyListener() {

        @Override
        public void keyTyped(KeyEvent e) {
            // TODO Auto-generated method stub
            // throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
        }

        @Override
        public void keyPressed(KeyEvent e) {
            // TODO Auto-generated method stub
            // throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
        }

        @Override
        public void keyReleased(KeyEvent e) {
            // TODO Auto-generated method stub
            // throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
            if(e.getKeyCode()==10){
                String msg=messageInput.getText();
                messageArea.append("Me:"+msg+"\n");
                out.println(msg);
                out.flush();
                messageInput.setText("");
                messageInput.requestFocus();
            }
        }
        
       });
    }
    private void createGUI(){

    this.setTitle("User Messenger[END]");
    this.setSize(600,700);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    heading.setFont(font);
    messageArea.setFont(font);
    messageInput.setFont(font);

    heading.setIcon(new ImageIcon("logo.jpg"));
    heading.setHorizontalTextPosition(SwingConstants.CENTER);
    heading.setVerticalTextPosition(SwingConstants.BOTTOM);
    heading.setHorizontalAlignment(SwingConstants.CENTER);
    heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    messageArea.setEditable(false);
    messageInput.setHorizontalAlignment(SwingConstants.CENTER);


    this.setLayout(new BorderLayout());

    this.add(heading,BorderLayout.NORTH);
    JScrollPane message= new JScrollPane(messageArea);

    this.add(message,BorderLayout.CENTER);
    this.add(messageInput,BorderLayout.SOUTH);

    this.setVisible(true);

    }
    public void startReading(){
        Runnable r1=()->{
            System.out.println("reader started..");
            try{
                while(true){
                    String chat=br.readLine();
                    if(chat.equals("exit")){
                        System.out.println("Server terminated the chat");
                        JOptionPane.showMessageDialog(this, "Server Terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    // System.out.println("client : " +chat);
                    messageArea.append("Server :"+chat+"\n");
                }
            } catch(Exception e){
                System.out.println("connection is closed");
                
            }
        };
        new Thread(r1).start();
    }
    
    public void startWriting(){
        Runnable r2=() -> {
            System.out.println("writer started..");
            try{
                while(!socket.isClosed()){
                   
                    BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                    String content= br1.readLine();
                    out.println(content);
                    out.flush();
                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }
                }
                System.out.println("connection is closed");
            }catch (Exception e){
                e.printStackTrace();
            }
        };
        new Thread(r2).start(); 
    }

    public static void main(String[]args) {
        System.out.println("this is client...");
        new user();
    }
}
