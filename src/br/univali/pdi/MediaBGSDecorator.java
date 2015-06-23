package br.univali.pdi;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class MediaBGSDecorator extends GrayScaleDecorator {
	

	private Raster[] rasters;
	private int cursor = 0;
	private int bufferSize;
	

	
	public MediaBGSDecorator(int bufferSize) {
		rasters = new Raster[bufferSize];
		this.bufferSize = bufferSize;
	}

	@Override
	public BufferedImage draw(BufferedImage img) {
		
		//super.draw(img);
		
		Raster raster = img.getData();
		
		rasters[cursor] = raster;
		

		int w = img.getWidth();
		int h = img.getHeight();

		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {

				int[] rgb1 = new int[3];
				int[] rgbResult = new int[]{0,0,0};
				int counter = 0;

				for (Raster r : rasters) {
					if (r != null) {

						rgb1 = r.getPixel(i, j, rgb1);

						rgbResult[0] += rgb1[0];
						rgbResult[1] += rgb1[1];
						rgbResult[2] += rgb1[2];
						counter++;
					}
				}
				
				rgb1 = rasters[cursor].getPixel(i, j, rgb1);
//				
//				rgbResult[0] = Math.max(0, rgbResult[0]/counter); 
//				rgbResult[1] = Math.max(0, rgbResult[1]/counter); 
//				rgbResult[2] = Math.max(0, rgbResult[2]/counter); 
				
				rgbResult[0] = Math.max(0,rgb1[0] - rgbResult[0]/counter); 
				rgbResult[1] = Math.max(0,rgb1[1] - rgbResult[1]/counter); 
				rgbResult[2] = Math.max(0,rgb1[2] - rgbResult[2]/counter); 				

				int rgbInt = new Color(rgbResult[0], rgbResult[1], rgbResult[2]).getRGB();
				img.setRGB(i, j, rgbInt);
			}
		}
		
		cursor = ( cursor + 1) % bufferSize;

		
		return img;
		
	}

	@Override
	public Object[][] getPrefs() {
		return new Object[0][0];
	}
	
	@Override
	public String toString() {
		return "Background Substract";
	}

}
