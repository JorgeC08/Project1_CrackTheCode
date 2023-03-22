package police;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import police.PoliceDepartment;
/**
 * Class whose main method will follow the steps needed for arresting 
 * members of criminal organizations. The step should be followed as established in the 
 * project's document.
 * @author Gretchen
 *
 */
public class ArrestOperation {

	public static void main(String[] args) throws IOException {
		
		PoliceDepartment pd = new PoliceDepartment("Captain Morgan");
		// Case 1  
		pd.setUpOrganizations("inputFiles/case1");
		String leader = pd.decipherMessage("inputFiles/case1/Flyers/Message1");
		pd.arrest(leader);
		pd.policeReport("results/Report.txt");
	}
}
