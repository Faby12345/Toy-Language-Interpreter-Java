package View;

import Expresions.ValueExp;
import Expresions.VarExp;
import Model.*;
import Repsitory.IRepository;
import Repsitory.MyRepository;
import Statemnts.*;
import Types.BoolType;
import Types.IntType;
import Types.StringType;
import Values.BoolValue;
import Values.IntValue;
import Values.StringValue;
import Values.Value;

public class Interpreter {
    public static void main(String[] args) {

        IStmt ex1 = example1();
        IStmt ex2 = example2();
        IStmt ex3 = exampleFiles();


        RunBundle b1 = bundle(ex1);
        RunBundle b2 = bundle(ex2);
        RunBundle b3 = bundle(ex3);


        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "exit"));
        menu.addCommand(new RunExample("1", ex1.toString(), b1.controller, b1.state));
        menu.addCommand(new RunExample("2", ex2.toString(), b2.controller, b2.state));
        menu.addCommand(new RunExample("3", ex3.toString(), b3.controller, b3.state));
        menu.show();
    }



    private record RunBundle(Controller controller, PrgState state) {}


    private static RunBundle bundle(IStmt program) {
        MyIStack<IStmt> stk = new MyStack<>();
        MyIDictionary<String, Value> sym = new MyDictionary<>();
        MyIList<Value> out = new MyList<>();
        MyIFileTable fileTable = new MyFileTable();

        PrgState state = new PrgState(stk, sym, out, program, fileTable);

        IRepository repo = new MyRepository();
        repo.addState(state);

        Controller controller = new Controller(repo);
        return new RunBundle(controller, state);
    }

    // int v; v=2; print(v)
    private static IStmt example1() {
        return new CompStmt(
                new DeclarationStmt("v", new IntType()),
                new CompStmt(
                        new AssigStmt("v", new ValueExp(new IntValue(2))),
                        new PrintStmt(new VarExp("v"))
                )
        );
    }

    // bool a; int v; a=true; If a Then v=2 Else v=3; print(v)
    private static IStmt example2() {
        return new CompStmt(
                new DeclarationStmt("a", new BoolType()),
                new CompStmt(
                        new DeclarationStmt("v", new IntType()),
                        new CompStmt(
                                new AssigStmt("a", new ValueExp(new BoolValue(true))),
                                new CompStmt(
                                        new IfStmt(new VarExp("a"),
                                                new AssigStmt("v", new ValueExp(new IntValue(2))),
                                                new AssigStmt("v", new ValueExp(new IntValue(3)))),
                                        new PrintStmt(new VarExp("v"))
                                )
                        )
                )
        );
    }

    // string varf; varf="test.in"; openRFile(varf); int varc;
    // readFile(varf, varc); print(varc); readFile(varf, varc); print(varc); closeRFile(varf)
    private static IStmt exampleFiles() {
        return new CompStmt(
                new DeclarationStmt("varf", new StringType()),
                new CompStmt(
                        new AssigStmt("varf", new ValueExp(new StringValue("test.in"))),
                        new CompStmt(
                                new openRFile(new VarExp("varf")),   // use your exact class names
                                new CompStmt(
                                        new DeclarationStmt("varc", new IntType()),
                                        new CompStmt(
                                                new readFile(new VarExp("varf"), "varc"),
                                                new CompStmt(
                                                        new PrintStmt(new VarExp("varc")),
                                                        new CompStmt(
                                                                new readFile(new VarExp("varf"), "varc"),
                                                                new CompStmt(
                                                                        new PrintStmt(new VarExp("varc")),
                                                                        new closeRFile(new VarExp("varf"))
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }
}
