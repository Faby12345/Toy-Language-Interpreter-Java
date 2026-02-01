package Controller;

import Controller.Controller;
import Model.MyDictionary;
import Model.MyFileTable;
import Model.MyHeap;
import Model.MyList;
import Model.MyStack;
import Model.PrgState;
import Repsitory.IRepository;
import Repsitory.MyRepository;
import Statemnts.IStmt;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import View.Interpreter;
public class ProgramChooserController implements Initializable {

    @FXML
    private ListView<IStmt> programsListView;

    @FXML
    private Button startButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 1. Create the list of programs
        List<IStmt> programs = new ArrayList<>();

         programs.add(Interpreter.example1());
         programs.add(Interpreter.example2());
         programs.add(Interpreter.exampleFiles());
         programs.add(Interpreter.exampleHeapReadNested());
         programs.add(Interpreter.exampleHeapWrite());
         programs.add(Interpreter.exampleHeapGC());
         programs.add(Interpreter.exWhile());
         programs.add(Interpreter.exampleConcurrent());
         programs.add(Interpreter.repeatUntilEx());
         programs.add(Interpreter.ConditionalAssigEx());
         programs.add(Interpreter.ForStmtEx());


        // 2. Populate the ListView
        ObservableList<IStmt> observableList = FXCollections.observableArrayList(programs);
        programsListView.setItems(observableList);

        // 3. Allow Single Selection only
        programsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    @FXML
    public void onStartButton(ActionEvent event) {
        IStmt selectedStmt = programsListView.getSelectionModel().getSelectedItem();

        if (selectedStmt == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error encountered");
            alert.setContentText("No program selected!");
            alert.showAndWait();
            return;
        }

        try {
            // 4. Run Static Type Check
            selectedStmt.typeCheck(new MyDictionary<>());

            // 5. Create the Backend Components
            PrgState prgState = new PrgState(
                    new MyStack<>(),
                    new MyDictionary<>(),
                    new MyList<>(),
                    new MyFileTable(),
                    new MyHeap(),
                    selectedStmt
            );

            // Use a specific log file for this run
            IRepository repo = new MyRepository();
            repo.addState(prgState);
            Controller controller = new Controller(repo);

            // 6. Load the Second Window (ProgramExecutor)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ProgramExecutor.fxml"));
            Parent root = loader.load();

            // 7. Pass the Controller to the Second Window
            ProgramExecutorController executorController = loader.getController();
            executorController.setController(controller);

            // 8. Show the Second Window
            Stage stage = new Stage();
            stage.setTitle("Program Executor");
            stage.setScene(new Scene(root, 700, 500));
            stage.show();



        } catch (IOException | RuntimeException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}