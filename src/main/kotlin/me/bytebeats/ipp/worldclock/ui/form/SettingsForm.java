package me.bytebeats.ipp.worldclock.ui.form;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.ProjectManager;
import me.bytebeats.ipp.worldclock.ConstsKt;
import me.bytebeats.ipp.worldclock.ProjectsKt;
import me.bytebeats.ipp.worldclock.TimeZoneFlagsKt;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SettingsForm implements Configurable {
    private JPanel mMainPane;
    private JLabel mClock1Label;
    private JComboBox<String> mTimeZoneComboBox1;
    private JCheckBox mEnableClock1CheckBox;
    private JLabel mClock2Label;
    private JComboBox<String> mTimeZoneComboBox2;
    private JCheckBox mEnableClock2CheckBox;
    private JCheckBox m24hTimeFormatCheckBox;

    private boolean modified = false;
    private final PropertiesComponent pc = PropertiesComponent.getInstance();

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "World Clock Options";
    }

    @Override
    public @Nullable JComponent createComponent() {
        ActionListener actionListener = e -> modified = true;

        String storedTimeZone1 = pc.getValue(ConstsKt.PC_KEY_TIME_ZONE_1, ConstsKt.DEFAULT_TIME_ZONE_1);
        String storedTimeZone2 = pc.getValue(ConstsKt.PC_KEY_TIME_ZONE_2, ConstsKt.DEFAULT_TIME_ZONE_2);

        List<String> tzs = TimeZoneFlagsKt.getAllAvailableTZAndFlags().keySet().stream()
                .sorted(Comparator.comparing(String::toUpperCase))
                .collect(Collectors.toList());
        for (int placeIdx = 0; placeIdx < tzs.size(); placeIdx++) {
            mTimeZoneComboBox1.addItem(tzs.get(placeIdx));
            mTimeZoneComboBox2.addItem(tzs.get(placeIdx));
            if (tzs.get(placeIdx).equalsIgnoreCase(storedTimeZone1)) {
                mTimeZoneComboBox1.setSelectedIndex(placeIdx);
            }
            if (tzs.get(placeIdx).equalsIgnoreCase(storedTimeZone2)) {
                mTimeZoneComboBox2.setSelectedIndex(placeIdx);
            }
        }

        mTimeZoneComboBox1.addActionListener(actionListener);

        mEnableClock1CheckBox.setSelected(pc.getBoolean(ConstsKt.PC_KEY_CLOCK_ENABLE_1, true));
        mEnableClock1CheckBox.addActionListener(actionListener);

        mTimeZoneComboBox2.addActionListener(actionListener);

        mEnableClock2CheckBox.setSelected(pc.getBoolean(ConstsKt.PC_KEY_CLOCK_ENABLE_2, true));
        mEnableClock2CheckBox.addActionListener(actionListener);

        m24hTimeFormatCheckBox.setSelected(pc.getBoolean(ConstsKt.PC_KEY_USE_24H_TIME_FORMAT, true));
        m24hTimeFormatCheckBox.addActionListener(actionListener);

        return mMainPane;
    }

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void apply() {
        pc.setValue(ConstsKt.PC_KEY_TIME_ZONE_1, (String) mTimeZoneComboBox1.getSelectedItem());
        pc.setValue(ConstsKt.PC_KEY_TIME_ZONE_2, (String) mTimeZoneComboBox2.getSelectedItem());
        pc.setValue(ConstsKt.PC_KEY_CLOCK_ENABLE_1, mEnableClock1CheckBox.isSelected());
        pc.setValue(ConstsKt.PC_KEY_CLOCK_ENABLE_2, mEnableClock2CheckBox.isSelected());
        pc.setValue(ConstsKt.PC_KEY_USE_24H_TIME_FORMAT, m24hTimeFormatCheckBox.isSelected());
        modified = false;
        ProjectsKt.refreshActiveProjects(ProjectManager.getInstance());
    }

    @Override
    public void reset() {
        String storedTimeZone1 = pc.getValue(ConstsKt.PC_KEY_TIME_ZONE_1, ConstsKt.DEFAULT_TIME_ZONE_1);
        String storedTimeZone2 = pc.getValue(ConstsKt.PC_KEY_TIME_ZONE_2, ConstsKt.DEFAULT_TIME_ZONE_2);

        List<String> tzs = TimeZoneFlagsKt.getAllAvailableTZAndFlags()
                .keySet()
                .stream()
                .sorted(Comparator.comparing(String::toUpperCase))
                .collect(Collectors.toList());
        for (int placeIdx = 0; placeIdx < tzs.size(); placeIdx++) {
            if (tzs.get(placeIdx).equalsIgnoreCase(storedTimeZone1)) {
                mTimeZoneComboBox1.setSelectedIndex(placeIdx);
            }
            if (tzs.get(placeIdx).equalsIgnoreCase(storedTimeZone2)) {
                mTimeZoneComboBox2.setSelectedIndex(placeIdx);
            }
        }

        mEnableClock1CheckBox.setSelected(pc.getBoolean(ConstsKt.PC_KEY_CLOCK_ENABLE_1, true));
        mEnableClock2CheckBox.setSelected(pc.getBoolean(ConstsKt.PC_KEY_CLOCK_ENABLE_2, true));
        m24hTimeFormatCheckBox.setSelected(pc.getBoolean(ConstsKt.PC_KEY_USE_24H_TIME_FORMAT, true));
        modified = false;
    }
}
