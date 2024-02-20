package chinookMgr.backend.db;

import chinookMgr.frontend.ErrorDialog;
import chinookMgr.frontend.MainMenu;
import chinookMgr.frontend.ViewStack;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.service.spi.ServiceException;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HibernateUtil {
	private static SessionFactory sessionFactory;
	public static Consumer<Boolean> onConnectionChange;
	private static boolean isConnected = false;

	public static void init(@NotNull Consumer<Boolean> onConnectionChange) {
		Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
		try (var x = new StandardServiceRegistryBuilder().configure().build()) {
			sessionFactory = new Configuration().configure().buildSessionFactory(x);
		}

		HibernateUtil.onConnectionChange = onConnectionChange;
		isConnected = true;
		new CheckingThread();
	}

	private static void invokeOnConnectionChange(boolean isConnected) {
		if (onConnectionChange != null)
			SwingUtilities.invokeLater(() -> onConnectionChange.accept(isConnected));
	}

	public static class CheckingThread extends Thread {
		public CheckingThread() {
			this.start();
		}

		@Override
		public void run() {
			while (!this.isInterrupted()) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}

				try (var session = getSession()) {
					session.createQuery("select 1", Integer.class).list();

					if (!isConnected)
						invokeOnConnectionChange(isConnected = true);
				} catch (JDBCConnectionException e) {
					if (isConnected)
						invokeOnConnectionChange(isConnected = false);
				}
			}
		}
	}

	public static Session getSession() {
		return sessionFactory.openSession();
	}


	public static <T> T withSession(@NotNull Function<Session, @NotNull T> consumer) {
		try (var session = getSession()) {
			session.beginTransaction();
			var result = consumer.apply(session);
			session.getTransaction().commit();
			return result;
		} catch (Exception e) {
			operationError(e);
		}
		return null;
	}

	public static void withSession(@NotNull Consumer<Session> consumer) {
		try (var session = getSession()) {
			session.beginTransaction();
			consumer.accept(session);
			session.getTransaction().commit();
		} catch (Exception e) {
			operationError(e);
		}
	}

	private static void operationError(Throwable e) {
		ErrorDialog.display(ViewStack.currentPanel(), "Ha ocurrido un error al realizar la operación. Abortando.", e);
		ViewStack.popAllButLast();
		SwingUtilities.invokeLater(ViewStack.current()::replaceWithWelcome);
	}
}