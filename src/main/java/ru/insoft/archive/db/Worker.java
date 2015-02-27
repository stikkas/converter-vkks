package ru.insoft.archive.db;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.pdf.PdfReader;
import java.awt.EventQueue;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.swing.JTextArea;
import ru.insoft.archive.config.Config;
import ru.insoft.archive.db.entity.access.Journal;
import ru.insoft.archive.db.entity.dict.DescriptorValue;
import ru.insoft.archive.db.entity.result.Case;
import ru.insoft.archive.db.entity.result.Document;
import ru.insoft.archive.db.entity.result.TopoRef;

/**
 *
 * @author Благодатских С.
 */
public class Worker extends Thread {

	/**
	 * Настройки
	 */
	private final Config config;
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

	/**
	 * регулярное выражение для определения расположения дела
	 */
	private static final Pattern toporefPattern = Pattern.compile("^(?<rack>\\d+)-(?<shelf>\\d+)$");

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
	 * Json builder для получения json представления объектов
	 */
	private final Gson gson;

	public Worker(JTextArea logPanel, Config config) {
		this.logPanel = logPanel;

		this.config = config;
		gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE)
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
	}

	@Override
	public void run() {
		try {
			convertData(config.dbFileName, Paths.get(config.dstDir, "data"));
			log("data from " + config.dbFileName + " have been converted and placed into " + config.dstDir);
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
		props.put("javax.persistence.jdbc.url", "jdbc:postgresql://" + config.dbHost + ":" + config.dbPort + "/" + config.db);
		props.put("javax.persistence.jdbc.password", config.dbPassword);
		props.put("javax.persistence.jdbc.user", config.dbUser);
		props.put("javax.persistence.jdbc.driver", config.dbDriver);
		EntityManager emDict = Persistence.createEntityManagerFactory("Dict", props)
				.createEntityManager();

		if (caseTypeCodes.isEmpty()) {
			fillDict(emDict, CASE_TYPE, caseTypeCodes);
		}
		if (caseStoreLifeCodes.isEmpty()) {
			fillDict(emDict, CASE_STORE_LIFE, caseStoreLifeCodes);
		}
		if (toporefCodes.isEmpty()) {
			fillDict(emDict, TOPOREF, toporefCodes);
		}
		if (docTypeCodes.isEmpty()) {
			fillDict(emDict, DOCUMENT_TYPE, docTypeCodes);
		}

		/*
		 showDicts(caseTypeCodes, "Case Type");
		 showDicts(caseStoreLifeCodes, "Case Store Life");
		 showDicts(toporefCodes, "Toporef");
		 showDicts(docTypeCodes, "Document Type");
		 */
		Map<String, Case> cases = new HashMap<>();

		int caseIndexNumber = 1;
		for (Journal journal : emAccess.createNamedQuery("Journal.findAll", Journal.class).getResultList()) {
			String caseType = journal.getCaseType().trim();
			if (caseType.isEmpty()) {
				caseType = config.caseType;
			}
			String prefix = caseTypeAttrCodes.get(caseType);
			if (prefix==null) {
				log(caseType + " has no prefix! Skip.");
				continue;
			}
			String caseNumber = journal.getCaseNumber().trim();
			if (caseNumber.isEmpty()) {
			 	caseNumber =  prefix + "-" + caseIndexNumber++;
			} else {
			 	caseNumber =  prefix + "-" + caseNumber;
			}

			Case acase = cases.get(caseNumber);

			if (acase == null) {
				acase = new Case(caseNumber, caseTypeCodes.get(caseType),
						caseStoreLifeCodes.get(journal.getStoreLife().trim()),
						journal.getCaseTitle(), journal.getRemark());
				cases.put(caseNumber, acase);
			}
			// Определяем топографический указатель
			String toporef = journal.getToporef().trim();
			if (!toporef.isEmpty()) {
				Matcher matcher = toporefPattern.matcher(toporef);
				if (matcher.find()) {
					acase.setToporef(new TopoRef(
							Integer.valueOf(matcher.group("rack")),
							Integer.valueOf(matcher.group("shelf"))));
				} else {
					log("Wrong format of the TopoRef: " + toporef);
				}

			}
			// пока два возможных формата полученных путей к pdf:
			// 1 - имя_файла#ссылка
			// 2 - директория\имя_файла
			String graph = journal.getGraph();
			Path srcFileName;
			int index = graph.indexOf('#');
			if (index != -1) {
				graph = graph.substring(0, index);
				srcFileName = Paths.get(config.srcPdfDir, graph);
			} else {
				index = graph.indexOf("\\");
				if (index != -1) {
					String dirname = graph.substring(0, index);
					graph = graph.substring(index + 1);
					srcFileName = Paths.get(config.srcPdfDir, dirname, graph);
				} else {
					srcFileName = Paths.get(config.srcPdfDir, graph);
				}
			}
			graph = caseNumber + "_" + graph;
			Path dstFileName = Paths.get(config.dstDir, "files", graph);
			copyPdf(srcFileName, dstFileName);
			Integer pages = journal.getDocPages();
			if (pages == null) { // В некоторых случаях может быть не указано кол-во страниц
				pages = getPagesOfPdf(dstFileName.toString());
			}

			String docNumber = journal.getDocNumber().trim();
			if (docNumber.isEmpty()) {// если не указан номер документа то "б/н" 
				docNumber = "б/н";
			}

			Document doc = new Document(docNumber,
					docTypeCodes.get(journal.getDocType().trim()),
					journal.getDocTitle(), pages,
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
	 * Копирует pdf файлы из исходной директории в директроию назначения Метод
	 * предполагает, что srcPdfDir задана
	 *
	 * @param srcPdfName путь к исходнуму файлу
	 * @param srcPdfName путь к файлу назначения
	 */
	private void copyPdf(Path srcPdfName, Path dstPdfName) throws IOException {
		try {
			Files.copy(srcPdfName, dstPdfName, StandardCopyOption.REPLACE_EXISTING);
			log(srcPdfName + " have been copied to " + dstPdfName);
		} catch (IOException ex) {
			log(srcPdfName + ": " + ex.getMessage());
		}
	}

	/**
	 * Заполняет справочники из базы
	 *
	 * @param emDict entityManager
	 * @param code код для группы значений справочника
	 * @param codes словарь для найденных значений - кодов
	 */
	private void fillDict(EntityManager emDict, String code, Map<String, String> codes) {
		boolean caseType = code.equals(CASE_TYPE);

		for (DescriptorValue value : emDict.createNamedQuery("DescriptorValue.findByGroup", DescriptorValue.class
		)
				.setParameter("code", code).getResultList()) {
			codes.put(value.getFullValue(), value.getValueCode());
			if (caseType) {
				caseTypeAttrCodes.put(value.getFullValue(),
						value.getDescriptorValueAttrCollection().get(0).getAttrValue());
			}
		}
	}

	private void showDicts(Map<String, String> codes, String msg) {
		System.out.println("=================" + msg + "=====================");
		for (String key : codes.keySet()) {
			System.out.println(key + " - " + codes.get(key));
		}
		System.out.println("========================================");
	}

	/**
	 * Получает кол-во страниц в pdf документе
	 *
	 * @param filename имя файла
	 * @return количество страниц
	 */
	private int getPagesOfPdf(String filename) {
		try {
			return new PdfReader(filename).getNumberOfPages();
		} catch (IOException ex) {
			log(filename + ": " + ex.getMessage());
			return 0;
		}
	}
}
