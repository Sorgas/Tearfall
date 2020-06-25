package stonering.game.model.system;

import stonering.entity.Entity;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Alexander on 03.02.2020.
 */
public abstract class EntityStream<T extends Entity> {
    public Stream<T> stream;

    public EntityStream(Collection<T> entities) {
        stream = entities.stream();
    }

    public EntityStream() {
        stream = container().objects.stream();
    }

    public EntityStream<T> filterByReachability(Position position) {
        LocalMap map = GameMvc.model().get(LocalMap.class);
        stream = stream.filter(item -> item.position != null)
                .filter(item -> map.passageMap.util.positionReachable(position, item.position, false));
        return this;
    }

    public EntityStream<T> filterByReachability(Position position, boolean acceptNearTarget) {
        LocalMap map = GameMvc.model().get(LocalMap.class);
        stream = stream.filter(item -> item.position != null)
                .filter(item -> map.passageMap.util.positionReachable(item.position, position, acceptNearTarget));
        return this;
    }

    public T getNearestTo(Position position) {
        return stream.min(Comparator.comparingInt(entity -> entity.position.fastDistance(position))).orElse(null);
    }

    public EntityStream<T> getNearestTo(Position position, int number) {
        stream = stream.sorted(Comparator.comparingInt(entity -> position.fastDistance(entity.position))).limit(number);
        return this;
    }

    public EntityStream<T> filterOnMap() {
        stream = stream.filter(entity -> entity.position != null);
        return this;
    }

    public EntityStream<T> filterNotInList(List<T> list) {
        stream = stream.filter(entity -> !list.contains(entity));
        return this;
    }

    public EntityStream<T> sorted(Comparator<Entity> comparator) {
        stream = stream.sorted(comparator);
        return this;
    }

    public EntityStream<T> filter(Predicate<? super T> predicate) {
        stream = stream.filter(predicate);
        return this;
    }

    public List<T> toList() {
        return stream.collect(Collectors.toList());
    }


    protected abstract EntityContainer<T> container();
}
