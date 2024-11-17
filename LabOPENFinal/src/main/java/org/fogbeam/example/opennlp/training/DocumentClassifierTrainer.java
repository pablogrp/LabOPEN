package org.fogbeam.example.opennlp.training;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

/**
 * @class DocumentClassifierTrainer
 * @brief Main class for training a document classifier model using OpenNLP.
 */
public class DocumentClassifierTrainer {

	// Crear un logger para capturar los errores
	private static final Logger LOGGER = Logger.getLogger(DocumentClassifierTrainer.class.getName());

	/**
	 * @brief Main method to train the document classifier model.
	 * @param args Command line arguments.
	 * @throws Exception if an error occurs during training.
	 */
	public static void main(String[] args) {
		DoccatModel model = null;
		InputStream dataIn = null;

		try {
			dataIn = new FileInputStream("training_data/en-doccat.train");
			ObjectStream<String> lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
			ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);
			model = DocumentCategorizerME.train("en", sampleStream);
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

		if (model == null) {
			// Si el modelo es nulo, mostramos un mensaje de error y terminamos
			LOGGER.severe("Model training failed, the model is null. Exiting...");
			return;
		}

		OutputStream modelOut = null;
		String modelFile = "models/en-doccat.model";

		try {
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
