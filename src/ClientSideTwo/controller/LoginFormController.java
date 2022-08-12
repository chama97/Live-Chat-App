package ClientSideTwo.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;

public class LoginFormController {
    public JFXButton loginButton;
    public TextField txtLogin;
    public AnchorPane loginFID;

    public String name;
    public static String userName;

    public static ArrayList<String> users = new ArrayList<>();

    public void loginOnAction(ActionEvent actionEvent) throws IOException {

        userName = txtLogin.getText().trim();
        boolean flag = false;
        if(users.isEmpty()){
            users.add(userName);
            flag = true;
        }


        for(String s : users){
            if (!s.equalsIgnoreCase(userName)) {
                flag = true;
                System.out.println(userName);
                break;
            }
        }

        if(flag){
            this.loginFID.getChildren().clear();
            this.loginFID.getChildren().add(FXMLLoader.load(this.getClass().
                    getResource("../view/ChatRoomForm.fxml")));
        }
    }
}
