package chinookMgr.backend;

import chinookMgr.frontend.Theme;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public final class Configuration implements Serializable {
	public int rowsPerPage = 100;
	public @NotNull Theme theme = Theme.ONE_DARK;
	public volatile int statusMessageDelay = 3000;


	private Configuration() {}
	private static Configuration instance;
	public static File CONFIG_FILE = new File("config.dat");

	public static @NotNull Configuration current() {
		return instance;
	}

	public void save() {
		try {
			new ObjectOutputStream(new FileOutputStream(CONFIG_FILE)).writeObject(this);
			System.out.println("Configuration saved.");
		} catch (IOException e) {
			System.out.println("Error saving configuration file.");
		}
	}

	public static @NotNull Configuration getFromFile(@NotNull File file) {
		try {
			return (Configuration)new ObjectInputStream(new FileInputStream(file)).readObject();
		} catch (Exception e) {
			System.out.println("Error reading configuration file. Using default configuration.");
			return getDefault();
		}
	}

	public static void loadFromFile() {
		instance = getFromFile(CONFIG_FILE);
		System.out.println("Configuration loaded.");
	}

	public static void setConfig(@NotNull Configuration config) {
		instance = config;
	}

	public static @NotNull Configuration getDefault() {
		return new Configuration();
	}
}