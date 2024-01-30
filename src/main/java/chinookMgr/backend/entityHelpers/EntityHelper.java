package chinookMgr.backend.entityHelpers;

import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.backend.db.entities.GenreEntity;
import chinookMgr.frontend.components.TableInspector;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public abstract class EntityHelper {
	private EntityHelper() {}

	public static <T> T getById(Class<T> entityClass, int id) {
		try (var session = HibernateUtil.getSession()) {
			return session.get(entityClass, id);
		}
	}

	public static <T> TableInspector<T>
	getTableInspector(@NotNull Class<T> entityClass, @NotNull String searchField) {
		assert searchField.chars().allMatch(Character::isAlphabetic);

		return new TableInspector<>(
			(session, input) -> session.createQuery(
					"from " + entityClass.getSimpleName() + " where " + searchField + " like :input",
					entityClass
				)
				.setParameter("input", "%" + input + "%"),

			(session, input) -> session.createQuery(
					"select count(*) from " + entityClass.getSimpleName() + " where " + searchField + " like :input",
					Long.class
				)
				.setParameter("input", "%" + input + "%")
		);
	}
}