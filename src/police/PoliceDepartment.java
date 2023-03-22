package police;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import criminals.Organization;
import interfaces.List;
import interfaces.MemberLambda;
import lists.ArrayList;
import lists.DoublyLinkedList;

public class PoliceDepartment {
	
	private List<Organization> criminalOrganizationFiles = new ArrayList<>();
	private String captain;
	public PoliceDepartment(String captain) {
		this.captain = captain;
	}

	public List<Organization> getCriminalOrganizations(){
		return this.criminalOrganizationFiles;
		
	}

	public void setUpOrganizations(String caseFolder) throws IOException {		
		
		File folder = new File(caseFolder + "/CriminalOrganizations");
		
		File[] orgFiles = folder.listFiles();
		
		if(orgFiles != null) {
			Arrays.sort(orgFiles);
			
			for(File files : orgFiles) {
				Organization newOrg = new Organization(caseFolder + "/CriminalOrganizations" + files);
				criminalOrganizationFiles.add(newOrg);	 	
				}
		}
	}
	

	public String decipherMessage(String caseFolder) throws IOException {
		
		Organization organization = new Organization(caseFolder, 5);
		BufferedReader br = new BufferedReader(new FileReader(caseFolder));
		
		// Guardamos la primera linea para obtener el digiRoot
		int theDigiRoot = getDigiroot(br.readLine()); 

		// Ignoramos la segunda linea ya que no la usaremos
		br.readLine(); 

		int theKey = organization.getLeaderKey();
		String linea;
		String leadersName = "";
		
		
		while ((linea = br.readLine()) != null) {

			// Salir del loop cuando lleguemos al final del mensaje
			if (linea.equals("--"))
				break; 
				
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
			writeReport.write("--");
		}
		writeReport.write("END OF REPORT");
		writeReport.close();
		
	}
}
