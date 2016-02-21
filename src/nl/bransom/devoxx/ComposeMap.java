package nl.bransom.devoxx;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ComposeMap extends DevoxxMap {

	public static void main(String[] args) throws Exception {
		new ComposeMap().doIt();
	}

	private void doIt() throws Exception {
		ComposeMap cm = new ComposeMap();
		for (int z = 0; z < MAX_ZOOM; z++) {
			List<String> imageFiles = new ArrayList<>();
			for (int y = getMinY(z); y < getMaxY(z); y++) {
				for (int x = getMinX(z); x < getMaxX(z); x++) {
					imageFiles.add(composeFileName(x, y, z));
				}
			}
			cm.compose(z, imageFiles, DEVOXX_DIRECTORY + "map[" + z + "]." + IMAGE_EXT);
		}
	}

	public void compose(int z, List<String> imageFilenames, String composedImageFile) throws IOException {
		BufferedImage firstBi = ImageIO.read(new File(imageFilenames.get(0)));
		int composedWidth = (getMaxX(z) - getMinX(z)) * firstBi.getWidth();
		int composedHeight = (getMaxY(z) - getMinY(z)) * firstBi.getHeight();

		BufferedImage composedImage = new BufferedImage(composedWidth, composedHeight, BufferedImage.TYPE_INT_RGB);
		Graphics g = composedImage.getGraphics();
		int offsetX = 0;
		int offsetY = 0;
		for (String imageFilename : imageFilenames) {
			BufferedImage bi = ImageIO.read(new File(imageFilename));
			g.drawImage(bi, offsetX, offsetY, null);
			offsetX += bi.getWidth();
			if (offsetX >= composedWidth) {
				offsetX = 0;
				offsetY += bi.getHeight();
			}
		}

		ImageIO.write(composedImage, IMAGE_EXT, new File(composedImageFile));
		System.out.println(composedImageFile);
	}
}
