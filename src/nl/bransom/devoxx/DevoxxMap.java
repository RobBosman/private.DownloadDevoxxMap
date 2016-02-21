package nl.bransom.devoxx;

public abstract class DevoxxMap {

	public static final String IMAGE_URL_BASE = "http://d3n8nshrz017vk.cloudfront.net/island-map/";
	public static final String IMAGE_EXT = "png";
	public static final String DEVOXX_DIRECTORY = "./Devoxx_map_2011/";
	public static final int[][] ZOOM_OFFSETS = { { 0, 0, 1, 1 }, { 0, 0, 2, 2 }, { 0, 0, 4, 4 }, { 0, 0, 8, 8 },
			{ 4, 4, 13, 13 }, { 8, 9, 26, 25 }, { 17, 19, 51, 50 } };
	public static final int MAX_ZOOM = ZOOM_OFFSETS.length;

	protected int getMinX(int z) {
		return ZOOM_OFFSETS[z][0];
	}

	protected int getMinY(int z) {
		return ZOOM_OFFSETS[z][1];
	}

	protected int getMaxX(int z) {
		return ZOOM_OFFSETS[z][2];
	}

	protected int getMaxY(int z) {
		return ZOOM_OFFSETS[z][3];
	}

	protected String composeURL(int x, int y, int z) {
		return IMAGE_URL_BASE + "tile-z" + z + "-x" + x + "-y" + y + "." + IMAGE_EXT;
	}

	protected String composeFileName(int x, int y, int z) {
		return DEVOXX_DIRECTORY + "map[" + z + "]/Devoxx2011-map[" + z + "]-(" + x + "," + y + ")." + IMAGE_EXT;
	}
}
