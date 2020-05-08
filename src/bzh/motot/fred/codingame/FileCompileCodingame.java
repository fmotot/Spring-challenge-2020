package bzh.motot.fred.codingame;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileCompileCodingame {
	private static String SEPARATOR = "//#################################################################################################";

	private static ArrayList<File> list;

	public static void main(String[] args) {
		list = new ArrayList<File>();

		
		try {
			File dir = new File(".\\src");

			getListFile(dir);

			FileReader fr;
			String line;
			BufferedReader br;
			
			String desktop = System.getProperty("user.home").replace("\\", "/") + "/Desktop/";
			FileWriter fw = new FileWriter(desktop + "codingame.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			
			File temp = null;
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getName().equals("Player.java") && i != 0) {
					temp = list.get(i);
					list.set(i, list.get(0));
					list.set(0, temp);
				}
			}

			System.out.println(list);
			for (File f : list) {
				line = "";
				fr = new FileReader(f);
				br = new BufferedReader(fr);
				boolean start = false;
				String[] split = null;

				if (f.getName().equals("Player.java")) {
					// s'il s'agit du fichier Player.java, passer les 10 premières lignes
					for (int i = 0; i < 10; i++) {
						br.readLine();
					}
				} else {
					
					// permet de ne commencer à prendre le fichier que depuis class ou enum en supprimant le public
					while (!start) {
						split = br.readLine().split(" ");
						for (String s : split) {
							if (s.contains("class") || s.contains("enum")) {
								start = true;
							}
						}
					}

					for (int i = 1; i < split.length; i++) {
						line += split[i] + " ";
					}
					bw.write(line + "\n");
				}
				
				while ((line = br.readLine()) != null) {
					bw.write(line + "\n");
				}
				br.close();

				bw.write("\n");
				for (int i = 0; i < 6; i++) {
					bw.write(SEPARATOR + "\n");
				}
				bw.write("\n");

				bw.flush();

			}

			bw.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void getListFile(File dir) {

		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				getListFile(file);
			} else {
				if (!file.getName().contentEquals("FileCompileCodingame.java")
						&& !file.getName().contentEquals("Launcher.java")) {
					list.add(file);
				}
			}
		}
	}
}
