package chinookMgr.backend;

import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.backend.db.entities.CustomerEntity;
import chinookMgr.backend.db.entities.EmployeeEntity;
import chinookMgr.shared.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class UserManager {
	private static User currentUser;
	public static Consumer<User<?>> onUserChange;

	public static User<?> getCurrentUser() {
		return UserManager.currentUser;
	}

	public static boolean isLoggedIn() {
		return UserManager.currentUser != null;
	}

	private static void setCurrentUser(User<?> user) {
		UserManager.currentUser = user;
		if (UserManager.onUserChange != null)
			UserManager.onUserChange.accept(user);
	}

	public static boolean login(@NotNull String email, @NotNull String password) {
		byte[] passwordHash = Utils.toMD5(password);

		try (var session = HibernateUtil.getSession()) {
			{
				var query = session.createQuery(
					"from EmployeeEntity where email = :email and password = :password",
					EmployeeEntity.class
				);
				query.setParameter("email", email);
				query.setParameter("password", passwordHash);

				// its an employee
				var result = query.getResultList();
				if (result.size() == 1) {
					setCurrentUser(new Employee(result.get(0)));
					return true;
				}
			}

			{
				var query = session.createQuery(
					"from CustomerEntity where email = :email and password = :password",
					CustomerEntity.class
				);
				query.setParameter("email", email);
				query.setParameter("password", passwordHash);

				// its a customer
				var result = query.getResultList();
				if (result.size() == 1) {
					setCurrentUser(new Customer(result.get(0)));
					return true;
				}
			}
		}

		return false;
	}

	public static void logout() {
		setCurrentUser(null);
	}
}