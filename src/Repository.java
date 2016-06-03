import java.util.Collection;

interface Repository {

    /**
     * Returns the number of entities found.
     *
     * @return the number of entities
     */
    long count();

    /**
     * Returns all the maintained entities.
     *
     * @return all entities
     */
    Collection<Todo> findAll();

    /**
     * Returns all the maintained entities matching any of the the given ids.
     *
     * @param ids of the entities to find
     *
     * @return all entities found
     */
    Collection<Todo> findAll(Collection<Long> ids);

    /**
     * Returns the entity with the given id.
     *
     * return the entity or {@code null} if not found
     */
    Todo findOne(Long id);

    /**
     * Saves the given entity in the repository, overwriting any previously saved
     * version and <strong>returning a new, possibly modified copy of the saved
     * entity</strong>.
     *
     * @param entity to save
     *
     * @return a new, saved and <strong>possibly modified</strong>, entity or
     *         {@code null} if saving failed
     */
    Todo save(Todo entity);

    /**
     * Saves the given entities, overwriting any previously saved versions.
     *
     * @param entities to save
     *
     * @return the saved entities only, not failures and never {@code null}
     */
    Collection<Todo> save(Collection<Todo> entities);

    /**
     * Deletes the given entity from the repository.
     *
     * @param entity to delete
     */
    void delete(Todo entity);

    /**
     * Deletes the entity identified by the given id.
     *
     * @param id of the entity to delete
     */
    void delete(Long id);

    /**
     * Deletes the given entities.
     *
     * @param entities to delete
     */
    void delete(Collection<Todo> entities);

    /**
     * Deletes all entities from the repository.
     */
    void deleteAll();

    /**
     * Checks whether an entity with the given id can be found in the repository.
     *
     * @param id of the entity to check for
     *
     * @return {@code true} if an entry with the given id is found, {@code false}
     *         otherwise
     */
    boolean exists(Long id);
}