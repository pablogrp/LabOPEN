package org.fogbeam.example.opennlp;

import java.io.*;
import java.nio.file.*;
import java.util.stream.Stream;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class TokenizerMain {
	public static void main(String[] args) {
		// Directorio de entrada y archivo de salida
		String inputDirectory = "src/main/java/org/fogbeam/example/opennlp/inputs"; // Cambiar a la ruta real de los archivos de entrada
		String outputFile = "src/main/java/org/fogbeam/example/opennlp/output/output.txt"; // Cambiar a la ruta deseada para el archivo de salida

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

				// Procesar todos los archivos del directorio de entrada
				try (Stream<Path> filePaths = Files.list(Paths.get(inputDirectory))) {
					filePaths.filter(Files::isRegularFile).forEach(filePath -> {
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
					});
				}
			}
			System.out.println("Proceso completado. Tokens guardados en: " + outputFile);
		} catch (IOException e) {
			System.err.println("Error inicializando el modelo o escribiendo el archivo de salida.");
			e.printStackTrace();
		}
	}
}