package br.univali.pdi;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class BackGroundSubstractionAlfa extends BackGroundAlgorithm {

	private Raster lastFrame;
	private final double alfaValue;
	private final double omegaValue;
	
	public BackGroundSubstractionAlfa() {
		this(0.05);
	}
	
	public BackGroundSubstractionAlfa(double alfaValue) {
		this.alfaValue = alfaValue;
		omegaValue = 1 - alfaValue;
	}
	
	@Override
	public String getName() {
		return "Back Ground Alfa";
	}

	@Override
	public BufferedImage execute(BufferedImage image) {
		
		if (lastFrame == null) {
			lastFrame = image.getData();
			return image;
		}
		
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		BufferedImage copyImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		int[] rgb = new int[3];
		int[] rgb_ = new int[3];

		Raster raster = image.getData();
		for (int j = 0; j < raster.getHeight(); j++) {
			for (int i = 0; i < raster.getWidth(); i++) {
				
				rgb = lastFrame.getPixel(i, j, rgb);
				rgb_ = raster.getPixel(i, j, rgb_);
				
				int currentIndexR = (int) (rgb[0] * omegaValue + rgb_[0] * alfaValue);
				int currentIndexG = (int) (rgb[1] * omegaValue + rgb_[1] * alfaValue);
				int currentIndexB = (int) (rgb[2] * omegaValue + rgb_[2] * alfaValue);
				
				if (currentIndexR > 255) currentIndexR = 255;
				if (currentIndexG > 255) currentIndexG = 255;
				if (currentIndexB > 255) currentIndexB = 255;
				
				copyImage.setRGB(i, j, new Color(currentIndexR, currentIndexG, currentIndexB).getRGB());
				
				if (substract) {
					currentIndexR = rgb_[0] - currentIndexR;
					currentIndexG = rgb_[1] - currentIndexG;
					currentIndexB = rgb_[2] - currentIndexB;
				}
				
				if (currentIndexR < 0) currentIndexR = 0;
				if (currentIndexG < 0) currentIndexG = 0;
				if (currentIndexB < 0) currentIndexB = 0;
				
				newImage.setRGB(i, j, new Color(currentIndexR, currentIndexG, currentIndexB).getRGB());
			}
		}
		
		lastFrame = copyImage.getData();
		return newImage;
	}

}
