package chinookMgr.backend;

import chinookMgr.db.HibernateUtil;
import chinookMgr.db.entities.CustomerEntity;
import chinookMgr.db.entities.EmployeeEntity;
import org.jetbrains.annotations.NotNull;

public class UserManager {
	private static User currentUser;

	public static User getCurrentUser() {
		return UserManager.currentUser;
	}

	public static boolean isLoggedIn() {
		return UserManager.currentUser != null;
	}

	public static boolean login(@NotNull String email, @NotNull String password) {
		try (var session = HibernateUtil.getSession()) {
			{
				var query = session.createQuery(
					"from EmployeeEntity where email = :email and password = :password",
					EmployeeEntity.class
				);
				query.setParameter("email", email);
				query.setParameter("password", password);

				// its an employee
				var result = query.getResultList();
				if (result.size() == 1) {
					UserManager.currentUser = new Employee(result.get(0));
					return true;
				}
			}

			{
				var query = session.createQuery(
					"from CustomerEntity where email = :email and password = :password",
					CustomerEntity.class
				);
				query.setParameter("email", email);
				query.setParameter("password", password);

				// its a customer
				var result = query.getResultList();
				if (result.size() == 1) {
					UserManager.currentUser = new Customer(result.get(0));
					return true;
				}
			}
		}

		return false;
	}
}