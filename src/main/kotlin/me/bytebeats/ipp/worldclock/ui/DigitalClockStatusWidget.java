package me.bytebeats.ipp.worldclock.ui;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.WindowManager;
import me.bytebeats.ipp.worldclock.ConstsKt;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class DigitalClockStatusWidget implements StatusBarWidget {
    private final String widgetID;
    private final StatusBar statusBar;
    private final PropertiesComponent pc = PropertiesComponent.getInstance();

    private Timer timer;

    @Contract(pure = true)
    public DigitalClockStatusWidget(String widgetID, Project project) {
        this.widgetID = widgetID;
        statusBar = WindowManager.getInstance().getStatusBar(project);
    }

    @Override
    public @NonNls
    @NotNull String ID() {
        return widgetID;
    }

    @Override
    public void install(@NotNull StatusBar statusBar) {
        ApplicationManager.getApplication().executeOnPooledThread(() -> startClockIfNot(statusBar));
    }

    private void startClockIfNot(StatusBar statusBar) {
        if (ConstsKt.WORLD_CLOCK_WIDGET_ID_1.equals(widgetID) && pc.getBoolean(ConstsKt.PC_KEY_CLOCK_ENABLE_1, true)
                || ConstsKt.WORLD_CLOCK_WIDGET_ID_2.equals(widgetID) && pc.getBoolean(ConstsKt.PC_KEY_CLOCK_ENABLE_2, true)) {
            try {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        statusBar.updateWidget(widgetID);
                    }
                }, 0L, 30000L);
            } catch (Exception ignore) {
                // do nothing here
            }
        }
    }

    public void refresh() {
        dispose();
        startClockIfNot(statusBar);
    }

    @Override
    public void dispose() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    @Override
    public @Nullable WidgetPresentation getPresentation() {
        return new WorldClockPresentation(widgetID);
    }
}
