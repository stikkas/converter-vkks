package ru.insoft.archive.db;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.swing.JTextArea;
import org.apache.commons.io.FileUtils;
import ru.insoft.archive.db.entity.access.Z07;

/**
 *
 * @author Благодатских С.
 */
public class Worker extends Thread {

	private final JTextArea logPanel;
	private final String srcDbFileName;
	private final String srcDirPdfName;
	private final String dstDirName;
	private final Gson gson;

	private final String dbPort;
	private final String dbDriver;
	private final String dbHost;
	private final String dbUser;
	private final String dbPassword;
	private final String db;

	public Worker(JTextArea logPanel, String srcDbFileName, 
			String srcDirPdfName, String dstDirName,
			String dbPort, String dbDriver, String dbHost, String dbUser,
			String dbPassword, String db) {
		this.logPanel = logPanel;
		this.srcDbFileName = srcDbFileName;
		this.srcDirPdfName = srcDirPdfName;
		this.dstDirName = dstDirName;
		
		this.dbPort = dbPort;
		this.dbDriver = dbDriver;
		this.dbHost = dbHost;
		this.dbUser = dbUser;
		this.dbPassword = dbPassword;
		this.db = db;

		gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).create();

	}

	@Override
	public void run() {
		try {
			convertData(srcDbFileName, Paths.get(dstDirName, "data"));
			log("data from " + srcDbFileName + " have been converted and placed into " + dstDirName);
			if (!srcDirPdfName.isEmpty()) {
				copyPdf(srcDirPdfName, Paths.get(dstDirName, "files"));
				log("pdf files from " + srcDirPdfName + " have been copied to " + dstDirName);
			}
		} catch (final Exception e) {
			log(e.getMessage());
		}
	}

	/**
	 * Записывает ход выполнения в текстовую панель
	 * @param msg сообщение
	 */
	private void log(final String msg) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				logPanel.append(msg + "\n");
			}
		});

	}

	/**
	 * Преобразует данные из файла Access в файлы json
	 *
	 * @param srcDbFileName база данных MS Access
	 * @param dstDirName директория назначения для json-файлов
	 */
	private void convertData(String srcDbFileName, Path dstDirName) throws IOException {
		Files.createDirectories(dstDirName);

		Properties props = new Properties();
		props.put( "javax.persistence.jdbc.url", "jdbc:ucanaccess://" + srcDbFileName);
		EntityManager emAccess = Persistence.createEntityManagerFactory("Access", props)
				.createEntityManager();

		props = new Properties();
		props.put( "javax.persistence.jdbc.url", "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + db);
		props.put("javax.persistence.jdbc.password", dbPassword);
		props.put("javax.persistence.jdbc.user", dbUser);
		props.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
		EntityManager emDict = Persistence.createEntityManagerFactory("Dict", props)
				.createEntityManager();

		for (Z07 zo7 : emAccess.createNamedQuery("Z07.findAll", Z07.class).getResultList()) {
			Files.write(Paths.get(dstDirName.toString(), zo7.getNom() + ".json"), 
					gson.toJson(zo7).getBytes());
		}
		emDict.close();
		emAccess.close();
	}

	/**
	 * Копирует pdf файлы из исходной директории в директроию назначения
	 *
	 * @param srcDirPdfName директория с файлами pdf
	 * @param dstDirName директория назначения
	 */
	private void copyPdf(String srcDirPdfName, Path dstDirName) throws IOException {
		FileUtils.copyDirectory(new File(srcDirPdfName), dstDirName.toFile());
	}

}
