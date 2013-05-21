package gov.nih.nci.nbia.deletion.dao;

import java.util.List;

public interface AnnotationDAO {
	public List<String> deleteAnnotation(List<Integer> seriesIDs);
}
