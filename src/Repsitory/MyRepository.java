package Repsitory;

import Model.MyIStack;
import Model.PrgState;
import Statemnts.IStmt;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MyRepository implements IRepository {
    private int currentIndex = -1;
    private List<PrgState> list = new ArrayList<>();
    private String filePath;
    public MyRepository(){
        //System.out.print("Log file path: ");
        //this.filePath = new Scanner(System.in).nextLine().trim();
        this.filePath = "test.txt";
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(filePath, false)))) {
            // nothing to write; opening with append=false truncates the file
        } catch (IOException e) {
            throw new RuntimeException("Cannot initialize log file: " + e.getMessage(), e);
        }
    }
    @Override
    public void addState(PrgState state) {
        this.list.add(state);
        currentIndex++;
    }
    @Override
    public PrgState getState() {
        return this.list.get(currentIndex);
    }

    @Override
    public void output() {
        for(PrgState state : this.list) {
            System.out.println(state);
        }
    }
    @Override
    public void logPrgState(){
        PrgState prg = getState();
        try (PrintWriter log = new PrintWriter(new BufferedWriter(new FileWriter(filePath, true)))) {

            log.println(prg.toString());
        } catch (IOException e) {
            throw new RuntimeException("I/O while logging: " + e.getMessage(), e);
        }
    }
}
