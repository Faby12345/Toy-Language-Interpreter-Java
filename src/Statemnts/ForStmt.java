package Statemnts;

import Exceptions.MyException;
import Expresions.ArithExp;
import Expresions.Exp;
import Expresions.ValueExp;
import Expresions.VarExp;
import Model.MyIDictionary;
import Model.MyIStack;
import Model.PrgState;
import Types.BoolType;
import Types.IntType;
import Types.Type;

public class ForStmt implements IStmt{
    private String index;
    private Exp start;
    private Exp Cond;
    private Exp step;
    private IStmt body;
    public ForStmt(String index, Exp start, Exp step, Exp Cond, IStmt body){
        this.index = index;
        this.start = start;
        this.step = step;
        this.Cond = Cond;
        this.body = body;
    }
    @Override
    public PrgState execute(PrgState state) {
        MyIStack<IStmt> stk = state.getStk();

        IStmt forStmt =
                new CompStmt(
                        new DeclarationStmt(index, new IntType()),
                        new CompStmt(
                                new AssigStmt(index, start),
                                new WhileStmt(
                                        Cond,
                                        new CompStmt(
                                                body,
                                                new AssigStmt(
                                                        index,
                                                        new ArithExp(
                                                                1,
                                                                new VarExp(index),
                                                                step
                                                        )
                                                )
                                        )
                                )
                        )
                );

        stk.push(forStmt);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) {

        Type startType = start.typeCheck(typeEnv);
        Type stepType = step.typeCheck(typeEnv);

        if (!startType.equals(new IntType()))
            throw new MyException("For: start must be int");
        if (!stepType.equals(new IntType()))
            throw new MyException("For: step must be int");


        typeEnv.update(index, new IntType());

        Type condType = Cond.typeCheck(typeEnv);
        if (!condType.equals(new BoolType()))
            throw new MyException("For: condition must be bool");

        body.typeCheck(typeEnv.deepCopy());

        return typeEnv;
    }


    @Override
    public String toString(){
        return "for("+index+"="+start.toString()+";"+Cond.toString()+";"+index+"+="+step.toString()+")";
    }
}
