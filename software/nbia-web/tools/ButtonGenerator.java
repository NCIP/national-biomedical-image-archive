/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ButtonGenerator {
	public static void main(String[] args) throws Exception {
	    // Create buffered image that does not support transparency
	    BufferedImage bimage = new BufferedImage(width,
	    		                                 height,
	    		                                 BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = (Graphics2D)bimage.createGraphics();
//	    g2d.setColor(borderColor);
//	    g2d.setStroke(new BasicStroke(3));
//	    g2d.drawLine(0,0,100,100);
	    drawBackground(g2d);
	    drawBorders(g2d);
	    drawText(g2d);
	    g2d.dispose();

	    ImageIO.write(bimage, "png", new File("c:/test.png"));
	}

	private static void drawText(Graphics2D g2d) {
		Font font = new Font("Arial Black", Font.PLAIN, 11);
		g2d.setFont(font);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(textColor);

		FontMetrics metrics = g2d.getFontMetrics(font);
	    //int hgt = metrics.getHeight();
	    //int textWidth = metrics.stringWidth(text);
	    Rectangle2D r2d = metrics.getStringBounds(text, g2d);
	    int textWidth = (int)r2d.getWidth();
	    int hgt = (int)r2d.getHeight() - 3; //3 is a fudge factor....

	    int usableHorizontalWidth = width - horizontalPadding*2 - rightVerticalBorderWidth - leftVerticalBorderWidth;
	    int usableVerticalHeight = height - verticalPadding*2 - horizontalBorderHeight*2;

	    System.out.println("usableVerticalHeight:"+usableVerticalHeight);
	    System.out.println("hgt:"+hgt);

	    final int FUDGE_FACTOR = 2;
		g2d.drawString(text,
			           horizontalPadding+leftVerticalBorderWidth+(usableHorizontalWidth-textWidth)/2+FUDGE_FACTOR,
			           (usableVerticalHeight+verticalPadding+horizontalBorderHeight)-(usableVerticalHeight-hgt)/2-FUDGE_FACTOR);

	}

	private static void drawBackground(Graphics2D g2d) {
		g2d.setColor(backgroundColor);
		g2d.fillRect(0,0,width, height);
	}

	private static void drawBorders(Graphics2D g2d) throws Exception {

		int horizontalBorderWidth = width - leftVerticalBorderWidth - rightVerticalBorderWidth - 2*horizontalPadding;

		g2d.setColor(borderColor);
		g2d.fillRect(leftVerticalBorderWidth+horizontalPadding,
				     verticalPadding,
				     horizontalBorderWidth,
				     horizontalBorderHeight);

		g2d.fillRect(leftVerticalBorderWidth+horizontalPadding,
			         height-verticalPadding-horizontalBorderHeight,
			         horizontalBorderWidth,
			         horizontalBorderHeight);

		InputStream is = ButtonGenerator.class.getClassLoader().getResourceAsStream("leftborder.png");
		BufferedImage leftBorderImage = ImageIO.read(is);
		g2d.drawImage(leftBorderImage, null, horizontalPadding,verticalPadding);
		is.close();

		is = ButtonGenerator.class.getClassLoader().getResourceAsStream("rightborder.png");
		BufferedImage rightBorderImage = ImageIO.read(is);
		g2d.drawImage(rightBorderImage,
				     null,
				     width-rightVerticalBorderWidth-horizontalPadding,
				     verticalPadding);
		is.close();
	}

	static int width = 125;//159;
	static int height = 27;
	static int horizontalPadding = 1;
	static int verticalPadding = 3;
	static int leftVerticalBorderWidth = 2;
	static int rightVerticalBorderWidth = 3;
	static int horizontalBorderHeight=2;

	static Color borderColor = new Color(110,129,166,240);

	static Color backgroundColor = new Color(255,255,255,241);

	//797383
	//static Color textColor = new Color(79,73,83);
	static Color textColor = new Color(121,115,131,239);

	static String text = "ANNOTATION";
}