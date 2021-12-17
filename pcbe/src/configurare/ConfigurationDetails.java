package configurare;

import java.util.ArrayList;
import java.util.Map;

/**
 * 
 * @author diana
 *{@summary Clasa ajutatoare in parsare JSON file} 
 */
public class ConfigurationDetails {
	private  int numarBirouri;
	private int numarGhisee[];
	private Map<String, ArrayList<String>> documente; 
	
	public int getNumarBirouri() {
		return numarBirouri;
	}
	
	public int[] getNumarGhisee() {
		return numarGhisee;
	}

	public Map<String, ArrayList<String>> getTrasee() {
		return documente;
	}
}