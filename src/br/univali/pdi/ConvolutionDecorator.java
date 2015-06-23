package br.univali.pdi;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public abstract class ConvolutionDecorator extends GrayScaleDecorator {

	protected int matrixSize;
	
	protected Object[][] prefs = {};
	int offset;

	
	public ConvolutionDecorator(int matrixSize) {
		this.matrixSize = matrixSize;
		offset = matrixSize/2;
	}
	
	@Override
	public BufferedImage draw(BufferedImage img) {
		super.draw(img);
		
		
		Raster raster = img.getData();

		for (int j = offset; j < raster.getHeight() - offset; j++)
		 {
			 for (int i = offset; i < raster.getWidth() - offset; i++)
			 {
				 applyMask(raster, i,j,img);
			 }
		 }
		
		return img;
		
	}

	public abstract void applyMask(Raster raster, int x, int y, BufferedImage img);
	

	@Override
	public Object[][] getPrefs() {
		return prefs;
	}
	
	@Override
	public String toString() {
		return "Convolution";
	}

}
