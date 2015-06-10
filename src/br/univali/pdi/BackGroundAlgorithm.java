package br.univali.pdi;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public abstract class BackGroundAlgorithm extends Algorithm {

	protected boolean substract;
	
	@Override
	protected JPanel buildParams() {
		
		JPanel panel = new JPanel();
		
		final JCheckBox checkBox = new JCheckBox("Subtrair background");
		panel.add(checkBox);
		
		checkBox.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				substract = checkBox.isSelected();
			}
		});
		
		return panel;
	}
}
