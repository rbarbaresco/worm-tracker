package br.univali.pdi;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class DilatationDecorator extends ConvolutionDecorator {

	public DilatationDecorator(int matrixSize) {
		super(matrixSize);
		prefs = new Object[matrixSize*matrixSize][2];
		for (int i = 0; i < matrixSize*matrixSize; i++) {
			prefs[i][0] = "x"+i%matrixSize+";y"+i/matrixSize;
			prefs[i][1] = new Integer(0);
		}
	}
	

	@Override
	public void applyMask(Raster raster, int x, int y,BufferedImage img) {
		
		int min =-(matrixSize/2);
		int max = matrixSize/2;
		
		int whiteRgbInt = new Color(255, 255, 255).getRGB();
		int hotSpot = raster.getPixel(x, y, new int[3])[0];
		if(hotSpot == 255){
			
			
			for (int xi = min; xi <= max; xi++) {
				for (int yj = min; yj <= max; yj++) {
					int maskPoint = (int) prefs[(yj+1)*matrixSize+(xi+1)][1];
					int resultPoint = whiteRgbInt*maskPoint;
					if(resultPoint != 0)
						img.setRGB(x+xi, y+yj, resultPoint);
				}
			}
		
		}
	}

	@Override
	public String toString() {
		return "Dilatation";
	}

}
