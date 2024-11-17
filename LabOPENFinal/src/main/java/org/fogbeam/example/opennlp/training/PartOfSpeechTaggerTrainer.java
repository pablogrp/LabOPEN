package org.fogbeam.example.opennlp.training;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.postag.WordTagSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

/**
 * @class PartOfSpeechTaggerTrainer
 * @brief Main class for training a POS tagger model using OpenNLP.
 */
public class PartOfSpeechTaggerTrainer {

	// Crear un logger para capturar los errores
	private static final Logger LOGGER = Logger.getLogger(PartOfSpeechTaggerTrainer.class.getName());

	/**
	 * @brief Main method to train the POS tagger model.
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {
		POSModel model = null;
		InputStream dataIn = null;

		try {
			// Load training data
			dataIn = new FileInputStream("training_data/en-pos.train");
			ObjectStream<String> lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
			ObjectStream<POSSample> sampleStream = new WordTagSampleStream(lineStream);

			// Train the model
			model = POSTaggerME.train("en", sampleStream, TrainingParameters.defaultParams(), null, null);
		} catch (IOException e) {
			// Usamos el logger para registrar el error
			LOGGER.log(Level.SEVERE, "Failed to read or parse training data, training failed", e);
		} finally {
			if (dataIn != null) {
				try {
					dataIn.close();
				} catch (IOException e) {
					// Registramos el error si ocurre
					LOGGER.log(Level.WARNING, "Failed to close input stream", e);
				}
			}
		}

		OutputStream modelOut = null;
		String modelFile = "models/en-pos.model";

		try {
			// Save the trained model
			modelOut = new BufferedOutputStream(new FileOutputStream(modelFile));
			model.serialize(modelOut);
		} catch (IOException e) {
			// Usamos el logger para registrar el error
			LOGGER.log(Level.SEVERE, "Failed to save model", e);
		} finally {
			if (modelOut != null) {
				try {
					modelOut.close();
				} catch (IOException e) {
					// Registramos el error si ocurre
					LOGGER.log(Level.SEVERE, "Failed to correctly save model, the written model might be invalid", e);
				}
			}
		}

		System.out.println("done");
	}
}
