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
			// Cargar los datos de entrenamiento
			dataIn = new FileInputStream("training_data/en-pos.train");
			ObjectStream<String> lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
			ObjectStream<POSSample> sampleStream = new WordTagSampleStream(lineStream);

			// Entrenar el modelo
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

		if (model == null) {
			LOGGER.severe("Model training failed, the model is null. Exiting...");
			return;  // Si el modelo es nulo, terminamos el proceso
		}

		// Guardar el modelo entrenado si no es nulo
		try (OutputStream modelOut = new BufferedOutputStream(new FileOutputStream("models/en-pos.model"))) {
			model.serialize(modelOut); // Serializar el modelo a un archivo
		} catch (IOException e) {
			// Usamos el logger para registrar el error
			LOGGER.log(Level.SEVERE, "Failed to save model", e);
		}

		// Mensaje de éxito al final
		System.out.println("done");
	}
}
