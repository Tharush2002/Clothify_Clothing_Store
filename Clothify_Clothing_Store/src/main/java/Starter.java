import config.AppInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Admin;
import model.Employee;
import service.ServiceFactory;
import service.custom.AdminService;
import service.custom.EmployeeService;
import util.Type;

public class Starter extends Application {
    private final AdminService adminService = ServiceFactory.getInstance().getServiceType(Type.ADMIN);
    private final EmployeeService employeeService = ServiceFactory.getInstance().getServiceType(Type.EMPLOYEE);

    public static void main(String[] args) {
        AppInitializer.initialize();
        launch();
        AppInitializer.shutdown();
    }

    @Override
    public void start(Stage stage) throws Exception {
        addDefaultUsers();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("./view/Home.fxml"))));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        stage.setResizable(false);
    }

    private void addDefaultUsers(){
        adminService.saveOrUpdate(new Admin(null,null,null,"admin@gmail.com","admin",null,"admin"),1);
        employeeService.saveOrUpdate(new Employee(null,null,null,"employee@gmail.com",null,"user","123456789",null,"user"),1);
    }
}
