package org.fogbeam.example.opennlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

/**
 * Clase PartOfSpeechTaggerMain
 */
public class PartOfSpeechTaggerMain {

	private static final Logger LOGGER = Logger.getLogger(PartOfSpeechTaggerMain.class.getName());

	/**
	 * Método main
	 *
	 * @param args Argumentos de línea de comandos
	 */
	public static void main(String[] args) {
		try (InputStream modelIn = new FileInputStream("models/en-pos-maxent.bin")) {
			POSModel model = new POSModel(modelIn);
			POSTaggerME tagger = new POSTaggerME(model);

			String[] sent = new String[] { "Most", "large", "cities", "in", "the", "US", "had", "morning", "and",
					"afternoon", "newspapers", "." };
			String[] tags = tagger.tag(sent);
			double[] probs = tagger.probs();

			for (int i = 0; i < sent.length; i++) {
				System.out.println("Token [" + sent[i] + "] has POS [" + tags[i] + "] with probability = " + probs[i]);
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error loading the POS model", e);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "An unexpected error occurred", e);
		}
		System.out.println("done");
	}
}
