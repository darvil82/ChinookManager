package chinookMgr.backend.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class HibernateUtil {
	private static SessionFactory sessionFactory;
	public static Consumer<Boolean> onConnectionChange;
	private static boolean isConnected = false;

	public static void init(@NotNull Consumer<Boolean> onConnectionChange) {
		HibernateUtil.onConnectionChange = onConnectionChange;

		try {
			sessionFactory = new Configuration().configure().buildSessionFactory(
				new StandardServiceRegistryBuilder().configure().build()
			);
			isConnected = true;
			new CheckingThread().start();
		} catch (Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static class CheckingThread extends Thread {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				try (var session = getSession()) {
					session.createQuery("select 1", Integer.class).list();

					if (!isConnected)
						onConnectionChange.accept(isConnected = true);
				} catch (Exception e) {
					if (isConnected) {
						onConnectionChange.accept(isConnected = false);
					}
				}
			}
		}
	}

	public static Session getSession() {
		return sessionFactory.openSession();
	}


	public static void withSession(Consumer<Session> consumer) {
		try (var session = getSession()) {
			consumer.accept(session);
		} catch (Exception e) {

		}
	}
}