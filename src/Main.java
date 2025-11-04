import Expresions.ArithExp;
import Expresions.ValueExp;
import Expresions.VarExp;

import Model.*;
import Statemnts.*;
import Types.IntType;
import Types.StringType;
import Values.IntValue;
import Values.StringValue;
import Values.Value;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static IStmt example1(){
        // int v; v=2; Print(v)
        // -> comp(stmt, comp(stmt, stmt))
        return new CompStmt(
                new DeclarationStmt("v", new IntType()),
                new CompStmt(
                        new AssigStmt("v", new ValueExp(new IntValue(2))),
                        new PrintStmt(new VarExp("v"))
                )
        );
    }
    private static IStmt example2(){
        // int a;int b; a=2+3*5;b=a+1;Print(b)
        return new CompStmt(new DeclarationStmt("a", new IntType()),
                new CompStmt(new DeclarationStmt("b", new IntType()),
                        new CompStmt(new AssigStmt("a", new ArithExp(1, new ValueExp(new IntValue(2)),  new ArithExp(3, new ValueExp(new IntValue(3)), new ValueExp(new IntValue(5))))),
                        new CompStmt(new AssigStmt("b", new ArithExp(1, new VarExp("a"), new ValueExp(new IntValue(1)))), new PrintStmt(new VarExp("b"))))
                ));
    }
    private static IStmt example3(){
        //bool a; int v; a=true;(If a Then v=2 Else v=3);Print(v)
        return new CompStmt(new DeclarationStmt("a", new Types.BoolType()),
                new CompStmt(new DeclarationStmt("v", new IntType()),
                        new CompStmt(new AssigStmt("a", new ValueExp(new Values.BoolValue(true))),
                                new CompStmt(new IfStmt(new VarExp("a"), new AssigStmt("v", new ValueExp(new IntValue(2))), new AssigStmt("v", new ValueExp(new IntValue(3)))),
                                        new PrintStmt(new VarExp("v"))
                                )
                        )
                )
        );
    }
    private static IStmt example4(){
        return  new CompStmt(new DeclarationStmt("varf", new StringType()), new CompStmt(
                new AssigStmt("varf", new ValueExp(new StringValue("test.in"))), new CompStmt(
                new openRFile(new VarExp("varf")), new CompStmt(
                new DeclarationStmt("varc", new IntType()), new CompStmt(
                new readFile(new VarExp("varf"), "varc"), new CompStmt(
                new PrintStmt(new VarExp("varc")), new CompStmt(
                new readFile(new VarExp("varf"), "varc"), new CompStmt(
                new PrintStmt(new VarExp("varc")), new closeRFile(new VarExp("varf"))))))))));
    }
    public static void main(String[] args) {
//        MyIStack<IStmt> stk = new MyStack<>();
//        MyIDictionary<String, Value> symtbl = new MyDictionary<>();
//        MyIList<Value> out = new MyList<>();
//
//        //PrgState state = new PrgState(stk, symtbl, out, example2());
//
//        IRepository repo = new MyRepository();
//        repo.addState(state);
//
//        Controller ctrl = new Controller(repo);
//
//
//
//
//        View view = new View(ctrl, repo, state);
//        view.start();

    }
}