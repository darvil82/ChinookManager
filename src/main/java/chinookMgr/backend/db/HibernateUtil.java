package chinookMgr.backend.db;

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

public class HibernateUtil {
	private static SessionFactory sessionFactory;
	public static Consumer<Boolean> onConnectionChange;
	private static boolean isConnected = false;

	public static boolean init(@NotNull Consumer<Boolean> onConnectionChange) {
		try {
			try (var x = new StandardServiceRegistryBuilder().configure().build()) {
				sessionFactory = new Configuration().configure().buildSessionFactory(x);
			}
		} catch (Exception ex) {
			return false;
		}

		HibernateUtil.onConnectionChange = onConnectionChange;
		isConnected = true;
		new CheckingThread();
		return true;
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


	public static void withSession(@NotNull Consumer<Session> consumer) {
		try (var session = getSession()) {
			consumer.accept(session);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(
				null,
				"Ha ocurrido un error al realizar la operación. Abortando.",
				"Error",
				JOptionPane.ERROR_MESSAGE
			);
			SwingUtilities.invokeLater(ViewStack.current()::replaceWithWelcome);
		}
	}
}