package org.fogbeam.example.opennlp;

import java.io.*;
import java.nio.file.*;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

/**
 * Clase principal para tokenizar archivos de texto.
 *
 * Esta clase carga un modelo de tokenización, tokeniza el contenido de uno o más archivos de entrada y guarda los tokens en un archivo de salida.
 */
public class TokenizerMain {

	/**
	 * Método principal que ejecuta la tokenización.
	 *
	 * @param args Argumentos de línea de comando donde el primer argumento es el archivo de salida y los siguientes son los archivos de entrada.
	 */
	public static void main(String[] args) {
		// Verificar que se pasaron los argumentos necesarios
		if (args.length < 2) {
			System.err.println("Usage: java TokenizerMain <outputFile> <inputFile1> <inputFile2> ... <inputFileN>");
			System.exit(1);
		}

		// Archivo de salida
		String outputFile = args[0];

		// Procesar el modelo de tokenización y los archivos de entrada
		processFiles(args, outputFile);
	}

	/**
	 * Procesa los archivos de entrada, tokeniza su contenido y guarda los tokens en un archivo de salida.
	 *
	 * @param args Argumentos de línea de comando con los archivos de entrada.
	 * @param outputFile Ruta del archivo de salida.
	 */
	private static void processFiles(String[] args, String outputFile) {
		// Modelo de tokenización
		String modelPath = "models/en-token.model";

		try (InputStream modelIn = new FileInputStream(modelPath)) {
			TokenizerModel model = new TokenizerModel(modelIn);
			Tokenizer tokenizer = new TokenizerME(model);

			// Crear directorio de salida si no existe
			createOutputDirectory(outputFile);

			// Abrir BufferedWriter en modo de sobrescritura
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, false))) {
				System.out.println("Archivo de salida creado: " + new File(outputFile).getAbsolutePath());

				// Procesar todos los archivos de entrada pasados como argumentos
				for (int i = 1; i < args.length; i++) {
					Path filePath = Paths.get(args[i]);
					if (Files.isRegularFile(filePath)) {
						processFile(filePath, tokenizer, writer);
					} else {
						System.err.println("El archivo no existe o no es un archivo regular: " + filePath);
					}
				}
			}
			System.out.println("Proceso completado. Tokens guardados en: " + outputFile);
		} catch (IOException e) {
			System.err.println("Error inicializando el modelo o escribiendo el archivo de salida.");
			e.printStackTrace();
		}
	}

	/**
	 * Crea el directorio de salida si no existe.
	 *
	 * @param outputFile Ruta del archivo de salida.
	 */
	private static void createOutputDirectory(String outputFile) {
		File outputFileObj = new File(outputFile);
		if (!outputFileObj.getParentFile().exists()) {
			outputFileObj.getParentFile().mkdirs();
			System.out.println("Directorio de salida creado: " + outputFileObj.getParentFile().getAbsolutePath());
		}
	}

	/**
	 * Procesa un archivo de entrada, lo tokeniza y escribe los tokens en el archivo de salida.
	 *
	 * @param filePath Ruta del archivo de entrada.
	 * @param tokenizer Tokenizador utilizado para tokenizar el contenido.
	 * @param writer BufferedWriter donde se escribirán los tokens.
	 */
	private static void processFile(Path filePath, Tokenizer tokenizer, BufferedWriter writer) {
		try {
			System.out.println("Procesando archivo: " + filePath);
			// Leer el contenido del archivo
			String content = Files.readString(filePath);
			System.out.println("Contenido del archivo: " + content);
			// Tokenizar el contenido
			String[] tokens = tokenizer.tokenize(content);
			// Escribir los tokens en el archivo de salida
			for (String token : tokens) {
				writer.write(token);
				writer.write("\n"); // Escribir cada token en una nueva línea
			}
			writer.write("\n"); // Separar bloques de tokens por archivo
			writer.flush(); // Asegurarse de que los datos se escriben en el archivo
		} catch (IOException e) {
			System.err.println("Error al procesar el archivo: " + filePath);
			e.printStackTrace();
		}
	}
}
