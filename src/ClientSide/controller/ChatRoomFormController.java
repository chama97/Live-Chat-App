package ClientSide.controller;

import javafx.event.ActionEvent;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatRoomFormController extends Thread {
    public AnchorPane chatFID;
    public Label txtUserName;
    public TextArea txtArea;
    public TextField txtMessage;

    public BufferedReader reader;
    public PrintWriter writer;
    public Socket socket;

    public void initialize(){
        System.out.println("Initialized method" +LoginFormController.userName);
        txtUserName.setText(LoginFormController.userName);
        try{
            socket = new Socket("localhost", 5000);
            System.out.println("Socket is connecting with server");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            this.start();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try{
            while (true){
                String msg = reader.readLine();
                String[] tokens = msg.split(" ");
                String cmd = tokens[0];
                StringBuilder fullMessage = new StringBuilder();
                for (int i = 1; i < tokens.length; i++) {
                    fullMessage.append(tokens[i]);
                }
                System.out.println(fullMessage);

                if(cmd.equalsIgnoreCase(LoginFormController.userName + ": ")){
                    continue;
                }else if (fullMessage.toString().equalsIgnoreCase("bye")){
                    break;
                }
                txtArea.appendText("\n"+ msg + "\n");
            }
            reader.close();
            writer.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendOnAction(ActionEvent actionEvent) {
        String msg = txtMessage.getText().trim();
        txtArea.setStyle("-fx-color: red;");
        writer.println(LoginFormController.userName + ": "+ msg);
        txtArea.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        txtMessage.setText("");
        if(msg.equalsIgnoreCase("Bye") || (msg.equalsIgnoreCase("logout"))){
            System.exit(0);
        }
    }

    public void imageOnAction(MouseEvent mouseEvent) {
    }
}
