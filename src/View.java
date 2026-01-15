import Exceptions.InvalidChoiceException;
import Controller.Controller;
import Repsitory.IRepository;
import Model.PrgState;

import java.util.Scanner;

public class View {
    Controller controller;
    IRepository repository;
    PrgState state;
    public View(Controller controller, IRepository repository, PrgState state){
        this.controller = controller;
        this.repository = repository;
        this.state = state;
    }
    public void oneStep(){
        state = state.oneStep();

    }
    public void allSteps(){
        try{
            controller.allStep();
        } catch (InterruptedException e){
            System.out.println("Program interrupted");
        }

    }
    public void start(){
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("1. One step");
            System.out.println("2. All steps");
            System.out.println("3. Exit");
            int choice = scanner.nextInt();
            switch(choice){
                case 1: oneStep(); break;
                case 2: allSteps(); break;
                case 3: System.exit(0); break;
                default: throw new InvalidChoiceException();
            }
        }
    }

}
