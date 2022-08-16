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
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.Observer;

public class ChatRoomFormController extends Thread{
    public AnchorPane chatFID;
    public Label txtUserName;
    public TextArea txtArea;
    public TextField txtMessage;

    public BufferedReader reader;
    public PrintWriter writer;
    public Socket socket;
    public ImageView emoji;
    public HBox emojiView;

    private FileChooser fileChooser;
    private File filePath;

    public void initialize(){
        emojiView.setVisible(false);
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

//                st = st.substring(3, st.length() - 1);
//
//
//                File file = new File(st);
//                Image image = new Image(file.toURI().toString());
//
//                ImageView imageView = new ImageView(image);
//
//                imageView.setFitHeight(150);
//                imageView.setFitWidth(200);
//
//
//                HBox hBox = new HBox(10);
//                hBox.setAlignment(Pos.BOTTOM_RIGHT);
            }
            reader.close();
            writer.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean saveControl = false;

    public void fileChooseOnAction(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        filePath = fileChooser.showOpenDialog(stage);
        saveControl = true;

        saveImage();
    }

    public void saveImage() {
        if (saveControl) {
            try {
                BufferedImage bufferedImage = ImageIO.read(filePath);
//                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                Image image = new Image(filePath.toURI().toString());
                ImageIO.write(bufferedImage,"png" , filePath);
                ImageView imageView = new ImageView();
                imageView.setImage(image);
                imageView.setFitHeight(30);
                imageView.setFitWidth(30);
                saveControl = false;
                writer.println(LoginFormController.userName + ": " + imageView.getImage());
                txtArea.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
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


//    public void emojiSendOnClick(MouseEvent mouseEvent) {
//        Image image = emoji.getImage();
//        ImageView imageView = new ImageView();
//        imageView.setImage(image);
//        imageView.setFitHeight(25);
//        imageView.setFitWidth(25);
//        writer.println(LoginFormController.userName + ": "+ imageView.getImage());
//        txtArea.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
//    }

    public void emojiOnClick(MouseEvent mouseEvent) {
        if(!emojiView.isVisible()){
            emojiView.setVisible(true);
        }else {
            emojiView.setVisible(false);
        }
    }

    public void thumbUpOnClick(MouseEvent mouseEvent) {
        txtMessage.appendText("\ud83d\udc4d");
    }

    public void thumbDownOnClick(MouseEvent mouseEvent) {
        txtMessage.appendText("\ud83d\udc4e");
    }

    public void okHandOnClick(MouseEvent mouseEvent) {
        txtMessage.appendText("\ud83d\udc4c");
    }

    public void smileOnClick(MouseEvent mouseEvent) {
        txtMessage.appendText("\ud83d\ude02");
    }

    public void sadOnClick(MouseEvent mouseEvent) {
        txtMessage.appendText("\ud83d\ude14");
    }

    public void angryOnClick(MouseEvent mouseEvent) {
        txtMessage.appendText("\ud83d\ude21");
    }

    public void heartOnClick(MouseEvent mouseEvent) {
        txtMessage.appendText("\ud83d\ude0d");
    }

}
