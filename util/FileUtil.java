/**
 * 
 */
package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author xiang
 * 
 */
public class FileUtil {

	public static void writetofile(String filename, String content) {
		try {
			FileWriter fstream = new FileWriter(filename, true);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(content);
			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

	}

	public static void rewritetofile(String filename, String content) {
		try {
			FileWriter fstream = new FileWriter(filename, false);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(content);
			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

	}

	public static String readfromFile(String filename) {
		StringBuilder filecontent = new StringBuilder();
		// Open the file that is the first
		// command line parameter
		FileInputStream fstream;
		try {
			fstream = new FileInputStream(filename);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				// Print the content on the console
				filecontent.append(strLine + "\n");
			}
			// Close the input stream
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Get the object of DataInputStream
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filecontent.toString();

	}

	public static String printLines(String name, InputStream ins)
			throws Exception {
		StringBuilder result = new StringBuilder();
		String line = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(ins));
		while ((line = in.readLine()) != null) {
			result.append("\t" + name + " " + line);
			result.append("\n");
		}
		return result.toString();
	}

}