package nci.nih.gov;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.util.TagUtils;

public class FindDicomFiles {

    public Boolean listHeader(DicomObject object, String tagGE, Object valueToFind) {
    	   Boolean found =false;
    	   String tagValue="";
           Iterator iter = object.datasetIterator();
           tagGE = "("+tagGE+")";
           while(iter.hasNext()) {
              DicomElement element = (DicomElement) iter.next();
              int tag = element.tag();
              try {

                 String tagAddr = TagUtils.toString(tag);

                 tagValue = object.getString(tag);

                 if(tagValue!=null){
                	 String valueToFinding = (String) valueToFind;

                	 if(tagAddr.equals(tagGE) && tagValue.equals(valueToFinding)){
                     found=true;
                	 }
                 	}
              }catch (Exception e) {
            	 // e.;
            	  	continue;
 		      }

           }
           return found;
        }

    public Boolean listHeader(DicomObject object, String tagGE) {
 	   Boolean found =false;
        Iterator iter = object.datasetIterator();
        //Have to append brackets before and after Group and Element
        //Since this is how the column is saved in database.
        tagGE = "("+tagGE+")";
        while(iter.hasNext()) {
           DicomElement element = (DicomElement) iter.next();
           int tag = element.tag();
           try {

              String tagAddr = TagUtils.toString(tag);
              //Compares the user entered Group, Element to the DICOM tag Group, Element
              //This function can also be called this way. if(tagAddr.contains(tagGE))
              if(tagAddr.equals(tagGE)){
                  found=true;
              }
           }catch (Exception e) {
        	   //Throws exception and print StackTrace.
        	   // e.printStackTrace();
        	   System.out.println(object.toString() +" is not a DICOM file.");
		      }

        }
        return found;
     }

        public static void main(String[] args) throws IOException {
               DicomObject object = null;
               Boolean BooleanAnswer=false;
               Boolean BooleanFound=false;
               BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
               //Take three inputs from User to search.
               System.out.print("Enter the Directory where you want to Search: ");
               //Eg: C:/Users/user/Desktop/9/
               String directoryToSearch = reader.readLine();
               while(directoryToSearch.length()==0){
            	   System.out.print("Enter the Directory where you want to Search: ");
            	   directoryToSearch = reader.readLine();
               }

               System.out.print("Enter the Group and Element seperated by a ',' : ");
               String GEtoSearch = reader.readLine();
               while(GEtoSearch.length()==0){
            	   System.out.print("Enter the Group and Element seperated by a ',' : ");
            	   GEtoSearch = reader.readLine();
               }

               System.out.print("Enter the Value for the Dicom Tag: ");
               String valueToSearch = reader.readLine();
               if(valueToSearch.length()==0){
            	   System.out.print("\t\t\tNo Value Supplied for Dicom Tag\n");
               }
               System.out.println("\n");
               //Function to look for the file specified.
               //This method search thru folders and sub-folders.
               File directory = new File(directoryToSearch);
			   List<File> fList = (List<File>) FileUtils.listFiles(directory, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
			   //Look for every file in the folders and sub-folders
               for (File file : fList){
                   if (file.isFile()){
                                   try {
						                  DicomInputStream dis = new DicomInputStream(new File(file.getCanonicalPath()));
						                  object = dis.readDicomObject();
						                  dis.close();
				               } catch (Exception e) {
				            	   //Displays the non-Dicom file location and continues to other files.
				            	   System.out.println(file.toString() +" is not a DICOM file");
				            	   continue;

				               }

                                   FindDicomFiles list = new FindDicomFiles();

				               if(valueToSearch.length()==0){
				            	   BooleanAnswer = list.listHeader(object,GEtoSearch);
				               }
				               else{BooleanAnswer = list.listHeader(object,GEtoSearch,valueToSearch);}

				               if (BooleanAnswer == true){
				            	   BooleanFound=true;
				            	   System.out.println("Search Criteria Matched at: "+ file.getCanonicalPath());
				               }
                   }
                   else{System.out.println("Not a file");}
               }
               if(BooleanFound==false)
            	   System.out.println("Couldn't Find Any Mathces. Please try again.");
            }
}