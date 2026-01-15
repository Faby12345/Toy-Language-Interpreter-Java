package Model;

import Exceptions.MyException;
import Repsitory.IRepository;
import Values.RefValue;
import Values.Value;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {
    private final IRepository repository;
    private ExecutorService executor; // Ref: Lab 8, Step 12 [cite: 77]

    public Controller(IRepository repository) {
        this.repository = repository;
    }



    private List<PrgState> removeCompletedPrg(List<PrgState> inPrgList) {
        return inPrgList.stream()
                .filter(p -> p.isNotCompleted())
                .collect(Collectors.toList());
    }


    // Executes one step for EVERY program in the list concurrently
    private void oneStepForAllPrg(List<PrgState> prgList) throws InterruptedException {

        prgList.forEach(prg -> {
            try {
                repository.logPrgState(prg);
            } catch (MyException e) {
                System.out.println(e.getMessage());
            }
        });

        // 2. Prepare the list of callables (tasks)
        List<Callable<PrgState>> callList = prgList.stream()
                .map((PrgState p) -> (Callable<PrgState>) (() -> {
                    return p.oneStep();
                }))
                .collect(Collectors.toList());


        // invokeAll executes the tasks and returns a list of Futures
        List<PrgState> newPrgList = executor.invokeAll(callList).stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException | ExecutionException e) {

                        System.out.println("Error in thread execution: " + e.getMessage());
                        return null;
                    }
                })
                .filter(p -> p != null) // Keep only the new threads created by fork
                .collect(Collectors.toList());

        //  Add the newly created threads to the existing list
        prgList.addAll(newPrgList);

        //  Log the programs after execution
        prgList.forEach(prg -> {
            try {
                repository.logPrgState(prg);
            } catch (MyException e) {
                System.out.println(e.getMessage());
            }
        });

        // Save the current state of the list in the repository
        repository.setPrgList(prgList);
    }


    public void allStep() throws InterruptedException {
        executor = Executors.newFixedThreadPool(2); // Create a pool of 2 threads


        List<PrgState> prgList = removeCompletedPrg(repository.getPrgList());

        while (prgList.size() > 0) {
            prgList.get(0).getHeap().setMap(
                    safeGarbageCollector(
                            getAddrFromAllSymTables(prgList),
                            prgList.get(0).getHeap().getMap()
                    )
            );

            // Execute one step for all threads
            oneStepForAllPrg(prgList);

            // Remove threads that finished during this step
            prgList = removeCompletedPrg(repository.getPrgList());
        }

        executor.shutdownNow(); // Kill the thread pool
        repository.setPrgList(prgList); // Update repo one last time
    }



    // New helper to get addresses from ALL symbol tables
    private List<Integer> getAddrFromAllSymTables(List<PrgState> prgList){
        return prgList.stream()
                .flatMap(p -> getAddrFromSymTable(p.getSymTable().getMap().values()).stream())
                .collect(Collectors.toList());
    }

    private List<Integer> getAddrFromSymTable(Collection<Value> symTableValues) {
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> ((RefValue) v).getAddr())
                .collect(Collectors.toList());
    }

    private Map<Integer, Value> safeGarbageCollector(List<Integer> symTableAddr, Map<Integer, Value> heap) {
        // This is the conservative garbage collector
        return heap.entrySet().stream()
                .filter(e -> symTableAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}