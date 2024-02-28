package chinookMgr.backend.db.entities;

import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.frontend.components.TableInspector;
import org.jetbrains.annotations.NotNull;

public abstract class EntityHelper {
	private EntityHelper() {}

	public static <T> T getById(Class<T> entityClass, Integer id) {
		if (id == null)
			return null;

		return HibernateUtil.withSession(session -> {
			return session.get(entityClass, id);
		});
	}

	public static <T> TableInspector<T>
	getTableInspector(@NotNull Class<T> entityClass, @NotNull String searchField) {
		assert searchField.chars().allMatch(Character::isAlphabetic);

		return new TableInspector<>(
			(session, input) -> session.createQuery(
					"from " + entityClass.getSimpleName() + " where " + searchField + " like :input",
					entityClass
				)
				.setParameter("input", defaultSearch(input)),

			(session, input) -> session.createQuery(
					"select count(*) from " + entityClass.getSimpleName() + " where " + searchField + " like :input",
					Long.class
				)
				.setParameter("input", defaultSearch(input))
		);
	}

	public static @NotNull String defaultSearch(@NotNull String input) {
		return "%" + input + "%";
	}
}