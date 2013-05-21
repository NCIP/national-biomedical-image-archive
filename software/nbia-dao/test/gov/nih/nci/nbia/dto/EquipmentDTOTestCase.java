package gov.nih.nci.nbia.dto;
import junit.framework.TestCase;

public class EquipmentDTOTestCase extends TestCase {

	public void testAccessors() {
		EquipmentDTO dto  = new EquipmentDTO("foo1", "foo2", "foo3");
		assertEquals(dto.getManufacturer(),"foo1");
		assertEquals(dto.getModel(),"foo2");
		assertEquals(dto.getVersion(),"foo3");
	}
}
