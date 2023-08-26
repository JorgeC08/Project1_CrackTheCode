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
        // Crear instancia de departamento de policía con un capitán asignado
		PoliceDepartment pd = new PoliceDepartment("Captain Morgan"); 
        // Configurar las organizaciones criminales con un archivo de entrada
		File flyersFolder = new File("inputFiles/case2/Flyers"); 

		pd.setUpOrganizations("inputFiles/case2");
		
		for(File code : flyersFolder.listFiles()) {
			String leaders = pd.decipherMessage(code.getPath());
			pd.arrest(leaders);
			
		}
		pd.policeReport("results/Report5.txt");
	
	}
	
}
