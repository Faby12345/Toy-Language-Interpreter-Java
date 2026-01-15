package Controller;

import Controller.Controller;
import Model.PrgState;
import Model.MyIStack;
import Model.MyIDictionary;
import Model.MyIList;
import Values.StringValue;
import Values.Value;
import Statemnts.IStmt;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProgramExecutorController {

    private Controller controller; // The backend controller

    // --- FXML UI ELEMENTS ---
    @FXML
    private TextField numberOfPrgStatesTextField;

    @FXML
    private TableView<Map.Entry<Integer, Value>> heapTableView;

    @FXML
    private TableColumn<Map.Entry<Integer, Value>, Integer> addressColumn;

    @FXML
    private TableColumn<Map.Entry<Integer, Value>, String> valueColumn;

    @FXML
    private ListView<String> outputListView;

    @FXML
    private ListView<String> fileTableListView;

    @FXML
    private ListView<Integer> prgStateIdentifiersListView;

    @FXML
    private TableView<Map.Entry<String, Value>> symbolTableView;

    @FXML
    private TableColumn<Map.Entry<String, Value>, String> varNameColumn;

    @FXML
    private TableColumn<Map.Entry<String, Value>, String> varValueColumn;

    @FXML
    private ListView<String> exeStackListView;

    @FXML
    private Button runOneStepButton;

    // --- INITIALIZATION ---

    @FXML
    public void initialize() {
        // 1. Setup Heap Table Columns
        // Maps the "Key" of the heap entry to the first column
        addressColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getKey()).asObject());
        // Maps the "Value" of the heap entry to the second column
        valueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().toString()));

        // 2. Setup Symbol Table Columns
        varNameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));
        varValueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().toString()));

        // 3. Listener: When user clicks a different ID, update the SymTable & Stack for that thread
        prgStateIdentifiersListView.setOnMouseClicked(this::changePrgState);
    }

    // Called from ProgramChooserController to pass the backend controller
    public void setController(Controller controller) {
        this.controller = controller;
        populate(); // Initial population of data
    }

    // --- EVENT HANDLERS ---

    @FXML
    public void runOneStep(ActionEvent event) {
        if (controller == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No program selected!");
            alert.show();
            return;
        }

        try {
            // Call the ONE STEP method you created in the backend
            controller.oneStep();

            // Refresh all tables/lists after the step
            populate();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
        }
    }

    private void changePrgState(MouseEvent event) {
        populateSymTableAndExeStack();
    }

    // --- POPULATION LOGIC ---

    private void populate() {
        populateHeap();
        populateProgramStateIdentifiers();
        populateFileTable();
        populateOutput();
        populateSymTableAndExeStack();
    }

    private void populateHeap() {
        // Heap is SHARED. We can take it from the first available state.
        if (controller.getRepo().getPrgList().size() > 0) {
            Map<Integer, Value> heap = controller.getRepo().getPrgList().get(0).getHeap().getMap();
            List<Map.Entry<Integer, Value>> heapList = new ArrayList<>(heap.entrySet());
            heapTableView.setItems(FXCollections.observableArrayList(heapList));
            heapTableView.refresh();
        }
    }

    private void populateProgramStateIdentifiers() {
        List<PrgState> prgStates = controller.getRepo().getPrgList();

        // Update the list of IDs
        List<Integer> idList = prgStates.stream()
                .map(PrgState::getId)
                .collect(Collectors.toList());
        prgStateIdentifiersListView.setItems(FXCollections.observableArrayList(idList));

        // Update the text field count
        numberOfPrgStatesTextField.setText(String.valueOf(prgStates.size()));
    }

    private void populateFileTable() {
        // 1. Check if we have any program states
        if (controller.getRepo().getPrgList().size() > 0) {

            // 2. Get the map. Note the type is <StringValue, BufferedReader>
            Map<StringValue, BufferedReader> filesMap = controller.getRepo().getPrgList().get(0).getFileTable().getContent();

            // 3. Create a list of Strings for the GUI
            List<String> fileList = new ArrayList<>();

            // 4. Iterate over the keys (which are StringValue objects) and convert them to String
            for (StringValue key : filesMap.keySet()) {
                fileList.add(key.toString()); // Assuming StringValue has .getValue() or .toString()
            }

            // 5. Update the ListView
            fileTableListView.setItems(FXCollections.observableArrayList(fileList));
        }
    }

    private void populateOutput() {
        // Output is SHARED.
        if (controller.getRepo().getPrgList().size() > 0) {
            MyIList<Value> outList = controller.getRepo().getPrgList().get(0).getOut();
            // Convert list of Value objects to list of Strings
            List<String> outStringList = new ArrayList<>();
            for (Value v : outList.getList()) {
                outStringList.add(v.toString());
            }
            outputListView.setItems(FXCollections.observableArrayList(outStringList));
        }
    }

    private void populateSymTableAndExeStack() {
        // 1. Get the selected ID
        Integer selectedId = prgStateIdentifiersListView.getSelectionModel().getSelectedItem();
        if (selectedId == null) {
            // Default to the first one if nothing selected, or just return
            if (!controller.getRepo().getPrgList().isEmpty()) {
                selectedId = controller.getRepo().getPrgList().get(0).getId();
            } else {
                return;
            }
        }

        // 2. Find the PrgState with that ID
        PrgState selectedPrgState = null;
        for (PrgState state : controller.getRepo().getPrgList()) {
            if (state.getId() == selectedId) {
                selectedPrgState = state;
                break;
            }
        }

        // 3. Populate if found
        if (selectedPrgState != null) {
            // -- SymTable --
            Map<String, Value> symTable = selectedPrgState.getSymTable().getContent();
            List<Map.Entry<String, Value>> symList = new ArrayList<>(symTable.entrySet());
            symbolTableView.setItems(FXCollections.observableArrayList(symList));
            symbolTableView.refresh();

            // -- ExeStack --
            // You need a way to iterate your Stack.
            // If MyStack doesn't expose a list, you might need to add a getter to it.
            // Assuming getStk() has a way to view elements:
            List<String> stackList = new ArrayList<>();

            // TIP: If you haven't implemented getReversed() in MyStack, implement it!
            // It helps display the top of the stack at the top of the list.
            for (IStmt s : selectedPrgState.getStk().getReversed()) {
                stackList.add(s.toString());
            }
            exeStackListView.setItems(FXCollections.observableArrayList(stackList));
        }
    }
}