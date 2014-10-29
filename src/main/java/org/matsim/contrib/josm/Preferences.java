package org.matsim.contrib.josm;

import org.matsim.core.utils.geometry.transformations.TransformationFactory;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.gui.preferences.DefaultTabPreferenceSetting;
import org.openstreetmap.josm.gui.preferences.PreferenceSetting;
import org.openstreetmap.josm.gui.preferences.PreferenceSettingFactory;
import org.openstreetmap.josm.gui.preferences.PreferenceTabbedPane;
import org.openstreetmap.josm.gui.preferences.PreferenceTabbedPane.PreferencePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.openstreetmap.josm.tools.I18n.tr;

/**
 * Preferences for the MATSim Plugin
 * 
 * 
 */
final class Preferences extends DefaultTabPreferenceSetting {

	private final static JCheckBox renderMatsim = new JCheckBox(
			"Activate MATSim Renderer");
	private final static JCheckBox showIds = new JCheckBox("Show link-Ids");
	private final static JSlider wayOffset = new JSlider(0, 100);
	private final static JLabel wayOffsetLabel = new JLabel(
			"Link offset for overlapping links");
	private final static JCheckBox showInternalIds = new JCheckBox(
			"Show internal Ids in table");

	private final static JCheckBox cleanNetwork = new JCheckBox("Clean Network");
	private final static JCheckBox keepPaths = new JCheckBox("Keep Paths");

	static final String[] coordSystems = { TransformationFactory.WGS84,
			TransformationFactory.ATLANTIS, TransformationFactory.CH1903_LV03,
			TransformationFactory.GK4, TransformationFactory.WGS84_UTM47S,
			TransformationFactory.WGS84_UTM48N,
			TransformationFactory.WGS84_UTM35S,
			TransformationFactory.WGS84_UTM36S,
			TransformationFactory.WGS84_Albers,
			TransformationFactory.WGS84_SA_Albers,
			TransformationFactory.WGS84_UTM33N, TransformationFactory.DHDN_GK4,
			TransformationFactory.WGS84_UTM29N,
			TransformationFactory.CH1903_LV03_GT,
			TransformationFactory.WGS84_SVY21,
			TransformationFactory.NAD83_UTM17N, TransformationFactory.WGS84_TM };

	private final JButton convertingDefaults = new JButton("Set converting defaults");

	private final static JCheckBox filterActive = new JCheckBox(
			"Activate hierarchy filter");
	private final static JLabel hierarchyLabel = new JLabel(
			"Only convert hierarchies up to: ");
	private final static JTextField hierarchyLayer = new JTextField();

	public static class Factory implements PreferenceSettingFactory {
		@Override
		public PreferenceSetting createPreferenceSetting() {
			return new Preferences();
		}
	}

	private Preferences() {
		super("matsim-scenario.png", tr("MASim preferences"),
				tr("Configure the MATSim plugin."), false, new JTabbedPane());
	}

	private JPanel buildVisualizationPanel() {
		JPanel pnl = new JPanel(new GridBagLayout());
		GridBagConstraints cOptions = new GridBagConstraints();

		wayOffset
				.setValue((int) ((Main.pref.getDouble("matsim_wayOffset", 0)) / 0.03));

		showIds.setSelected(Main.pref.getBoolean("matsim_showIds")
				&& Main.pref.getBoolean("matsim_renderer"));
		renderMatsim.setSelected(Main.pref.getBoolean("matsim_renderer"));
		wayOffset.setEnabled(Main.pref.getBoolean("matsim_renderer"));
		showIds.setEnabled(Main.pref.getBoolean("matsim_renderer"));
		wayOffsetLabel.setEnabled(Main.pref.getBoolean("matsim_renderer"));
		showInternalIds.setSelected(Main.pref.getBoolean(
				"matsim_showInternalIds", false));

		cOptions.anchor = GridBagConstraints.NORTHWEST;

		cOptions.insets = new Insets(4, 4, 4, 4);

		cOptions.weightx = 0;
		cOptions.weighty = 0;
		cOptions.gridx = 0;
		cOptions.gridy = 0;
		pnl.add(renderMatsim, cOptions);

		cOptions.gridy = 1;
		pnl.add(showIds, cOptions);

		cOptions.weightx = 0;
		cOptions.gridy = 2;
		pnl.add(wayOffsetLabel, cOptions);

		cOptions.weightx = 1;
		cOptions.gridx = 1;
		pnl.add(wayOffset, cOptions);

		cOptions.weightx = 0;
		cOptions.weighty = 1;
		cOptions.gridx = 0;
		cOptions.gridy = 3;
		pnl.add(showInternalIds, cOptions);

		return pnl;
	}

