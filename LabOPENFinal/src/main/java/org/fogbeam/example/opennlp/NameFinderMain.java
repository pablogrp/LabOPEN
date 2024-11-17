package org.fogbeam.example.opennlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

/**
 * @class NameFinderMain
 * @brief Main class for finding names in text using OpenNLP.
 */
public class NameFinderMain {

	private static final Logger LOGGER = Logger.getLogger(NameFinderMain.class.getName());

	/**
	 * @brief Main method to run the name finder.
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {
		try (InputStream modelIn = new FileInputStream("models/en-ner-person.model")) {
			TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
			NameFinderME nameFinder = new NameFinderME(model);

			String[] tokens = {
					"Phillip", "Rhodes", "is", "presenting", "at", "some", "meeting", "."
			};

			Span[] names = nameFinder.find(tokens);

			for (Span ns : names) {
				System.out.println("ns: " + ns.toString());

				// if you want to actually do something with the name
				// ...
			}
			nameFinder.clearAdaptiveData();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "An error occurred while loading the name finder model", e);
		}
		System.out.println("done");
	}
}
