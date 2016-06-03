import java.util.*;

public class InMemoryRepository implements Repository {
    private HashMap<Long, Todo> internalMap;

    public InMemoryRepository() {
        this(Collections.<Todo> emptyList());
    }

    public InMemoryRepository(Collection<Todo> values) {
        // TODO: Refactor
        this.internalMap = new HashMap<>();
        for (Todo value : values) {
            this.internalMap.put(value.getId(), value);
        }
    }

    @Override
    public long count() {
        return (long) this.internalMap.size();
    }

    @Override
    public Collection<Todo> findAll() {
        if (this.internalMap.isEmpty()) {
            return Collections.emptyList();
        } else {
            return this.internalMap.values();
        }
    }

    @Override
    public Collection<Todo> findAll(Collection<Long> ids) {
        Collection<Todo> found = new ArrayList<>();

        for (Long id: ids) {
            if (this.internalMap.containsKey(id)) {
                found.add(this.internalMap.get(id));
            }
        }

        if (found.isEmpty()) {
            return null;
        } else {
            return found;
        }
    }

    @Override
    public Todo findOne(Long id) {
        return this.internalMap.get(id);
    }

    @Override
    public Todo save(Todo entity) {
        this.internalMap.put(entity.getId(), entity);

        return entity;
    }

    @Override
    public Collection<Todo> save(Collection<Todo> entities) {
        Collection<Todo> save = new ArrayList<>();

        for (Todo todo : entities) {
            save.add(this.save(todo));
        }

        return save;
    }

    @Override
    public void delete(Todo entity) {
        System.out.println("Before delete - repo count " + this.internalMap.size());

        if (this.internalMap.containsValue(entity)) {
            System.out.println("Removing todo: \"" + entity.toString() + "\"");
            this.internalMap.remove(entity.getId());
        }

        System.out.println("After delete - repo count " + this.internalMap.size());
    }

    @Override
    public void delete(Long id) {
        this.internalMap.remove(id);
    }

    @Override
    public void delete(Collection<Todo> entities) {
        for (Todo entity : entities) {
            this.delete(entity);
        }
    }

    @Override
    public void deleteAll() {
        this.internalMap.clear();
    }

    @Override
    public boolean exists(Long id) {
        return this.internalMap.containsKey(id);
    }
}
