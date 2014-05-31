package config;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import database.Mundo;

//import java.util.Properties;

public class Mundo_Reader {

	// Lista dos mundos
	static List<Mundo> mundoList = new ArrayList<Mundo>();
	
	// Mundo Selecionado
	public static Mundo MundoSelecionado;

	public static void read(String section) {

		try {

			// read the user-alterable config file
			Scanner in = new Scanner(new StringReader(section));
			
			store(in);

			// in case the file is corrupt, for any reason (thus we generalize
			// the exception), we use
			// the default file
		} catch (IOException e) {
			System.out.println("bugou geral");
		}

	}

	/**
	 * Stores the read information to the worldList
	 * 
	 * @param in
	 * @throws IOException
	 */
	private static void store(Scanner in) throws IOException {

		String total = "";

		// reads the lines to gather all the properties of each world, running
		// once per world
		// breaks once there are no more worlds to read
		while (in.hasNextLine()) {

			String s;
			total += in.nextLine()+"\n";

			// reads the lines to gather all of the properties, breaking once
			// the line contains no more properties (i.e. the world will change)
			while (in.hasNextLine()) {
				
				s = in.nextLine().trim();
				if (!s.equals(""))
					total += s + "\n";
				else 
					break;
			}

			Properties i = new Properties();
			i.load(new StringReader(total));

			Mundo mundo = new Mundo(i);
			mundoList.add(mundo);
		}

		in.close();

	}

	/**
	 * Saves the configurations into a string
	 */
	public static String getMundosConfig() {

		String section = "";

		for (Mundo i : mundoList)
			section += i.getConfigText();

		return section;

	}

	public static void setMundoSelecionado(Mundo mundo) {

		MundoSelecionado = mundo;

		MundoSelecionado.setTemposDeProdu��o();

		MundoSelecionado.setUnidadeList();

	}

	public static Mundo getMundo(int index) {
		return mundoList.get(index);
	}

	public static List<Mundo> getMundoList() {
		return mundoList;
	}

}