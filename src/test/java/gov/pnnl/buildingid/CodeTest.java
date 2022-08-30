package gov.pnnl.buildingid;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class CodeTest extends TestCase {
	public CodeTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(CodeTest.class);
	}

	public void testCode() {
		assertEquals("849VQJH6+95J-0-0-0-0", Code.encode(37.7784744216144, -122.3896056846471, 11).getValue());
		
		assertEquals("849VQJH6+95J-51-58-42-50", Code.encode(37.777404202489, -122.391163268176, 37.779726076137, -122.387805614265, 37.7784744216144, -122.3896056846471, 11).getValue());
	}
}
