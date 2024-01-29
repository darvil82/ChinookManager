package chinookMgr.shared;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.function.BiFunction;

@FunctionalInterface
public interface Querier<T> extends BiFunction<Session, String, Query<T>> {
}