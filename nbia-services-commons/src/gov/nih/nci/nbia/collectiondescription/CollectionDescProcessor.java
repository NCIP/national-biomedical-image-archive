/**
 * 
 */
package gov.nih.nci.nbia.collectiondescription;

import gov.nih.nci.nbia.dao.CollectionDescDAO;
import gov.nih.nci.nbia.dto.CollectionDescDTO;
import gov.nih.nci.nbia.util.SpringApplicationContext;

import java.util.List;

/**
 * process request send from the UI
 * @author lethai
 *
 * @todo This object is apparently a useless pass through for the DAO.
 * maybe look to see if presentation is doing something extra, or DAO is doing
 * something extra that should go here.  "Processor" is usually a clue that
 * the responsibilities for the object are ill-defined....
 */
public class CollectionDescProcessor {
	private CollectionDescDAO collectionDescDAO;
	public CollectionDescProcessor(){
		collectionDescDAO = (CollectionDescDAO)SpringApplicationContext.getBean("collectionDescDAO");
	}
	
	public List<String> getCollectionNames(){
		return collectionDescDAO.findCollectionNames();		
	}
	
	public CollectionDescDTO getCollectionDescByCollectionName(String collectionName){
		CollectionDescDTO collectionDescDTO =  collectionDescDAO.findCollectionDescByCollectionName(collectionName);
		//System.out.println("getCollectionDescByCollectionName: pkId = " + collectionDescDTO.getId() + " collectionName = " + collectionDescDTO.getCollectionName());
		return collectionDescDTO;
	}
	
	public long update(CollectionDescDTO dto){
		//System.out.println("Processor: CollectionName: " + dto.getCollectionName() + " \t description: " + dto.getDescription());
		return collectionDescDAO.save(dto);
	}
}
