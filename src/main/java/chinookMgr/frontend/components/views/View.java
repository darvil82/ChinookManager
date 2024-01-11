package chinookMgr.frontend.components.views;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public abstract class View {

	public abstract @NotNull JPanel getPanel();

	public abstract @NotNull String getName();

	public boolean disableBackButton() {
		return false;
	}
	public void onReMount(@Nullable View prevView) {}
}