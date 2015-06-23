package br.univali.pdi;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class GrayScaleDecorator extends ImageDecorator {

	@Override
	public BufferedImage draw(BufferedImage img) {
		
		Raster raster = img.getData();

		for (int j = 0; j < raster.getHeight(); j++)
		 {
			 for (int i = 0; i < raster.getWidth(); i++)
			 {
				 int[] rgb = new int[3];
				
				 
				 rgb = raster.getPixel(i, j, rgb);
				 int gray = (rgb[0]+rgb[1]+rgb[2])/3;
				 rgb[0] = gray;
				 rgb[1] = gray;
				 rgb[2] = gray;
				 int rgbInt = new Color(rgb[0],rgb[1],rgb[2]).getRGB();
				 img.setRGB(i, j, rgbInt);
			 }
		 }
		
		return img;
	}
	
	@Override
	public Object[][] getPrefs() {
		Object[][] prefs = {};
		return prefs;
	}
	
	@Override
	public String toString() {
		return "GrayScale";
	}

}
