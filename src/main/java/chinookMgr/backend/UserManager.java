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

	public static Boolean isEmailAvailable(@NotNull String email) {
		return HibernateUtil.withSession(s -> {
			long numEmployees = s.createQuery("select count(*) from EmployeeEntity where email = :email", Long.class)
				.setParameter("email", email)
				.getSingleResult();

			long numCustomers = s.createQuery("select count(*) from CustomerEntity where email = :email", Long.class)
				.setParameter("email", email)
				.getSingleResult();

			return numEmployees + numCustomers == 0;
		});
	}

	public static Boolean registerCustomer(@NotNull String email, @NotNull String password, @NotNull String firstName, @NotNull String lastName, boolean loginOnSuccess) {
		byte[] passwordHash = Utils.toMD5(password.getBytes());

		return HibernateUtil.withSession(s -> {
			if (!isEmailAvailable(email)) return false;

			var customer = new CustomerEntity();
			customer.setEmail(email);
			customer.setPassword(passwordHash);
			customer.setFirstName(firstName);
			customer.setLastName(lastName);
			s.merge(customer);

			if (loginOnSuccess) setCurrentUser(customer);

			return true;
		});
	}

	public static void logout() {
		setCurrentUser(null);
	}
}