package View;

import Controller.Controller;
import Exceptions.MyException;
import Expresions.*;
import Model.*;
import Repsitory.IRepository;
import Repsitory.MyRepository;
import Statemnts.*;
import Types.*;
import Values.BoolValue;
import Values.IntValue;
import Values.StringValue;
import Values.Value;

public class Interpreter {
    public static void main(String[] args) {
        IStmt ex1 = example1();
        IStmt ex2 = example2();
        IStmt ex3 = exampleFiles();
        IStmt ex4 = exampleHeapReadNested();
        IStmt ex5 = exampleHeapWrite();
        IStmt ex6 = exampleHeapGC();
        IStmt ex7 = exWhile();
        IStmt ex8 = exampleConcurrent();

        RunBundle b1 = bundle(ex1);
        RunBundle b2 = bundle(ex2);
        RunBundle b3 = bundle(ex3);
        RunBundle b4 = bundle(ex4);
        RunBundle b5 = bundle(ex5);
        RunBundle b6 = bundle(ex6);
        RunBundle b7 = bundle(ex7);
        RunBundle b8 = bundle(ex8);


        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "exit"));
        menu.addCommand(new RunExample("1", ex1.toString(), b1.controller, b1.state));
        menu.addCommand(new RunExample("2", ex2.toString(), b2.controller, b2.state));
        menu.addCommand(new RunExample("3", ex3.toString(), b3.controller, b3.state));
        menu.addCommand(new RunExample("4", ex4.toString(), b4.controller, b4.state));
        menu.addCommand(new RunExample("5", ex5.toString(), b5.controller, b5.state));
        menu.addCommand(new RunExample("6", ex6.toString(), b6.controller, b6.state));
        menu.addCommand(new RunExample("7", ex7.toString(), b7.controller, b7.state));
        menu.addCommand(new RunExample("8", ex8.toString(), b8.controller, b8.state));
        menu.show();
    }

    private record RunBundle(Controller controller, PrgState state) {}


    private static RunBundle bundle(IStmt program) {

        try {
            MyIDictionary<String, Type> typeEnv = new MyDictionary<>();
            program.typeCheck(typeEnv);
        } catch (MyException e) {
            System.out.println("Type error in program:");
            System.out.println(program.toString());
            System.out.println("-> " + e.getMessage());
            throw e;
        }

        MyIStack<IStmt> stk = new MyStack<>();
        MyIDictionary<String, Value> sym = new MyDictionary<>();
        MyIList<Value> out = new MyList<>();
        MyIFileTable fileTable = new MyFileTable();
        MyIHeap heap = new MyHeap();


        PrgState state = new PrgState(stk, sym, out, fileTable, heap, program);
        IRepository repo = new MyRepository();
        repo.addState(state);

        Controller controller = new Controller(repo);
        return new RunBundle(controller, state);
    }



    public static IStmt repeatUntilEx() {
        return new CompStmt(
                new DeclarationStmt("v", new IntType()),
                new CompStmt(
                        new DeclarationStmt("x", new IntType()),
                        new CompStmt(
                                new DeclarationStmt("y", new IntType()),
                                new CompStmt(
                                        new AssigStmt("v", new ValueExp(new IntValue(0))),
                                        new CompStmt(
                                                new RepeatUntilStmt(
                                                        new CompStmt(
                                                                new ForkStmt(
                                                                        new CompStmt(
                                                                                new PrintStmt(new VarExp("v")),
                                                                                new AssigStmt(
                                                                                        "v",
                                                                                        new ArithExp(
                                                                                                2,
                                                                                                new VarExp("v"),
                                                                                                new ValueExp(new IntValue(1))
                                                                                        )
                                                                                )
                                                                        )
                                                                ),
                                                                new AssigStmt(
                                                                        "v",
                                                                        new ArithExp(
                                                                                1,
                                                                                new VarExp("v"),
                                                                                new ValueExp(new IntValue(1))
                                                                        )
                                                                )
                                                        ),
                                                        new RelationalExp(
                                                                "==",
                                                                new VarExp("v"),
                                                                new ValueExp(new IntValue(3))
                                                        )
                                                ),
                                                new CompStmt(
                                                        new AssigStmt("x", new ValueExp(new IntValue(1))),
                                                        new CompStmt(
                                                                new NoOpStmt(),
                                                                new CompStmt(
                                                                        new AssigStmt("y", new ValueExp(new IntValue(3))),
                                                                        new CompStmt(
                                                                                new NoOpStmt(),
                                                                                new PrintStmt(
                                                                                        new ArithExp(
                                                                                                3,
                                                                                                new VarExp("v"),
                                                                                                new ValueExp(new IntValue(10))
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }

    // Ref int v; Ref int a; v=10; new(a,22);
// fork(wH(a,30); v=32; print(v); print(rH(a)));
// print(v); print(rH(a))
    public static IStmt exampleConcurrent() {
        return new CompStmt(
                new DeclarationStmt("v", new IntType()),
                new CompStmt(
                        new DeclarationStmt("a", new RefType(new IntType())),
                        new CompStmt(
                                new AssigStmt("v", new ValueExp(new IntValue(10))),
                                new CompStmt(
                                        new HeapAlloc("a", new ValueExp(new IntValue(22))),
                                        new CompStmt(
                                                // --- FORK STATEMENT ---
                                                new ForkStmt(new CompStmt(
                                                        new HeapWrite("a", new ValueExp(new IntValue(30))),
                                                        new CompStmt(
                                                                new AssigStmt("v", new ValueExp(new IntValue(32))),
                                                                new CompStmt(
                                                                        new PrintStmt(new VarExp("v")),
                                                                        new PrintStmt(new HeapRead(new VarExp("a")))
                                                                )
                                                        )
                                                )),
                                                // --- PARENT THREAD CODE ---
                                                new CompStmt(
                                                        new PrintStmt(new VarExp("v")),
                                                        new PrintStmt(new HeapRead(new VarExp("a")))
                                                )
                                        )
                                )
                        )
                )
        );
    }




    // Ref int v; new(v,20);
// print(rH(v)); wH(v,30);
// print(rH(v)+5);
    public static IStmt exampleHeapWrite() {
        return new CompStmt(
                // Ref int v;
                new DeclarationStmt("v", new RefType(new IntType())),
                new CompStmt(
                        // new(v,20);
                        new HeapAlloc("v", new ValueExp(new IntValue(20))),
                        new CompStmt(
                                // print(rH(v));
                                new PrintStmt(new HeapRead(new VarExp("v"))),
                                new CompStmt(
                                        // wH(v,30);
                                        new HeapWrite("v", new ValueExp(new IntValue(30))),
                                        // print(rH(v)+5);
                                        new PrintStmt(
                                                new ArithExp(
                                                        1,
                                                        new HeapRead(new VarExp("v")),
                                                        new ValueExp(new IntValue(5))
                                                )
                                        )
                                )
                        )
                )
        );
    }
    // Ref int v; new(v,20);
// Ref Ref int a; new(a,v);
// new(v,30);
// print(rH(rH(a)));
    public static IStmt exampleHeapGC() {
        return new CompStmt(
                // Ref int v;
                new DeclarationStmt("v", new RefType(new IntType())),
                new CompStmt(
                        // new(v,20);
                        new HeapAlloc("v", new ValueExp(new IntValue(20))),
                        new CompStmt(
                                // Ref Ref int a;
                                new DeclarationStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(
                                        // new(a, v);
                                        new HeapAlloc("a", new VarExp("v")),
                                        new CompStmt(
                                                // new(v,30);
                                                new HeapAlloc("v", new ValueExp(new IntValue(30))),
                                                // print(rH(rH(a)));
                                                new PrintStmt(
                                                        new HeapRead(
                                                                new HeapRead(
                                                                        new VarExp("a")
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }


    // Ref int v; new(v,20);
// Ref Ref int a; new(a,v);
// print(rH(v)); print(rH(rH(a))+5);
    public static IStmt exampleHeapReadNested() {
        return new CompStmt(
                // Ref int v;
                new DeclarationStmt("v", new RefType(new IntType())),
                new CompStmt(
                        // new(v,20);
                        new HeapAlloc("v", new ValueExp(new IntValue(20))),
                        new CompStmt(
                                // Ref Ref int a;
                                new DeclarationStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(
                                        // new(a, v);
                                        new HeapAlloc("a", new VarExp("v")),
                                        new CompStmt(
                                                // print(rH(v));
                                                new PrintStmt(new HeapRead(new VarExp("v"))),
                                                // print(rH(rH(a)) + 5);
                                                new PrintStmt(
                                                        new ArithExp(
                                                                1,
                                                                new HeapRead(
                                                                        new HeapRead(
                                                                                new VarExp("a")
                                                                        )
                                                                ),
                                                                new ValueExp(new IntValue(5))
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }

    public static IStmt exWhile() {
        return new CompStmt(
                new DeclarationStmt("v", new IntType()),
                new CompStmt(
                        new AssigStmt("v", new ValueExp(new IntValue(4))),
                        new CompStmt(
                                new WhileStmt(
                                        // while (v > 0)
                                        new RelationalExp(
                                                ">",
                                                new VarExp("v"),
                                                new ValueExp(new IntValue(0))
                                        ),
                                        // body: { print(v); v = v - 1; }
                                        new CompStmt(
                                                new PrintStmt(new VarExp("v")),
                                                new AssigStmt(
                                                        "v",
                                                        new ArithExp(
                                                                2,
                                                                new VarExp("v"),
                                                                new ValueExp(new IntValue(1))
                                                        )
                                                )
                                        )
                                ),

                                new PrintStmt(new VarExp("v"))
                        )
                )
        );
    }



    // int v; v=2; print(v)
    public static IStmt example1() {
        return new CompStmt(
                new DeclarationStmt("v", new IntType()),
                new CompStmt(
                        new AssigStmt("v", new ValueExp(new IntValue(2))),
                        new PrintStmt(new VarExp("v"))
                )
        );
    }

    // bool a; int v; a=true; If a Then v=2 Else v=3; print(v)
    public static IStmt example2() {
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
    public static IStmt exampleFiles() {
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
