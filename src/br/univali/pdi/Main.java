package br.univali.pdi;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractorMOG2;

public class Main {

	public static void main(String[] args) throws Exception {
		
		nu.pattern.OpenCV.loadShared();
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		Mat frame = new Mat();
		VideoCapture camera = new VideoCapture("files/08-amp.mp4");
		JFrame jframe = new JFrame("Title");
		jframe.setExtendedState(JFrame.MAXIMIZED_BOTH);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel vidPanel = new JLabel();
		jframe.setContentPane(vidPanel);
		jframe.setVisible(true);
		
		MediaBGSDecorator ms = new MediaBGSDecorator(15);
		DilatationDecorator di  = new DilatationDecorator(3);
		di.getPrefs()[0][1] = 1;
		di.getPrefs()[1][1] = 1;
		di.getPrefs()[2][1] = 1;
		di.getPrefs()[3][1] = 1;
		di.getPrefs()[4][1] = 1;
		di.getPrefs()[5][1] = 1;
		di.getPrefs()[6][1] = 1;
		di.getPrefs()[7][1] = 1;
		di.getPrefs()[8][1] = 1;
		
		BackgroundSubtractorMOG2 bgs = new BackgroundSubtractorMOG2();
		
		while (true) {
			
			if (camera.read(frame)) {
				Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGB2GRAY);
				Imgproc.threshold(frame, frame, 65, 255, Imgproc.THRESH_BINARY_INV);
				//Imgproc.inv
				
				Imgproc.dilate(frame, frame, Imgproc.getStructuringElement(Imgproc.MORPH_DILATE, new Size(8,8)));
				List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
				Mat hierarchy = new Mat();
				//alphaBGS(bi);
				//thinIt(bi);
				//ms.draw(bi);
				//di.draw(bi);
				
				
				Imgproc.findContours(frame, contours, hierarchy,
						Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);

				double largest_area = 0;
				int largest_contour_index = 0;
				Rect bounding_rect;
				for (int i = 0; i < contours.size(); i++) {
					double a = Imgproc.contourArea(contours.get(i), false);
					if (a > largest_area) {
						largest_area = a;
						largest_contour_index = i;
						bounding_rect = Imgproc.boundingRect(contours.get(i));
					}
				}
				
				Imgproc.cvtColor(frame, frame, Imgproc.COLOR_GRAY2RGB);

				Scalar color = new Scalar(255, 0, 0);

				ArrayList<MatOfPoint> arrayList = new ArrayList<MatOfPoint>();
				arrayList.add(contours.get(largest_contour_index));

				Imgproc.drawContours( frame, arrayList,0, color,-1 ); // Draw the largest contour using previously stored index.
				
				//Imgproc.rectangle(src, bounding_rect,  Scalar(0,255,0),1, 8,0); 
				
				BufferedImage bi = Mat2BufferedImage(frame);
				ImageIcon image = new ImageIcon(bi);
				vidPanel.setIcon(image);
				vidPanel.repaint();
				
			}
			
		}
	}

	public static BufferedImage Mat2BufferedImage(Mat m) {
		// source:
		// http://answers.opencv.org/question/10344/opencv-java-load-image-to-gui/
		// Fastest code
		// The output can be assigned either to a BufferedImage or to an Image

		int type = BufferedImage.TYPE_BYTE_GRAY;
		if (m.channels() > 1) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}
		int bufferSize = m.channels() * m.cols() * m.rows();
		byte[] b = new byte[bufferSize];
		m.get(0, 0, b); // get all the pixels
		BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(b, 0, targetPixels, 0, b.length);
		return image;

	}
		
	
	
	public static BufferedImage thinIt(BufferedImage image) throws IOException {
	     
        //BufferedImage image = ImageIO.read(new File("files/bw1.jpg"));
 
        int[][] imageData = new int[image.getHeight()][image.getWidth()];
        Color c;
        for (int y = 0; y < imageData.length; y++) {
            for (int x = 0; x < imageData[y].length; x++) {
 
                if (image.getRGB(x, y) == Color.BLACK.getRGB()) {
                    imageData[y][x] = 1;
                } else {
                    imageData[y][x] = 0;
 
                }
            }
        }
 
        ThinningService thinningService = new ThinningService();
     
        thinningService.doZhangSuenThinning(imageData);
         
        for (int y = 0; y < imageData.length; y++) {
 
            for (int x = 0; x < imageData[y].length; x++) {
 
                if (imageData[y][x] == 1) {
                    image.setRGB(x, y, Color.BLACK.getRGB());
 
                } else {
                    image.setRGB(x, y, Color.WHITE.getRGB());
                }
 
 
            }
        }
 
        return image;
        //ImageIO.write(image, "jpg", new File("files/bwThin.jpg"));
 
    }
	
	
	public static BufferedImage grayScale(BufferedImage img) {
		
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
	
	
	
	private static Raster lastRaster;
	
	private static float alpha= 0.03f;
	private static float beta = 0.97f;
	
	
	public static BufferedImage alphaBGS(BufferedImage img) {
		
		Raster raster = img.getData();
		
		BufferedImage imgCopy = deepCopy(img);
		
		if(lastRaster == null)
			lastRaster = raster;
		
		

		int w = img.getWidth();
		int h = img.getHeight();

		for (int j = 0; j < h; j++) {
			for (int i = 0; i < w; i++) {

				int[] rgb1 =  raster.getPixel(i, j, new int[3]);
				int[] rgb2 = lastRaster.getPixel(i, j, new int[3]);
				
				int[] rgbResult = new int[]{0,0,0};
				
				
				
				
				rgbResult[0] = (int) ((rgb1[0] * alpha + rgb2[0] * beta));
				rgbResult[1] = (int) ((rgb1[1] * alpha + rgb2[1] * beta));
				rgbResult[2] = (int) ((rgb1[2] * alpha + rgb2[2] * beta));
				
				
				int rgbInt = new Color(rgbResult[0], rgbResult[1], rgbResult[2]).getRGB();
				imgCopy.setRGB(i, j, rgbInt);
				
				
				int rgbInt2 = new Color(Math.abs(rgb1[0]-rgbResult[0]),
						Math.abs(rgb1[1]- rgbResult[1]),
						Math.abs(rgb1[2]-rgbResult[2])).getRGB();
				img.setRGB(i, j, rgbInt2);
				
		
			}
		}
		
		lastRaster = imgCopy.getData();

		return img;
		
	}
	
	public static BufferedImage deepCopy(BufferedImage bi) {
		 ColorModel cm = bi.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = bi.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}


}