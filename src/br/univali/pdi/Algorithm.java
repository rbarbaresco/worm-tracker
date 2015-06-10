package br.univali.pdi;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public abstract class Algorithm {
	
	@Override
	public String toString() {
		return getName();
	}

	public JPanel getParams() {
		JPanel holder = new JPanel(new BorderLayout());
		holder.add(buildParams(), BorderLayout.NORTH);
		return holder;
	}
	
	public abstract String getName();
	protected abstract JPanel buildParams();
	public abstract BufferedImage execute(BufferedImage image);

}
