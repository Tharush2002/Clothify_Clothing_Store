import config.AppInitializer;
import db.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.core.MySQLDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import model.Admin;
import model.Employee;
import service.ServiceFactory;
import service.custom.AdminService;
import service.custom.EmployeeService;
import util.Type;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public class Starter extends Application {

    public static void main(String[] args) {
        AppInitializer.initialize();
        runLiquibaseMigrations();
        launch();
        AppInitializer.shutdown();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("./view/Home.fxml"));

        Scene scene = new Scene(root);

        final double[] xOffset = {0};
        final double[] yOffset = {0};

        root.setOnMousePressed(event -> {
            xOffset[0] = event.getSceneX();
            yOffset[0] = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset[0]);
            stage.setY(event.getScreenY() - yOffset[0]);
        });

        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        stage.setResizable(false);
    }


    private static void runLiquibaseMigrations() {
        Properties properties = new Properties();

        Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Database database = null;

        try {
            database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

            try (Liquibase liquibase = new Liquibase("db/changelog/db.changelog-master.xml", new ClassLoaderResourceAccessor(), database)) {
                properties.forEach((key, value) -> liquibase.setChangeLogParameter(Objects.toString(key), value));
                liquibase.update(new Contexts(), new LabelExpression());
                log.info("Liquibase update completed successfully.");
            }
        } catch (LiquibaseException e) {
            log.error("Liquibase exception occurred: ", e);
        } catch (Exception e) {
            log.error("Error initializing Liquibase: ", e);
        }
    }
}
