package gov.nih.nci.ncia.deletion.dao;

import java.util.List;

public interface AnnotationDAO {
	public List<String> deleteAnnotation(List<Integer> seriesIDs);
}
