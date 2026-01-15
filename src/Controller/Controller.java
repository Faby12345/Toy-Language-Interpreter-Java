package Controller;

import Exceptions.MyException;
import Model.PrgState;
import Repsitory.IRepository;
import Values.RefValue;
import Values.Value;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Controller {
    private final IRepository repository;
    private ExecutorService executor;

    public Controller(IRepository repository) {
        this.repository = repository;
    }

    // --- NEW METHOD FOR GUI: Returns the repository so GUI can read data ---
    public IRepository getRepo() {
        return repository;
    }

    // --- NEW METHOD FOR GUI: Executes exactly ONE step when button is clicked ---
    public void oneStep() throws InterruptedException {
        executor = Executors.newFixedThreadPool(2);
        List<PrgState> prgList = removeCompletedPrg(repository.getPrgList());

        if (prgList.size() > 0) {
            // Garbage Collection
            prgList.get(0).getHeap().setMap(
                    safeGarbageCollector(
                            getAddrFromAllSymTables(prgList),
                            prgList.get(0).getHeap().getMap()
                    )
            );

            // Execute one step
            oneStepForAllPrg(prgList);

            // Remove completed programs
            prgList = removeCompletedPrg(repository.getPrgList());
        }

        executor.shutdownNow();
        repository.setPrgList(prgList);
    }

    // --- EXISTING METHODS ---

    public List<PrgState> removeCompletedPrg(List<PrgState> inPrgList) {
        return inPrgList.stream()
                .filter(p -> p.isNotCompleted())
                .collect(Collectors.toList());
    }

    public void oneStepForAllPrg(List<PrgState> prgList) throws InterruptedException {
        // Log before execution
        prgList.forEach(prg -> {
            try {
                repository.logPrgState(prg);
            } catch (MyException e) {
                System.out.println(e.getMessage());
            }
        });

        // Prepare the list of callables
        List<Callable<PrgState>> callList = prgList.stream()
                .map((PrgState p) -> (Callable<PrgState>) (() -> {
                    return p.oneStep();
                }))
                .collect(Collectors.toList());

        // Start the execution of the callables
        List<PrgState> newPrgList = executor.invokeAll(callList).stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        System.out.println("Error in thread execution: " + e.getMessage());
                        return null;
                    }
                })
                .filter(p -> p != null)
                .collect(Collectors.toList());

        // Add the new created threads to the list of existing threads
        prgList.addAll(newPrgList);

        // Log after execution
        prgList.forEach(prg -> {
            try {
                repository.logPrgState(prg);
            } catch (MyException e) {
                System.out.println(e.getMessage());
            }
        });

        repository.setPrgList(prgList);
    }

    public void allStep() throws InterruptedException {
        executor = Executors.newFixedThreadPool(2);
        List<PrgState> prgList = removeCompletedPrg(repository.getPrgList());

        while (prgList.size() > 0) {
            prgList.get(0).getHeap().setMap(
                    safeGarbageCollector(
                            getAddrFromAllSymTables(prgList),
                            prgList.get(0).getHeap().getMap()
                    )
            );
            oneStepForAllPrg(prgList);
            prgList = removeCompletedPrg(repository.getPrgList());
        }
        executor.shutdownNow();
        repository.setPrgList(prgList);
    }

    private List<Integer> getAddrFromAllSymTables(List<PrgState> prgList) {
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
        return heap.entrySet().stream()
                .filter(e -> symTableAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}