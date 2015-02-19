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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.swing.JTextArea;
import org.apache.commons.io.FileUtils;
import ru.insoft.archive.db.entity.access.Journal;
import ru.insoft.archive.db.entity.dict.DescriptorValue;
import ru.insoft.archive.db.entity.result.Case;
import ru.insoft.archive.db.entity.result.Document;

/**
 *
 * @author Благодатских С.
 */
public class Worker extends Thread {

	// Коды для групп справочников
	/**
	 * Тип архивного дела
	 */
	private static final String CASE_TYPE = "CASE_TYPE";
	/**
	 * Срок хранения архивного дела
	 */
	private static final String CASE_STORE_LIFE = "CASE_STORE_LIFE";
	/**
	 * Вид архивного документа
	 */
	private static final String DOCUMENT_TYPE = "DOCUMENT_TYPE";
	/**
	 * Топографический указатель
	 */
	private static final String TOPOREF = "TOPOREF";

	private static final Map<String, String> caseTypeCodes = new HashMap<>();
	private static final Map<String, String> caseStoreLifeCodes = new HashMap<>();
	private static final Map<String, String> docTypeCodes = new HashMap<>();
	private static final Map<String, String> toporefCodes = new HashMap<>();
	private static final Map<String, String> caseTypeAttrCodes = new HashMap<>();
	/**
	 * Информационная панель для вывода информации
	 */
	private final JTextArea logPanel;
	/**
	 * путь к файлу БД Access
	 */
	private final String srcDbFileName;
	/**
	 * путь к папке с pdf файлами, на которые есть ссылки в БД Access
	 */
	private final String srcDirPdfName;
	/**
	 * путь к папке куда следует поместить преобразованные данные и pdf файлы.
	 * преобразованные данные в dstDirName/data, pdf файлы в dstDirName/files
	 */
	private final String dstDirName;
	/**
	 * Json builder для получения json представления объектов
	 */
	private final Gson gson;

	// Настройки подключения к БД справочников
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

		gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE)
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

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
	 *
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
		props.put("javax.persistence.jdbc.url", "jdbc:ucanaccess://" + srcDbFileName);
		EntityManager emAccess = Persistence.createEntityManagerFactory("Access", props)
				.createEntityManager();

		props = new Properties();
		props.put("javax.persistence.jdbc.url", "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + db);
		props.put("javax.persistence.jdbc.password", dbPassword);
		props.put("javax.persistence.jdbc.user", dbUser);
		props.put("javax.persistence.jdbc.driver", dbDriver);
		EntityManager emDict = Persistence.createEntityManagerFactory("Dict", props)
				.createEntityManager();

		if (caseTypeCodes.isEmpty())
			fillDict(emDict, CASE_TYPE, caseTypeCodes);
		if (caseStoreLifeCodes.isEmpty())
			fillDict(emDict, CASE_STORE_LIFE, caseStoreLifeCodes);
		if (toporefCodes.isEmpty())
			fillDict(emDict, TOPOREF, toporefCodes);
		if (docTypeCodes.isEmpty())
			fillDict(emDict, DOCUMENT_TYPE, docTypeCodes);

		/*
		showDicts(caseTypeCodes, "Case Type");
		showDicts(caseStoreLifeCodes, "Case Store Life");
		showDicts(toporefCodes, "Toporef");
		showDicts(docTypeCodes, "Document Type");
		*/

		Map<String, Case> cases = new HashMap<>();

		// Для вестника ВККС
		for (Journal journal : emAccess.createNamedQuery("Journal.findAll", Journal.class).getResultList()) {
			String caseType = journal.getCaseType();
			String caseNumber = caseTypeAttrCodes.get(caseType.trim()) + "-" + journal.getCaseNumber();
			Case acase = cases.get(caseNumber);

			if (acase == null){
				acase = new Case(caseNumber, caseTypeCodes.get(caseType.trim()), 
						caseStoreLifeCodes.get(journal.getStoreLife().trim()),
						journal.getCaseTitle(), journal.getRemark());
				cases.put(caseNumber, acase);
			}

			String graph = journal.getGraph();
			graph = graph.substring(0, graph.indexOf('#'));

			Document doc = new Document(journal.getDocNumber(), 
					docTypeCodes.get(journal.getDocType().trim()), 
					journal.getDocTitle(), journal.getDocPages(), 
					journal.getDocDate(), journal.getRemark(), 
					journal.getCourt(), journal.getFio(), graph);

			acase.addDocument(doc);
		}

		emDict.close();
		emAccess.close();
		String dst = dstDirName.toString();
		for (String key : cases.keySet()) {
			Files.write(Paths.get(dst, key + ".json"), gson.toJson(cases.get(key)).getBytes());
		}
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

	/**
	 * Заполняет справочники из базы
	 * @param emDict entityManager 
	 * @param code  код для группы значений справочника
	 * @param codes словарь для найденных значений - кодов
	 */
	private void fillDict(EntityManager emDict, String code, Map<String, String> codes) {
		boolean caseType= code.equals(CASE_TYPE);
		for( DescriptorValue value : emDict.createNamedQuery("DescriptorValue.findByGroup", DescriptorValue.class)
				.setParameter("code", code).getResultList()) {
			codes.put(value.getFullValue(), value.getValueCode());	
			if (caseType)
				caseTypeAttrCodes.put(value.getFullValue(), 
						value.getDescriptorValueAttrCollection().get(0).getAttrValue());
		}
	}

	private void showDicts(Map<String, String> codes, String msg) {
		System.out.println("=================" + msg + "=====================");
		for (String key : codes.keySet()){
			System.out.println(key + " - " + codes.get(key));
		}
		System.out.println("========================================");
	}
}
