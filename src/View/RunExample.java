package View;

import Model.Controller;
import Model.PrgState;

public class RunExample extends Command{
    private final Controller controller;
    private final PrgState state;
    private boolean ran = false;

    public RunExample(String key, String desc, Controller controller, PrgState state) {
        super(key, desc);
        this.controller = controller;
        this.state = state;
    }

    @Override
    public void execute() {
        if (ran) {
            System.out.println("This example was already executed. Choose another option.");
            return;
        }
        try {
            controller.allSteps(state);
            ran = true;
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
