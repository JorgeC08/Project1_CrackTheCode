package police;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import criminals.Member;
import criminals.Organization;
import interfaces.List;
import interfaces.MemberLambda;
import lists.ArrayList;
import lists.DoublyLinkedList;

public class PoliceDepartment {
	
	private List<Organization> criminalOrganizationFiles = new ArrayList<>();
	private String captain;
	private int numberOfArrest = 0;
	private int theRoot = 0;
	public PoliceDepartment(String captain) {
		this.captain = captain;
	}

	public List<Organization> getCriminalOrganizations(){
		return this.criminalOrganizationFiles;
		
	}
	/**
	 * Lee los archivos de organizaciones criminales en la carpeta 
	 * "CriminalOrganizations" y crea objetos Organization para cada uno de ellos.
	 * Luego, agrega estos objetos a un ArrayList llamado criminalOrganizationFiles.
	 *
	 * @param caseFolder Ruta de la carpeta que contiene la carpeta "CriminalOrganizations"
	 * @throws IOException Si se produce un error al leer los archivos
	 */
	public void setUpOrganizations(String caseFolder) throws IOException {
		// Crea un archivo y lo inicializa con la ruta de la carpeta de CriminalOrganizations
		File folder = new File(caseFolder + "/CriminalOrganizations");

		// Para obtener un array de objetos de CriminalOrganizations
		File[] orgFiles = folder.listFiles();

		// Ordena el array alfabéticamente 
		Arrays.sort(orgFiles);

		// Para comprobar si no es nulo
		if(orgFiles != null) {

			for(File files : orgFiles) {
				// Obtiene la ruta de archivo del objeto File actual
				String filesPath = files.getPath();

				// Crea un nuevo objeto Organization utilizando la ruta de archivo obtenida y lo inicializa con la información del archivo
				Organization newOrg = new Organization(filesPath);

				// Agrega el objeto Organization recién creado al ArrayList criminalOrganizationFiles
				criminalOrganizationFiles.add(newOrg); 
			}
		}
	}
	
	/**
	 * Decodifica un mensaje cifrado que se encuentra en un archivo.
	 *
	 * @param caseFolder Ruta del archivo que contiene el mensaje cifrado.
	 * @return El nombre del líder de la organización criminal.
	 * @throws IOException Si ocurre un error al leer el archivo.
	 */
	public String decipherMessage(String caseFolder) throws IOException {
		// Abrir el archivo que contiene el mensaje cifrado
		BufferedReader br = new BufferedReader(new FileReader(caseFolder));

		// Obtener el digiroot de la primera línea para encontrar la organización criminal 
		int theKey = getDigiroot(br.readLine().substring(1));
		theKey--; // Restar 1 para obtener el índice correcto en la lista de organizaciones
		theKey = criminalOrganizationFiles.get(theKey).getLeaderKey() - 1;// Obtener el índice del líder de la organización
		theRoot = theKey;
		// Ignorar la segunda línea
		br.readLine();
		
		// Leer el resto del archivo para obtener el nombre del líder
		String linea;
		String leadersName = "";

		while (!(linea = br.readLine()).equals("--")) {

			// Dividiendo las lineas por las palabras
			String[] palabras = linea.split(" ");

			if (palabras.length > theKey) {
				leadersName += palabras[theKey].charAt(0);
			} else {
				leadersName += " ";
			}

		}
		// Cerrar el archivo y devolver el nombre del líder
		br.close();
		return leadersName;
	}

