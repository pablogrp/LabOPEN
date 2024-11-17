package org.fogbeam.example.opennlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

/**
 * Clase SentenceDetectionMain
 */
public class SentenceDetectionMain {

	private static final Logger LOGGER = Logger.getLogger(SentenceDetectionMain.class.getName());

	/**
	 * Método main
	 *
	 * @param args Argumentos de línea de comandos
	 */
	public static void main(String[] args) {
		try (InputStream modelIn = new FileInputStream("models/en-sent.model");
			 InputStream demoDataIn = new FileInputStream("demo_data/en-sent1.demo")) {

			SentenceModel model = new SentenceModel(modelIn);
			SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);

			String demoData = convertStreamToString(demoDataIn);
			String[] sentences = sentenceDetector.sentDetect(demoData);

			for (String sentence : sentences) {
				System.out.println(sentence + "\n");
			}

		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error reading the sentence detection model or demo data", e);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "An unexpected error occurred", e);
		}

		System.out.println("done");
	}

	/**
	 * Método convertStreamToString
	 *
	 * @param is InputStream
	 * @return Cadena convertida desde el InputStream
	 */
	static String convertStreamToString(java.io.InputStream is) {
		try (Scanner s = new Scanner(is).useDelimiter("\\A")) {
			return s.hasNext() ? s.next() : "";
		}
	}
}
