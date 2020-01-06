package qa;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.TemporaryFolder;

public class AllTestCase {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();
	
	@Test
	public void testInputFileNoEntry() throws IOException{
		
		final File tempFile = tempFolder.newFile("tempFile.txt");
		Assert.assertEquals(tempFile.length(), 0);
	}
	
	public void readFileRelativePath() throws IOException {
        File file = new File("src/test/resources/files/file1.txt");
        assertTrue(file.exists());
    }
	
	@Test
	public void testInputFileInvalidFile() throws IOException{
		File file = new File("src/test/resources/input.txt");
        assertTrue(file.exists());
	}
	
	@Test
	public void testRelativeFileNoData() throws IOException{
		final File tempFile = tempFolder.newFile("src/test/resources/files/file2.txt");
		Assert.assertEquals(tempFile.length(), 0);
	}
	
	@Test
	public void testInputFileWithData() throws IOException {
		final File tempFile = tempFolder.newFile("src/test/resources/input.txt");
		Assert.assertTrue(tempFile.length()> 0);
	}

}
