package chinookMgr.backend.entityHelpers;

import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.backend.db.entities.GenreEntity;
import chinookMgr.frontend.components.TableInspector;
import org.jetbrains.annotations.NotNull;

public abstract class EntityHelper {
	private EntityHelper() {}

	public static <T> T getById(Class<T> entityClass, int id) {
		try (var session = HibernateUtil.getSession()) {
			return session.get(entityClass, id);
		}
	}

	public static <T> TableInspector.Builder<T>
	getTableInspectorBuilder(@NotNull Class<T> entityClass, @NotNull String searchField) {
		return TableInspector.create(entityClass)
			.withQuerier("from %s where %s like :search".formatted(entityClass.getSimpleName(), searchField))
			.withCounter("select count(*) from %s where %s like :search".formatted(entityClass.getSimpleName(), searchField));
	}
}