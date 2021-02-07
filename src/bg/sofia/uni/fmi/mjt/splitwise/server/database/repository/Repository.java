package bg.sofia.uni.fmi.mjt.splitwise.server.database.repository;

import java.util.Collection;

public interface Repository<T> {
    void add(T object);

    void remove(T object);

    boolean contains(T object);

    Collection<T> getAll();
}
