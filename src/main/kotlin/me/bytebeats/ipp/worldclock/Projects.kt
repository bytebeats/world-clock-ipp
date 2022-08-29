package me.bytebeats.ipp.worldclock

import com.intellij.ide.projectView.ProjectView
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.wm.WindowManager
import me.bytebeats.ipp.worldclock.ui.DigitalClockStatusWidget

/**
 * Return true if the project can be manipulated. Project is not null, not disposed, etc.
 */
private fun Project?.isActive(): Boolean = this?.isDisposed ?: false

private fun Project?.refresh() {
    if (isActive()) {
        ProjectView.getInstance(this!!).apply {
            val statusBar = WindowManager.getInstance().getStatusBar(this@refresh)
            for (id in listOf(CLOCK_WIDGET_ID_1, CLOCK_WIDGET_ID_2)) {
                statusBar.updateWidget(id)
                (statusBar.getWidget(id) as? DigitalClockStatusWidget)?.refresh()
            }
        }
    }
}

fun ProjectManager.refreshActiveProjects() {
    openProjects.forEach { it.refresh() }
}