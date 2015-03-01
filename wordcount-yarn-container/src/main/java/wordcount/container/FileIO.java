package wordcount.container;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileIO {

	// Read the text file
	public static String read(String filename) throws IOException {

		String in = new String(Files.readAllBytes(Paths.get(filename)));
		return in;

	}

	public static void write(String out, String filename)
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(filename, "UTF-8");
		writer.print(out);
		writer.close();
	}

}
