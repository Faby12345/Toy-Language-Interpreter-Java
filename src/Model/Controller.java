package Model;

import Exceptions.MyException;
import Exceptions.StackEmptyException;
import Repsitory.IRepository;
import Statemnts.IStmt;
import Values.RefValue;
import Values.Value;

import java.util.*;
import java.util.stream.Collectors;

public class Controller {
    private final IRepository repository;

    public Controller(IRepository repository){
        this.repository = repository;
    }

    // Execute exactly one step
    public PrgState OneStep(PrgState state){
        MyIStack<IStmt> stack = state.getStk();
        if (stack.isEmpty()) {

            throw new StackEmptyException();
        }
        IStmt stmt = stack.pop();

        stmt.execute(state);
        return state;
    }
    // collect addresses of RefValues from symbol table
    private List<Integer> getAddrFromSymTable(Collection<Value> symTableValues) {
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> ((RefValue) v).getAddr())
                .collect(Collectors.toList());
    }

    // collect addresses of RefValues from heap values
    private List<Integer> getAddrFromHeap(Collection<Value> heapValues) {
        return heapValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> ((RefValue) v).getAddr())
                .collect(Collectors.toList());
    }

    private Map<Integer, Value> safeGarbageCollector(Collection<Value> symTableValues,
                                                     Map<Integer, Value> heap) {

        Set<Integer> reachable = new HashSet<>(getAddrFromSymTable(symTableValues));


        boolean changed = true;
        while (changed) {
            changed = false;


            Set<Integer> newAddrs = heap.entrySet().stream()
                    .filter(e -> reachable.contains(e.getKey()))
                    .map(Map.Entry::getValue)
                    .filter(v -> v instanceof RefValue)
                    .map(v -> ((RefValue) v).getAddr())
                    .filter(addr -> !reachable.contains(addr))
                    .collect(Collectors.toSet());

            if (!newAddrs.isEmpty()) {
                reachable.addAll(newAddrs);
                changed = true;
            }
        }

        // 3. keep only reachable heap entries
        return heap.entrySet().stream()
                .filter(e -> reachable.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    public void allSteps(PrgState state){
        logSafe();
        while (!state.getStk().isEmpty()) {
            state = OneStep(state);

            state.getHeap().setMap(
                    safeGarbageCollector(
                            state.getSymTable().getMap().values(),
                            state.getHeap().getMap()
                    )
            );

            logSafe();
        }
    }


    private void logSafe() {
        try {
            repository.logPrgState();
        } catch (RuntimeException e1) {
            throw new MyException("Logging failed");
        }
    }

}
