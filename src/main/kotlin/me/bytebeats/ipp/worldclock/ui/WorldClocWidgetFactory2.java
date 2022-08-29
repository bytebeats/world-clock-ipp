package me.bytebeats.ipp.worldclock.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.StatusBarWidgetFactory;
import me.bytebeats.ipp.worldclock.ConstsKt;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class WorldClocWidgetFactory2 implements StatusBarWidgetFactory {
    @Override
    public @NonNls
    @NotNull String getId() {
        return ConstsKt.CLOCK_WIDGET_ID_2;
    }

    @Override
    public @Nls
    @NotNull String getDisplayName() {
        return "World Clock 2";
    }

    @Override
    public boolean isAvailable(@NotNull Project project) {
        return true;
    }

    @Override
    public @NotNull StatusBarWidget createWidget(@NotNull Project project) {
        return new DigitalClockStatusWidget(ConstsKt.CLOCK_WIDGET_ID_2, project);
    }

    @Override
    public void disposeWidget(@NotNull StatusBarWidget statusBarWidget) {

    }

    @Override
    public boolean canBeEnabledOn(@NotNull StatusBar statusBar) {
        return true;
    }
}
