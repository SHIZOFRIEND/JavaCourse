package lb3;

import java.util.*;

class DependencyGraph<T> {
    private final Comparator<? super T> comparator;
    private final Map<T, Set<T>> graph;

    public DependencyGraph(Comparator<? super T> comparator) {
        this.comparator = Objects.requireNonNull(comparator);
        this.graph = new TreeMap<>(comparator);
    }

    public void addVertex(T vertex) {
        Objects.requireNonNull(vertex, "Вершина не может быть null");
        graph.putIfAbsent(vertex, new TreeSet<>(comparator));
    }

    public void addDependency(T before, T after) {
        addVertex(before);
        addVertex(after);
        graph.get(before).add(after);
    }

    public void removeDependency(T before, T after) {
        Objects.requireNonNull(before);
        Objects.requireNonNull(after);

        Set<T> neighbors = graph.get(before);

        if (neighbors != null) {
            neighbors.remove(after);
        }
    }

    public Result<T> resolve() {
        Map<T, Integer> indegree = new TreeMap<>(comparator);

        for (T vertex : graph.keySet()) {
            indegree.put(vertex, 0);
        }

        for (Set<T> neighbors : graph.values()) {
            for (T neighbor : neighbors) {
                indegree.put(neighbor, indegree.get(neighbor) + 1);
            }
        }

        PriorityQueue<T> ready = new PriorityQueue<>(comparator);

        for (Map.Entry<T, Integer> entry : indegree.entrySet()) {
            if (entry.getValue() == 0) {
                ready.offer(entry.getKey());
            }
        }

        List<T> order = new ArrayList<>();

        while (!ready.isEmpty()) {
            T current = ready.poll();
            order.add(current);

            for (T next : graph.get(current)) {
                int degree = indegree.get(next) - 1;
                indegree.put(next, degree);

                if (degree == 0) {
                    ready.offer(next);
                }
            }
        }

        if (order.size() == graph.size()) {
            return Result.success(order);
        }

        return Result.failure(findCycle());
    }

    private List<T> findCycle() {
        Map<T, Integer> state = new TreeMap<>(comparator);

        for (T vertex : graph.keySet()) {
            state.put(vertex, 0);
        }

        List<T> path = new ArrayList<>();

        for (T vertex : graph.keySet()) {
            if (state.get(vertex) == 0) {
                List<T> cycle = dfs(vertex, state, path);

                if (cycle != null) {
                    return cycle;
                }
            }
        }

        return new ArrayList<>();
    }

    private List<T> dfs(
            T vertex,
            Map<T, Integer> state,
            List<T> path) {

        state.put(vertex, 1);
        path.add(vertex);

        for (T next : graph.get(vertex)) {
            if (state.get(next) == 0) {
                List<T> cycle = dfs(next, state, path);

                if (cycle != null) {
                    return cycle;
                }
            } else if (state.get(next) == 1) {
                int start = path.indexOf(next);
                List<T> cycle = new ArrayList<>(
                        path.subList(start, path.size()));

                cycle.add(next);
                return cycle;
            }
        }

        path.remove(path.size() - 1);
        state.put(vertex, 2);

        return null;
    }
}

class Result<T> {
    private final boolean possible;
    private final List<T> order;
    private final List<T> cycle;

    private Result(
            boolean possible,
            List<T> order,
            List<T> cycle) {
        this.possible = possible;
        this.order = new ArrayList<>(order);
        this.cycle = new ArrayList<>(cycle);
    }

    public static <T> Result<T> success(List<T> order) {
        return new Result<>(true, order,
                Collections.<T>emptyList());
    }

    public static <T> Result<T> failure(List<T> cycle) {
        return new Result<>(false,
                Collections.<T>emptyList(), cycle);
    }

    public boolean isPossible() {
        return possible;
    }

    public List<T> getOrder() {
        return new ArrayList<>(order);
    }

    public List<T> getCycle() {
        return new ArrayList<>(cycle);
    }
}

public class Task15 {
    public static void main(String[] args) {
        DependencyGraph<String> graph =
                new DependencyGraph<>(Comparator.naturalOrder());

        graph.addDependency("Java", "Collections");
        graph.addDependency("Java", "OOP");
        graph.addDependency("OOP", "Spring");
        graph.addDependency("Collections", "Spring");
        graph.addVertex("SQL");

        Result<String> result = graph.resolve();

        if (result.isPossible()) {
            System.out.println("Порядок изучения:");
            System.out.println(result.getOrder());
        } else {
            System.out.println("Обнаружен цикл:");
            System.out.println(result.getCycle());
        }

        System.out.println("\nДобавляем цикл:");
        graph.addDependency("Spring", "Java");

        result = graph.resolve();

        if (result.isPossible()) {
            System.out.println("Порядок изучения:");
            System.out.println(result.getOrder());
        } else {
            System.out.println("Обнаружен цикл:");
            System.out.println(result.getCycle());
        }

        System.out.println("\nПроверка отсутствующей вершины:");
        graph.addDependency("Unknown", "Advanced");

        System.out.println(graph.resolve().getCycle());
    }
}
