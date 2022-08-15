package ClientSide.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

public class ChatRoomFormController extends Thread{
    public AnchorPane chatFID;
    public Label txtUserName;
    public TextArea txtArea;
    public TextField txtMessage;

    public BufferedReader reader;
    public PrintWriter writer;
    public Socket socket;
    public ImageView emoji;

    private FileChooser fileChooser;
    private File filePath;

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

    public boolean saveControl = false;

    public void imageOnAction(MouseEvent mouseEvent) {

    }

   

    public void sendOnAction(MouseEvent mouseEvent) {
        String msg = txtMessage.getText().trim();
        //txtUserName.setStyle(String.valueOf(Color.RED));
        writer.println(LoginFormController.userName + ": "+ msg);
        txtArea.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        txtMessage.setText("");
        if(msg.equalsIgnoreCase("Bye") || (msg.equalsIgnoreCase("logout"))){
            System.exit(0);
        }
    }

    public void emojiSendOnClick(MouseEvent mouseEvent) {
        Image image = emoji.getImage();
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(25);
        imageView.setFitWidth(25);
        writer.println(LoginFormController.userName + ": "+ imageView.getImage());
        txtArea.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
    }


}
