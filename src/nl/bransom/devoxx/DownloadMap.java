package nl.bransom.devoxx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DownloadMap extends DevoxxMap {

	public static void main(String[] args) throws Exception {
		long startmillis = System.currentTimeMillis();
		{
			new DownloadMap().doIt();
		}
		System.out.println("duration: " + (System.currentTimeMillis() - startmillis) + "ms");
	}

	private static final int THREAD_POOL_SIZE = 8;

	private void doIt() throws Exception {
		ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		try {
			for (int z = 0; z < MAX_ZOOM; z++) {
				for (int y = getMinY(z); y < getMaxY(z); y++) {
					for (int x = getMinX(z); x < getMaxX(z); x++) {
						String imageUrl = composeURL(x, y, z);
						String filename = composeFileName(x, y, z);
						threadPool.execute(new DownloadFileTask(imageUrl, filename));
					}
				}
			}
		} finally {
			threadPool.shutdown();
		}
		threadPool.awaitTermination(60, TimeUnit.SECONDS);
	}

	private class DownloadFileTask implements Runnable {

		private String imageUrl;
		private String filename;

		public DownloadFileTask(String imageUrl, String filename) {
			this.imageUrl = imageUrl;
			this.filename = filename;
		}

		@Override
		public void run() {
			try {
				downloadFile(imageUrl, filename);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	protected void downloadFile(String imageUrl, String filename) throws IOException {
		new File(filename).getParentFile().mkdirs();
		URL url = new URL(imageUrl);
		InputStream is = null;
		OutputStream os = null;
		try {
			is = url.openStream();
			os = new FileOutputStream(filename);
			byte[] b = new byte[1024];
			int numRead;
			while ((numRead = is.read(b)) > 0) {
				os.write(b, 0, numRead);
			}
		} finally {
			if (is != null) {
				is.close();
			}
			if (os != null) {
				os.close();
			}
		}
		System.out.println(filename);
	}
}
