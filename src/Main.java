import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// NO "package View;" here anymore!

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // IMPORTANT: We must specify the folder "View/" because Main is now outside of it.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("View/ProgramChooser.fxml"));

        Parent root = loader.load();

        primaryStage.setTitle("Program Chooser");
        primaryStage.setScene(new Scene(root, 500, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}