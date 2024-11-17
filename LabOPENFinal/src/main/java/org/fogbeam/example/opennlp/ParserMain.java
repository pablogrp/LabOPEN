package org.fogbeam.example.opennlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;

/**
 * @class ParserMain
 * @brief Main class for parsing sentences using OpenNLP.
 */
public class ParserMain {

	private static final Logger LOGGER = Logger.getLogger(ParserMain.class.getName());

	/**
	 * @brief Main method to run the parser.
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {
		try (InputStream modelIn = new FileInputStream("models/en-parser-chunking.bin")) {
			ParserModel model = new ParserModel(modelIn);
			Parser parser = ParserFactory.create(model);
			String sentence = "The quick brown fox jumps over the lazy dog.";
			Parse[] topParses = ParserTool.parseLine(sentence, parser, 1);

			if (topParses.length > 0) {
				Parse parse = topParses[0];
				System.out.println(parse.toString());
				parse.showCodeTree();
			} else {
				LOGGER.warning("No parse trees were generated for the given sentence.");
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "An error occurred while loading the parser model", e);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "An unexpected error occurred", e);
		}
		System.out.println("done");
	}
}
