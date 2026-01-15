package Repsitory;

import Exceptions.MyException;
import Model.PrgState;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MyRepository implements IRepository {
    private List<PrgState> myList; // The list of all running threads (PrgStates)
    private String logFilePath;

    public MyRepository() {
        this.myList = new ArrayList<>();
        this.logFilePath = "test.txt";
        // clear the log file at the start of execution
        emptyLogFile();
    }

    // Helper method to clear the file initially
    public void emptyLogFile() {
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, false)))) {
            // Opening with append=false clears the file content
        } catch (IOException e) {
            // It's not critical if we can't clear it, but good to report
            System.out.println("Could not clear log file: " + e.getMessage());
        }
    }

    @Override
    public void addState(PrgState state) {
        this.myList.add(state);
    }

    // [cite: 13] Return the list of program states
    @Override
    public List<PrgState> getPrgList() {
        return this.myList;
    }

    //  Replace the repository's list with the new one from Controller
    @Override
    public void setPrgList(List<PrgState> list) {
        this.myList = list;
    }

    //  Log a specific PrgState to the file
    @Override
    public void logPrgState(PrgState prgState) throws MyException {
        // We use try-with-resources to ensure the file is closed automatically.
        // append=true is CRITICAL so we don't overwrite previous logs.
        try (PrintWriter log = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)))) {
            log.println(prgState.toString());
        } catch (IOException e) {
            throw new MyException("File error: " + e.getMessage());
        }
    }
}