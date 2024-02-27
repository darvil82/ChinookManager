package chinookMgr.backend;

import chinookMgr.backend.db.HibernateUtil;
import chinookMgr.backend.db.entities.CustomerEntity;
import chinookMgr.backend.db.entities.EmployeeEntity;
import chinookMgr.shared.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class UserManager {
	private static User currentUser;
	public static Consumer<User> onUserChange;

	public static User getCurrentUser() {
		return UserManager.currentUser;
	}

	public static boolean isLoggedIn() {
		return UserManager.currentUser != null;
	}

	private static void setCurrentUser(User user) {
		UserManager.currentUser = user;
		if (UserManager.onUserChange != null)
			UserManager.onUserChange.accept(user);
	}

	public static Boolean login(@NotNull String email, @NotNull String password) {
		byte[] passwordHash = Utils.toMD5(password.getBytes());

		return HibernateUtil.withSession(session -> {
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
					setCurrentUser(result.getFirst());
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
					setCurrentUser(result.getFirst());
					return true;
				}
			}

			return false;
		});
	}

	public static Boolean registerCustomer(@NotNull String email, @NotNull String password, @NotNull String firstName, @NotNull String lastName) {
		byte[] passwordHash = Utils.toMD5(password.getBytes());

		return HibernateUtil.withSession(s -> {
			boolean foundCustomer = !s.createQuery("from CustomerEntity where email = :email", CustomerEntity.class)
				.setParameter("email", email)
				.getResultList()
				.isEmpty();

			if (foundCustomer) return false;

			var customer = new CustomerEntity();
			customer.setEmail(email);
			customer.setPassword(passwordHash);
			customer.setFirstName(firstName);
			customer.setLastName(lastName);
			s.merge(customer);

			return true;
		});
	}

	public static void logout() {
		setCurrentUser(null);
	}
}