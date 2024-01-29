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

	public static <T> TableInspector<T>
	getTableInspectorBuilder(@NotNull Class<T> entityClass, @NotNull String searchField) {
		return new TableInspector<>(
			(session, input) -> session.createQuery("from " + entityClass.getSimpleName() + " where :field like :input", entityClass)
				.setParameter("input", "%" + input + "%")
				.setParameter("field", searchField),

			(session, input) -> session.createQuery("select count(*) from " + entityClass.getSimpleName() + " where :field like :input", Long.class)
				.setParameter("input", "%" + input + "%")
				.setParameter("field", searchField)
		);
	}
}