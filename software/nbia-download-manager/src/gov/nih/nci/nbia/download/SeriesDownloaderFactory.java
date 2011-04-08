package gov.nih.nci.nbia.download;


public abstract class SeriesDownloaderFactory {

	public static AbstractSeriesDownloader createSeriesDownloader(boolean local) {
		String seriesDownloaderClassName = null;
		if(local) {
			seriesDownloaderClassName = System.getProperty("localSeriesDownloader.className");
		}
		else {
			seriesDownloaderClassName = System.getProperty("remoteSeriesDownloader.className");
		}

		if(seriesDownloaderClassName==null) {
			throw new RuntimeException("local or remote seriesDownloader.className must be defined in system properties");
		}
		else {
			try {
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				Class clazz = Class.forName(seriesDownloaderClassName, false, loader);
				AbstractSeriesDownloader abstractSeriesDownloader =  (AbstractSeriesDownloader)clazz.newInstance();
				return abstractSeriesDownloader;
			}
			catch(Exception ex) {
				throw new RuntimeException(ex);
			}
		}			
	}
}