	private JPanel buildConvertPanel() {
		JPanel pnl = new JPanel(new GridBagLayout());
		GridBagConstraints cOptions = new GridBagConstraints();

		cleanNetwork.setSelected(Main.pref.getBoolean("matsim_cleanNetwork",
				true));
		keepPaths.setSelected(Main.pref.getBoolean("matsim_keepPaths",
				false));
		
		convertingDefaults.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				OsmConvertDefaultsDialog dialog = new OsmConvertDefaultsDialog();
				JOptionPane pane = new JOptionPane(dialog,
						JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
				JDialog dlg = pane.createDialog(Main.parent, tr("Defaults"));
				dlg.setAlwaysOnTop(true);
				dlg.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				dlg.setVisible(true);
				if (pane.getValue() != null) {
					if (((Integer) pane.getValue()) == JOptionPane.OK_OPTION) {
						dialog.handleInput();
						if (Main.main.getActiveLayer() != null) {
							Main.main.getCurrentDataSet().clearSelection();
							MATSimPlugin.toggleDialog.activeLayerChange(Main.main.getActiveLayer(), Main.main.getActiveLayer());
						}
					}
				}
				dlg.dispose();
			}
		});

		filterActive.setSelected(Main.pref.getBoolean("matsim_filterActive",
				false));
		hierarchyLayer.setText(String.valueOf(Main.pref.getInteger(
				"matsim_filter_hierarchy", 6)));

		cOptions.anchor = GridBagConstraints.NORTHWEST;

		cOptions.insets = new Insets(4, 4, 4, 4);

		cOptions.weighty = 0;
		cOptions.weightx = 0;
		cOptions.gridx = 0;
		cOptions.gridy = 0;
		pnl.add(cleanNetwork, cOptions);
		
		cOptions.gridy = 1;
		pnl.add(keepPaths, cOptions);

		cOptions.gridy = 2;
		pnl.add(convertingDefaults, cOptions);

		cOptions.gridy = 3;
		pnl.add(filterActive, cOptions);

		cOptions.gridy = 4;
		pnl.add(hierarchyLabel, cOptions);
		cOptions.gridx = 1;
		pnl.add(hierarchyLayer, cOptions);
		
		cOptions.weighty = 1;
		cOptions.weightx = 1;
		cOptions.fill = GridBagConstraints.HORIZONTAL;
		cOptions.gridwidth = 2;
		cOptions.gridx = 0;
		cOptions.gridy = 5;
		JSeparator jSep = new JSeparator(SwingConstants.HORIZONTAL);
		pnl.add(jSep, cOptions);

		return pnl;
	}

	JTabbedPane buildContentPane() {
		JTabbedPane pane = getTabPane();
		pane.addTab(tr("Visualization"), buildVisualizationPanel());
		pane.addTab(tr("Converter Options"), buildConvertPanel());
		return pane;
	}

	@Override
	public void addGui(final PreferenceTabbedPane gui) {
		GridBagConstraints gc = new GridBagConstraints();
		gc.weightx = 1.0;
		gc.weighty = 1.0;
		gc.anchor = GridBagConstraints.NORTHWEST;
		gc.fill = GridBagConstraints.BOTH;
		PreferencePanel panel = gui.createPreferenceTab(this);
		panel.add(buildContentPane(), gc);
	}

	@Override
	public boolean ok() {
		if (!showIds.isSelected()) {
			Main.pref.put("matsim_showIds", false);
		} else {
			Main.pref.put("matsim_showIds", true);
		}
		if (!renderMatsim.isSelected()) {
			Main.pref.put("matsim_renderer", false);
		} else {
			Main.pref.put("matsim_renderer", true);
		}
		if (!cleanNetwork.isSelected()) {
			Main.pref.put("matsim_cleanNetwork", false);
		} else {
			Main.pref.put("matsim_cleanNetwork", true);
		}
		if (!keepPaths.isSelected()) {
			if (Main.pref.put("matsim_keepPaths", false)) {
				NewConverter.keepPaths = false;
				if (Main.main.getActiveLayer() != null) {
					Main.main.getCurrentDataSet().clearSelection();
					MATSimPlugin.toggleDialog.activeLayerChange(Main.main.getActiveLayer(), Main.main.getActiveLayer());
				}
			}
		} else {
			if (Main.pref.put("matsim_keepPaths", true)) {
				NewConverter.keepPaths = true;
				if (Main.main.getActiveLayer() != null) {
					Main.main.getCurrentDataSet().clearSelection();
					MATSimPlugin.toggleDialog.activeLayerChange(Main.main.getActiveLayer(), Main.main.getActiveLayer());
				}
			}
		}
		if (showInternalIds.isSelected()) {
			Main.pref.put("matsim_showInternalIds", true);
		} else {
			Main.pref.put("matsim_showInternalIds", false);
		}
		if (filterActive.isSelected()) {
			Main.pref.put("matsim_filterActive", true);
		} else {
			Main.pref.put("matsim_filterActive", false);
		}
		Main.pref.putInteger("matsim_filter_hierarchy",
				Integer.parseInt(hierarchyLayer.getText()));
		int temp = wayOffset.getValue();
		double offset = ((double) temp) * 0.03;
		Main.pref.putDouble("matsim_wayOffset", offset);
		return false;
	}
}
