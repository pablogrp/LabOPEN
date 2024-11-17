package org.fogbeam.example.opennlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;

/**
 * @class DocumentClassifierMain
 * @brief Main class for classifying documents using OpenNLP.
 */
public class DocumentClassifierMain {

	/**
	 * @brief Main method to run the document classifier.
	 * @param args Command line arguments.
	 * @throws Exception if an error occurs during document classification.
	 */
	public static void main(String[] args) throws Exception {
		InputStream is = null;
		try {
			is = new FileInputStream("models/en-doccat.model");

			DoccatModel model = new DoccatModel(is);
			String inputText = "What happens if we have declining bottom-line revenue?";
			DocumentCategorizerME categorizer = new DocumentCategorizerME(model);
			double[] outcomes = categorizer.categorize(inputText);
			String category = categorizer.getBestCategory(outcomes);

			System.out.println("Input classified as: " + category);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				is.close();
			}
		}
		System.out.println("done");
	}
}