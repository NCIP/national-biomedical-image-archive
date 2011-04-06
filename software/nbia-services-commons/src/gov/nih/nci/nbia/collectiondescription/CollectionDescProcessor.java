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