	/**
	 * Devuelve el Digiroot de un número dado, sumando entre su sus valores 
	 * hasta que solo quede UN digito
	 * 
	 * @param numbers un String que representa el número al cual se le quiere calcular el Digiroot
	 * @return the digiRoot del numero
	 */
	public int getDigiroot(String numbers) {
		
		
		int digiRoot = 0;
		 // Iteramos por cada dígito en el String, lo convertimos a Integer y lo sumamos al digiRoot 
		for(int i = 0; i < numbers.length(); i++) {
			char c = numbers.charAt(i);
			int temp = Integer.parseInt(Character.toString(c));
			digiRoot += temp;
		}
		// Si al sumar todos los dijitos todavia quedan mas de dos digitos se vuelve a sumar entre si
		while(digiRoot > 9) {
			int tempDigi = 0;
	        String digiComoString = Integer.toString(digiRoot);
	        // Iteramos por cada dígito del digiRoot, lo convertimos a Integer y lo sumamos a tempDigi
	        for(int i = 0; i < digiComoString.length(); i++) {
	        	char c = digiComoString.charAt(i);
	        	int temp = Integer.parseInt(Character.toString(c));
	        	tempDigi += temp;
	        }
	        // Volvemos a guardar el digiRoot solo si entramos al while
	        digiRoot = tempDigi;
		}
		// Devolvemos el digiRoot para ser utilizado
		return digiRoot;
	}
	/*
	 * Busca al lider de una organizacion criminal en una lista de organizaciones
	 * para arrestarlo. Tambien se arrestan a sus underlings. Se resuelve de manera 
	 * recursiva y devuelve una lista con los underlings arrestados y se traslada siguiendo 
	 * la jerarquia de orden.
	 * 
	 * @param leader  El que dirigue la organizacion
	 * 
	 * @return  la lista con los underlings arrestados y sus l 
	 */
	public void arrest(String leader) {
	    // Creamos una lista vacía para almacenar los underlings del líder arrestado
	    ArrayList<Member> underlingsOfArrestedLeader = new ArrayList<>();

	    // Buscamos al miembro que contiene al líder en todas las organizaciones
	    Member theLeader = null;
	    for (Organization org : criminalOrganizationFiles) {
	        // Utilizamos la función de búsqueda por nickname para encontrar al miembro que contiene al líder
	        List<Member> theList = org.organizationTraversal(M -> M.getNickname().equalsIgnoreCase(leader));
	        // Si encontramos al miembro, lo marcamos como arrestado y agregamos sus underlings a la lista de underlings del líder arrestado
	        if (!theList.isEmpty()) {
	            theLeader = theList.get(0);
	            theLeader.setArrested(true);

	            // Agregamos los underlings del líder a la lista de underlings del líder arrestado
	            List<Member> underlings = theLeader.getUnderlings();
	            for (int i = 0; i < underlings.size(); i++) {
	                underlingsOfArrestedLeader.add(underlings.get(i));
	            }
	            break;
	        }
	    }

	    // Si encontramos al líder, llamamos al método arrestHelper() con la lista de underlings del líder arrestado
	    if (theLeader != null) {
	        arrestHelper(underlingsOfArrestedLeader);
	    }
	}



	public void arrestHelper(ArrayList<Member> underlings) {
	    // Si la lista de underlings está vacía, terminamos la recursión
	    if (underlings.isEmpty()) return;

	    int maxUnderlings = 0;
	    int maxPos = 0;
	    int i = 0;

	    // Recorremos la lista de underlings para encontrar al underling con más underlings a su cargo
	    for (Member underling : underlings) {
	        // Marcamos al underling como arrestado si no lo está
	        if (!underling.isArrested()) 
	            underling.setArrested(true);
	        
	        numberOfArrest++;
	        
	        // Comparamos la cantidad de underlings del underling actual con el máximo
	        int underlingsListSize = underling.getUnderlings().size();
	        if (maxUnderlings < underlingsListSize) {
	            maxUnderlings = underlingsListSize;
	            maxPos = i;
	        }

	        i++;
	    }

	    // Obtenemos al underling con más underlings y lo eliminamos de la lista de underlings
	    Member maxUnderling = underlings.get(maxPos);
	    underlings.remove(maxPos);

	    // Agregamos los underlings del underling con más subordinados a la lista de underlings
	    
	    for (int j = maxUnderling.getUnderlings().size() - 1; j >= 0; j--) {
	        underlings.add(0, maxUnderling.getUnderlings().get(j));
	    }

	    // Llamamos al método arrestHelper con la lista actualizada de underlings
	    arrestHelper(underlings);
	}

	/**
	 * Crea y escribe un reporteo que describe el estado actual de las organizaciones 
	 * criminales y los arrestos realizados.
	 * 
	 * @param filePath		 la ruta del archivo en la que se escribirá el informe
	 * @throws IOException 		si hay un error de entrada / salida al escribir el archivo
	 */
	
	public void policeReport(String filePath) throws IOException {
	    // Crea un objeto File con la ruta de archivo especificada
		File report = new File(filePath);
	    // Crea un objeto BufferedWriter y FileWriter para escribir en el archivo
		BufferedWriter writeReport = new BufferedWriter(new FileWriter(report));
		// Escribe la informacion 
		writeReport.write("CASE REPORT\n" + "\n" + "In charge of Operation: " + this.captain + "\n");
		writeReport.newLine();
		writeReport.write("Total arrest made: " + Integer.toString(this.numberOfArrest) + "\n");
		writeReport.newLine();
		writeReport.write("Current Status of Criminal Organizations:\n");
		writeReport.newLine();
		
		// Cicla por todas las organizaciones criminales y escribe su estado actual 
		for(Organization org: getCriminalOrganizations()) {
			// Si el jefe de la organizacion esta arresatdo se considera disuelta
			if(org.getBoss().isArrested() == true) {
				writeReport.write("DISSOLVED\n");
			}
			writeReport.write(org.toString());
			writeReport.write("--- \n");
		}
		writeReport.write("END OF REPORT \n");
		writeReport.close(); // Se cierra el objeto BufferedWritter
		
	}
}
