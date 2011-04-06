package gov.nih.nci.nbia.zip;

/**
 * Gets an instance of a file zipper 
 */
public class FileZipperFactory {

    /**
     * Determines the type of zipper to use and returns it
     *
     * @throws Exception
     */
    public static AbstractFileZipper getInstance() {
        return new ZipFiles();
    }
}
