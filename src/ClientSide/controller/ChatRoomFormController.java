package ClientSide.controller;

import javafx.application.Platform;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
    public HBox emojiView;
    public VBox vBox;

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

                String[] massageAr = msg.split(" ");
                String string = "";
                for (int i = 0; i < massageAr.length - 1; i++) {
                    string += massageAr[i + 1] + " ";
                }

                String fChar = "";

                if (string.length() > 3) {
                    fChar = string.substring(0, 3);
                }

                if (fChar.equalsIgnoreCase("")) {
                    string = string.substring(3, string.length() - 1);

                    File file = new File(string);
                    Image image = new Image(file.toURI().toString());

                    ImageView imageView = new ImageView(image);

                    imageView.setFitWidth(150);
                    imageView.setFitHeight(200);

                    HBox hBox = new HBox(10);
                    hBox.setAlignment(Pos.BOTTOM_RIGHT);

                    hBox.getChildren().add(imageView);

                    Platform.runLater(() -> vBox.getChildren().addAll(hBox));

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

    public void fileChooseOnAction(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        this.filePath = fileChooser.showOpenDialog(stage);
        writer.println(LoginFormController.userName + " : " + filePath.getPath());
        writer.flush();
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
