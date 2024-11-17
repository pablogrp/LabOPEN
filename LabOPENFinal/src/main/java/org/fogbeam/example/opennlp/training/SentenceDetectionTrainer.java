package org.fogbeam.example.opennlp.training;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.sentdetect.SentenceSample;
import opennlp.tools.sentdetect.SentenceSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

/**
 * @class SentenceDetectionTrainer
 * @brief Main class for training a sentence detection model using OpenNLP.
 */
public class SentenceDetectionTrainer {
	/**
	 * @brief Main method to train the sentence detection model.
	 * @param args Command line arguments.
	 * @throws Exception if an error occurs during training.
	 */
	public static void main(String[] args) throws Exception {
		Charset charset = Charset.forName("UTF-8");
		ObjectStream<String> lineStream = new PlainTextByLineStream(new FileInputStream("training_data/en-sent.train"), charset);
		ObjectStream<SentenceSample> sampleStream = new SentenceSampleStream(lineStream);

		SentenceModel model;

		try {
			model = SentenceDetectorME.train("en", sampleStream, true, null, TrainingParameters.defaultParams());
		} finally {
			sampleStream.close();
		}

		OutputStream modelOut = null;
		try {
			modelOut = new BufferedOutputStream(new FileOutputStream("models/en-sent.model"));
			model.serialize(modelOut);
		} finally {
			if (modelOut != null) {
				modelOut.close();
			}
		}

		System.out.println("done");
	}
}