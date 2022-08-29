package me.bytebeats.ipp.worldclock.ui

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.ui.popup.ListPopup
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.util.Consumer
import me.bytebeats.ipp.worldclock.*
import java.awt.event.MouseEvent
import java.time.Instant
import java.time.ZoneId
import javax.swing.Icon

class WorldClockPresentation(private val widgetId: String) : StatusBarWidget.MultipleTextValuesPresentation {
    private val pc by lazy { PropertiesComponent.getInstance() }

    override fun getTooltipText(): String? =
        getTooltipFromTimeZone(
            if (widgetId == WORLD_CLOCK_WIDGET_ID_1) pc.getValue(PC_KEY_CLOCK_TIME_ZONE_1)
            else pc.getValue(PC_KEY_CLOCK_TIME_ZONE_2)
        )

    private fun getTooltipFromTimeZone(timeZone: String?): String? {
        if (timeZone?.startsWith(GMT).orFalse() || timeZone == UTC || timeZone == CST
            || timeZone == EST || timeZone == PST
        ) {
            return timeZone
        }
        val zoneId = ZoneId.of(timeZone)
        return "$zoneId (GMT ${zoneId.rules.getStandardOffset(Instant.now())})"
    }

    override fun getClickConsumer(): Consumer<MouseEvent>? = null

    override fun getPopupStep(): ListPopup? = null

    override fun getSelectedValue(): String? {
        return if (widgetId == WORLD_CLOCK_WIDGET_ID_1) {
            if (pc.getBoolean(PC_KEY_CLOCK_ENABLE_1, true)) {
                getCurrentDateWithTimeZone(pc.getValue(PC_KEY_CLOCK_TIME_ZONE_1, WORLD_CLOCK_DEFAULT_TIME_ZONE_1))
            } else {
                null
            }
        } else {
            if (pc.getBoolean(PC_KEY_CLOCK_ENABLE_2, true)) {
                getCurrentDateWithTimeZone(pc.getValue(PC_KEY_CLOCK_ENABLE_2, WORLD_CLOCK_DEFAULT_TIME_ZONE_2))
            } else {
                null
            }
        }
    }

    private fun getCurrentDateWithTimeZone(timeZone: String?): String {
        if (timeZone?.startsWith(GMT).orFalse() || timeZone == UTC || timeZone == CST
            || timeZone == EST || timeZone == PST
        ) {
            val zoneId = ZoneId.of(timeZone)
            return "$timeZone: ${getCurrentDateWith(zoneId, pc.getBoolean(PC_KEY_USE_24H_TIME_FORMAT))}"
        }
        return " ${getCurrentDateWith(ZoneId.of(timeZone), pc.getBoolean(PC_KEY_USE_24H_TIME_FORMAT))}"
    }

    override fun getIcon(): Icon? {
        val zoneId = if (widgetId == WORLD_CLOCK_WIDGET_ID_1) {
            if (pc.getBoolean(PC_KEY_CLOCK_ENABLE_1, true)) {
                pc.getValue(PC_KEY_CLOCK_TIME_ZONE_1, WORLD_CLOCK_DEFAULT_TIME_ZONE_1)
            } else {
                return null
            }
        } else {
            if (pc.getBoolean(PC_KEY_CLOCK_ENABLE_2, true)) {
                pc.getValue(PC_KEY_CLOCK_TIME_ZONE_2, WORLD_CLOCK_DEFAULT_TIME_ZONE_2)
            } else {
                return null
            }
        }
        return getFlagIcon(zoneId)
    }
}