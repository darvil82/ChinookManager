package chinookMgr.frontend.components;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public abstract class Tool {
	public abstract @NotNull JPanel getPanel();

	public abstract @NotNull String getName();
}