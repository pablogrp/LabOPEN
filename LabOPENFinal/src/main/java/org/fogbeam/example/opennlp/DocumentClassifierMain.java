package org.fogbeam.example.opennlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;

/**
 * @class DocumentClassifierMain
 * @brief Main class for classifying documents using OpenNLP.
 */
public class DocumentClassifierMain {

	private static final Logger LOGGER = Logger.getLogger(DocumentClassifierMain.class.getName());

	/**
	 * @brief Main method to run the document classifier.
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {
		try (InputStream is = new FileInputStream("models/en-doccat.model")) {

			DoccatModel model = new DoccatModel(is);
			String inputText = "What happens if we have declining bottom-line revenue?";
			DocumentCategorizerME categorizer = new DocumentCategorizerME(model);
			double[] outcomes = categorizer.categorize(inputText);
			String category = categorizer.getBestCategory(outcomes);

			System.out.println("Input classified as: " + category);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "An error occurred while loading the document categorizer model", e);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "An error occurred during document classification", e);
		}
		System.out.println("done");
	}
}
