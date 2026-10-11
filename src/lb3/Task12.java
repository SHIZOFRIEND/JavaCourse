package lb3;

import java.util.*;
import java.util.function.Function;

interface Repository<ID, T> {
    void save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    boolean deleteById(ID id);
}

class InMemoryRepository<ID, T> implements Repository<ID, T> {
    private final Map<ID, T> storage = new HashMap<>();
    private final Function<T, ID> idExtractor;

    public InMemoryRepository(Function<T, ID> idExtractor) {
        this.idExtractor = Objects.requireNonNull(idExtractor);
    }

    @Override
    public void save(T entity) {
        Objects.requireNonNull(entity, "Entity не может быть null");

        ID id = Objects.requireNonNull(
                idExtractor.apply(entity),
                "ID не может быть null"
        );

        storage.put(id, entity);
    }

    @Override
    public Optional<T> findById(ID id) {
        Objects.requireNonNull(id, "ID не может быть null");
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean deleteById(ID id) {
        Objects.requireNonNull(id, "ID не может быть null");
        return storage.remove(id) != null;
    }
}

public class Task12 {
    public static void main(String[] args) {
        Repository<Integer, String> repository =
                new InMemoryRepository<>(String::length);

        repository.save("Java");
        repository.save("Python");
        repository.save("C#");

        System.out.println("Все записи: " + repository.findAll());

        System.out.println("Поиск по ID 4: "
                + repository.findById(4));

        repository.save("Ruby");

        System.out.println("После повторного сохранения: "
                + repository.findAll());

        List<String> copy = repository.findAll();
        copy.clear();

        System.out.println("После очистки копии: "
                + repository.findAll());

        System.out.println("Удаление ID 2: "
                + repository.deleteById(2));

        System.out.println("Удаление ID 100: "
                + repository.deleteById(100));
    }
}
