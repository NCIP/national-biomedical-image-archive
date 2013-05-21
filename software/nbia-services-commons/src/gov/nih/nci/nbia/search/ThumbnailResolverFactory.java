package gov.nih.nci.nbia.search;

public class ThumbnailResolverFactory {

	
	public static ThumbnailURLResolver getThumbnailURLResolver() {
		if(instance==null) {
			instance = createThumbnailURLResolver();
		}
		return instance;
	}
	
	/////////////////////////////////////PRIVATE////////////////////////////////////
	
	private static ThumbnailURLResolver instance;
	
	
	private static ThumbnailURLResolver createThumbnailURLResolver() {
		String thumbnailResolverClassName = System.getProperty("thumbnailResolver.className");

		if(thumbnailResolverClassName==null) {
			throw new RuntimeException("thumbnailResolver.className must be defined in system properties");
		}
		else {
			try {
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				Class clazz = Class.forName(thumbnailResolverClassName, false, loader);
				ThumbnailURLResolver thumbnailURLResolver =  (ThumbnailURLResolver)clazz.newInstance();
				return thumbnailURLResolver;
			}
			catch(Exception ex) {
				throw new RuntimeException(ex);
			}
		}			
	}	
}
