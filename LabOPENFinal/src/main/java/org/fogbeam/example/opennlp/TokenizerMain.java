package org.fogbeam.example.opennlp;

import java.io.*;
import java.nio.file.*;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class TokenizerMain {
	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println("Usage: java TokenizerMain <outputFile> <inputFile1> <inputFile2> ... <inputFileN>");
			System.exit(1);
		}

		// Archivo de salida
		String outputFile = args[0];

		// Modelo de tokenización
		String modelPath = "models/en-token.model";

		try (InputStream modelIn = new FileInputStream(modelPath)) {
			TokenizerModel model = new TokenizerModel(modelIn);
			Tokenizer tokenizer = new TokenizerME(model);

			// Crear directorio de salida si no existe
			File outputFileObj = new File(outputFile);
			if (!outputFileObj.getParentFile().exists()) {
				outputFileObj.getParentFile().mkdirs();
				System.out.println("Directorio de salida creado: " + outputFileObj.getParentFile().getAbsolutePath());
			}

			// Abrir BufferedWriter en modo de sobrescritura
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileObj, false))) {
				System.out.println("Archivo de salida creado: " + outputFileObj.getAbsolutePath());

				// Procesar todos los archivos de entrada pasados como argumentos
				for (int i = 1; i < args.length; i++) {
					Path filePath = Paths.get(args[i]);
					if (Files.isRegularFile(filePath)) {
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
}