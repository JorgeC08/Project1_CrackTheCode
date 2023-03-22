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

	public void setUpOrganizations(String caseFolder) throws IOException {		
		
		// Creamos un folder que esta dirigido a las organizaciones criminales
		File folder = new File(caseFolder + "/CriminalOrganizations");
		
		File[] orgFiles = folder.listFiles();
		Arrays.sort(orgFiles);
		
		if(orgFiles != null) {
				
			for(File files : orgFiles) {
				String filesPath = files.getPath();
				Organization newOrg = new Organization(filesPath);
				criminalOrganizationFiles.add(newOrg);	 	
				}
		}
	}

	public String decipherMessage(String caseFolder) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(caseFolder));

		// Guardamos la primera linea para obtener el digiRoot
		int theKey = getDigiroot(br.readLine().substring(1));
		theKey--;
		theKey = criminalOrganizationFiles.get(theKey).getLeaderKey() - 1;
		theRoot = theKey;
		// Leemos la segunda linea sin guardarla para ignorarla
		br.readLine();
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
		br.close();
		return leadersName;
	}

	public int getDigiroot(String numbers) {
		
		
		int digiRoot = 0;
		// Cogemos la primera linea String, la pasamos a Integer usando parseInt y la sumamos digito a digito 
		for(int i = 0; i < numbers.length(); i++) {
			char c = numbers.charAt(i);
			int temp = Integer.parseInt(Character.toString(c));
			digiRoot += temp;
		}
		// Si al sumar todos los dijitos todavia quedan mas de dos digitos se vuelve a sumar entre si
		while(digiRoot > 9) {
			int tempDigi = 0;
	        String digiComoString = Integer.toString(digiRoot);
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
	
	public void arrest(String leader) {
	    // Creamos una lista vacía de underlings del líder y una lista para guardar el miembro que contiene al líder
	    ArrayList<Member> underOfLeader = new ArrayList<>();
	    List<Member> listContainingLeader;

	    // Recorremos las organizaciones para encontrar el miembro que contiene al líder
	    for (Organization organization : criminalOrganizationFiles) {
	        // Utilizamos la función de búsqueda por nickname para encontrar el miembro que contiene al líder
	        listContainingLeader = organization.organizationTraversal(M -> M.getNickname().toLowerCase().equals(leader.toLowerCase()));

	        // Si encontramos al miembro, marcamos al líder como arrestado y agregamos sus underlings a la lista de underlings del líder
	        if (!listContainingLeader.isEmpty()) {
	            Member leaderMember = listContainingLeader.get(0);
	            leaderMember.setArrested(true);

	            // Agregamos los underlings del líder a la lista 
	            for (int i = leaderMember.getUnderlings().size() - 1; i >= 0; i--) {
	                underOfLeader.add(0, leaderMember.getUnderlings().get(i));
	            }

	            // Llamamos al método arrestHelper con la lista de underlings del líder
	            arrestHelper(underOfLeader);
	            break;
	        }
	    }
	}

	public void arrestHelper(ArrayList<Member> underlings) {
	    // Si la lista de underlings está vacía, terminamos la recursión
	    if (underlings.isEmpty()) 
	        return;

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
	        int underlingsSize = underling.getUnderlings().size();
	        if (underlingsSize > maxUnderlings) {
	            maxUnderlings = underlingsSize;
	            maxPos = i;
	        }

	        i++;
	    }

	    // Obtenemos al underling con más underlings y lo eliminamos de la lista de underlings
	    Member maxUnderling = underlings.get(maxPos);
	    underlings.remove(maxPos);

	    // Agregamos los underlings del undering con más subordinados a la lista de underlings
	    for (int j = maxUnderling.getUnderlings().size() - 1; j >= 0; j--) {
	        underlings.add(0, maxUnderling.getUnderlings().get(j));
	    }

	    // Llamamos al método arrestHelper con la lista actualizada de underlings
	    arrestHelper(underlings);
	}


	
	public void policeReport(String filePath) throws IOException {
		
		File report = new File(filePath);
		
		BufferedWriter writeReport = new BufferedWriter(new FileWriter(report));
		
		writeReport.write("CASE REPORT\n" + "\n" + "In charge of Operation: " + this.captain + "\n");
		writeReport.newLine();
		writeReport.write("Total arrest made: " + Integer.toString(this.numberOfArrest) + "\n");
		writeReport.newLine();
		writeReport.write("Current Status of Criminal Organizations:\n");
		writeReport.newLine();
		
		for(Organization org: getCriminalOrganizations()) {
			if(org.getBoss().isArrested() == true) {
				writeReport.write("DISSOLVED\n");
			}
			writeReport.write(org.toString());
			writeReport.write("--- \n");
		}
		writeReport.write("END OF REPORT \n");
		writeReport.close();
		
	}
}
