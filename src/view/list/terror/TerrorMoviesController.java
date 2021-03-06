package view.list.terror;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.ModelMovie;
import model.ModelRating;
import model.ModelUser;
import model.entity.Movie;
import view.list.InfoMoviesController;
import view.list.ListController;
import view.principal.Main;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class TerrorMoviesController implements Initializable {

    @FXML
    private Pane pnMovies;

    @FXML
    private Label lbTitle;

    @FXML
    private Label lbGender;

    /**
     * Busca quais filmes do banco de dados que tem gênero
     * de terror e mostra na tela
     * */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int posX = 74;
        int posY = 155;
        int i = 1;

        for(Movie movie : ModelMovie.getInstance().readAllMovies()){

            EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e)
                {
                    InfoMoviesController.movieId = movie.getId();
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../infomovies.fxml"));
                        Parent root1 = (Parent) fxmlLoader.load();
                        Stage stage = new Stage();
                        // Block the parent window
                        stage.initOwner(Main.stage);
                        stage.initModality(Modality.WINDOW_MODAL);
                        // Properties of info
                        stage.setTitle("Info");
                        stage.setScene(new Scene(root1));
                        stage.setResizable(false);
                        stage.show();
                    } catch (Exception exception) {
                        System.out.println("Erro");
                    }
                }
            };

            if (posX > 788) {
                posY = 155 + (376 * (i / 4));
                posX = 74;
                i--;
            }

            int currentYear = LocalDate.now().getYear();
            int userAge = ModelUser.getInstance().readUsersByEmail(ListController.email).getBirthDate().getYear();
            if(movie.getMinimumAge() <= currentYear - userAge) {
                if ((movie.getMovieGenre().getDescription()).equals("Terror")) {
                    if (posX <= 788) {
                        ImageView pnImg = new ImageView();
                        pnImg.setLayoutX(posX);
                        pnImg.setLayoutY(posY);
                        pnImg.setFitHeight(238);
                        pnImg.setFitWidth(185);
                        Image image = SwingFXUtils.toFXImage(movie.getImageBuffered(), null);
                        pnImg.setImage(image);
                        pnImg.setId("pnImg");
                        pnMovies.getChildren().add(pnImg);
                        Button btnMovie = new Button();
                        btnMovie.setLayoutX(posX);
                        btnMovie.setLayoutY(posY);
                        btnMovie.setPrefSize(185, 238);
                        btnMovie.setId("btnMovie");
                        btnMovie.setOnAction(event);
                        pnMovies.getChildren().add(btnMovie);
                        Pane pnRate = new Pane();
                        pnRate.setPrefSize(63, 32);
                        pnRate.setLayoutX(posX + 65);
                        pnRate.setLayoutY(posY - 21);
                        pnRate.setId("pnRate");
                        pnMovies.getChildren().add(pnRate);
                        Label lbRate = new Label();
                        lbRate.setPrefSize(63, 32);
                        lbRate.setLayoutX(posX + 86);
                        lbRate.setLayoutY(posY - 21);
                        lbRate.setId("lbRate");
                        lbRate.setText(Float.toString(ModelRating.getInstance().readAvgRatingByMovie(ModelMovie.getInstance().readMoviesById(Integer.toString(movie.getId())))));                        pnMovies.getChildren().add(lbRate);
                        Label lbTitle = new Label();
                        lbTitle.setPrefSize(63, 32);
                        lbTitle.setLayoutX(posX + 5);
                        lbTitle.setLayoutY(posY + 264);
                        lbTitle.setPrefSize(170, 29);
                        lbTitle.setId("lbTitle");
                        lbTitle.setText(movie.getName());
                        pnMovies.getChildren().add(lbTitle);
                        Label lbGender = new Label();
                        lbGender.setPrefSize(63, 32);
                        lbGender.setLayoutX(posX + 5);
                        lbGender.setLayoutY(posY + 300);
                        lbGender.setPrefSize(90, 23);
                        lbGender.setId("lbGender");
                        lbGender.setText(movie.getMovieGenre().getDescription());
                        pnMovies.getChildren().add(lbGender);
                        posX += 238;
                    }
                    i++;
                }
            }
        }
    }
}
