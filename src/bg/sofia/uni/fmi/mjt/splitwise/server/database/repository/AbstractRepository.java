package bg.sofia.uni.fmi.mjt.splitwise.server.database.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public abstract class AbstractRepository<T> implements Repository<T> {

    protected final Set<T> collection;

    public AbstractRepository() {
        this.collection = new HashSet<>();
    }

    public AbstractRepository(String fileName) {
        collection = new HashSet<>();
        try {
            Stream<String> lines = Files.lines(Path.of(fileName));
            lines.forEach(line -> collection.add(makeObjectFromLine(line)));
        } catch (IOException exception) {
            //TODO
        }
    }

    @Override
    public void add(T object) {
        collection.add(object);
    }

    @Override
    public void remove(T object) {
         collection.remove(object);
    }

    @Override
    public boolean contains(T object) {
        return collection.contains(object);
    }

    @Override
    public Collection<T> getAll() {
        return this.collection;
    }

    protected abstract T makeObjectFromLine(String line);
}
