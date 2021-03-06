package view.changedata;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.ModelUser;
import model.entity.User;
import utils.Dialog;
import utils.EncryptPassword;
import utils.ValidateEmail;
import view.principal.Main;

import javax.swing.text.html.ImageView;
import java.awt.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ChangeDataController implements Initializable {

    @FXML
    private ImageView imgAlterarDados;

    @FXML
    private Rectangle rctBackground;

    @FXML
    private JFXTextField txtfUser;

    @FXML
    private JFXTextField txtfEmail;

    @FXML
    private JFXDatePicker dtBirthdate;

    @FXML
    private JFXPasswordField pfPass;

    @FXML
    private JFXPasswordField pfConfirmPass;

    @FXML
    private JFXPasswordField pfCurrentPass;

    @FXML
    private JFXButton btnAlterar;

    @FXML
    private JFXButton btnQuit;

    String email;

    String oldEmail;

    //inicializa a tela de Update de usuario
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.addOnChangesScreenListener(new Main.OnChangeScreen() {
            @Override
            public void onScreenChanged(String newScreen, String currentUser) {
                if(newScreen.equals("changeData")) {
                    txtfUser.setText(ModelUser.getInstance().readUsersByEmail(currentUser).getName());
                    txtfEmail.setText(ModelUser.getInstance().readUsersByEmail(currentUser).getEmail());
                    dtBirthdate.setValue(ModelUser.getInstance().readUsersByEmail(currentUser).getBirthDate());
                    email = currentUser;
                    oldEmail = ModelUser.getInstance().readUsersByEmail(currentUser).getEmail();
                }
            }
        });
    }

    /**
     * Recebe as informações pegas da Tela de Update e edita o usuario
     * */
    public void changeRegister(ActionEvent event) {
        if((pfConfirmPass.getText().equals("") && pfPass.getText().equals("")) ||
            (!pfConfirmPass.getText().equals("") && !pfPass.getText().equals("") )){

            if (txtfUser.getText().equals("") || txtfEmail.getText().equals("")  || dtBirthdate.getValue() == null
                    ||pfCurrentPass.getText().equals("") ) {
                Dialog.warning("Favor informar todos campos");
            } else if (!ValidateEmail.isValidEmail(txtfEmail.getText())) {
                Dialog.warning("Informe um endereço de E-mail válido");
            } else if (!pfPass.getText().equals(pfConfirmPass.getText())) {
                Dialog.warning("As senhas não coincidem");
            } else if(!(ModelUser.getInstance().readUsersByEmail(oldEmail).getPassword().equals(
                    EncryptPassword.encryptPassword(oldEmail, pfCurrentPass.getText())))){
                Dialog.warning("Informe a senha atual corretamente para prosseguir");
            } else {
                User user;
                if(!pfPass.getText().equals("")){
                    user = new User(ModelUser.getInstance().readUsersByEmail(oldEmail).getId(), txtfUser.getText(),
                            txtfEmail.getText(), EncryptPassword.encryptPassword(txtfEmail.getText(), pfPass.getText()),
                            dtBirthdate.getValue());}
                else{
                    user = new User(ModelUser.getInstance().readUsersByEmail(oldEmail).getId(), txtfUser.getText(),
                        txtfEmail.getText(), ModelUser.getInstance().readUsersByEmail(oldEmail).getPassword(),
                        dtBirthdate.getValue());}
                boolean update = ModelUser.getInstance().updateUser(user);
                if (!update) {
                    Dialog.error("Erro ao atualizar");
                } else {
                    Dialog.information("Atualização Conluída");
                    pfCurrentPass.setText("");
                    pfConfirmPass.setText("");
                    pfPass.setText("");
                    Main.changeScreen("login");
                }
            }
        }
    }

    //Volta para o menu principal
    public void backHome(ActionEvent event) {
        if(ModelUser.getInstance().isAdmin(ModelUser.getInstance().readUsersByEmail(email))){
            Main.changeScreen("adm", email);
        } else {
            Main.changeScreen("home", email);
        }
    }



}
