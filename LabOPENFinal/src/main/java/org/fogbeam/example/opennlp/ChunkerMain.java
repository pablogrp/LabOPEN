package org.fogbeam.example.opennlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;

/**
 * @class ChunkerMain
 * @brief Main class for chunking text using OpenNLP.
 */
public class ChunkerMain {

	private static final Logger LOGGER = Logger.getLogger(ChunkerMain.class.getName());

	/**
	 * @brief Main method to run the chunker.
	 * @param args Command line arguments.
	 * @throws Exception if an error occurs during chunking.
	 */
	public static void main(String[] args) throws Exception {
		InputStream modelIn = null;
		ChunkerModel model = null;
		try {
			modelIn = new FileInputStream("models/en-chunker.model");
			model = new ChunkerModel(modelIn);

			ChunkerME chunker = new ChunkerME(model);

			// Ordinarily you'd use a Tokenizer to do this
			String sent[] = new String[] { "Rockwell", "International", "Corp.", "'s",
					"Tulsa", "unit", "said", "it", "signed", "a", "tentative", "agreement",
					"extending", "its", "contract", "with", "Boeing", "Co.", "to",
					"provide", "structural", "parts", "for", "Boeing", "'s", "747",
					"jetliners", "." };

			// and then use the POS Tagger to do this
			String pos[] = new String[] { "NNP", "NNP", "NNP", "POS", "NNP", "NN",
					"VBD", "PRP", "VBD", "DT", "JJ", "NN", "VBG", "PRP$", "NN", "IN",
					"NNP", "NNP", "TO", "VB", "JJ", "NNS", "IN", "NNP", "POS", "CD", "NNS",
					"." };

			String tag[] = chunker.chunk(sent, pos);

			double probs[] = chunker.probs();

            /*
                The chunk tags contain the name of the chunk type, for
                example I-NP for noun phrase words and I-VP for verb
                phrase words. Most chunk types have two types of chunk
                tags, B-CHUNK for the first word of the chunk and I-CHUNK
                for each other word in the chunk.
            */
			for (int i = 0; i < sent.length; i++) {
				System.out.println("Token [" + sent[i] + "] has chunk tag [" + tag[i] + "] with probability = " + probs[i]);
			}
		} catch (IOException e) {
			// Log the error instead of printing the stack trace
			LOGGER.log(Level.SEVERE, "Model loading failed", e);
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
					// Log the exception during closing
					LOGGER.log(Level.WARNING, "Failed to close InputStream", e);
				}
			}
		}
		System.out.println("done");
	}
}
