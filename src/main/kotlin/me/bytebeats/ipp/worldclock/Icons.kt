package me.bytebeats.ipp.worldclock

import com.intellij.openapi.util.IconLoader
import me.bytebeats.ipp.worldclock.ui.WorldClockPresentation
import javax.swing.Icon

fun getFlagIcon(flag: String): Icon? = try {
    IconLoader.getIcon("/flags/$flag.svg", WorldClockPresentation::class.java)
} catch (ignore: Exception) {
    null
}

