import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Starter extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("./view/Home.fxml"))));
//        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("./view/EmployeeDashboard.fxml"))));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        stage.setResizable(false);
    }
}
