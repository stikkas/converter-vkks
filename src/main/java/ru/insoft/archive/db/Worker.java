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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.swing.JTextArea;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import ru.insoft.archive.config.Config;
import ru.insoft.archive.db.entity.access.Journal;
import ru.insoft.archive.db.entity.dict.DescriptorValue;
import ru.insoft.archive.db.entity.result.Case;
import ru.insoft.archive.db.entity.result.Document;
import ru.insoft.archive.db.entity.result.TopoRef;
import ru.insoft.archive.eavkks.ejb.es.EsSearchHelperRemote;

/**
 *
 * @author Благодатских С.
 */
public class Worker extends Thread {

	private EsSearchHelperRemote helper;
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
	private static final javax.validation.Validator validator
			= Validation.buildDefaultValidatorFactory().getValidator();

	/**
	 * Информационная панель для вывода информации
	 */
	private final JTextArea logPanel;
	/**
	 * Json builder для получения json представления объектов
	 */
	private final Gson gson;

	public Worker(JTextArea logPanel, Config config, EsSearchHelperRemote helper) {
		this.logPanel = logPanel;
		this.helper = helper;
		this.config = config;
		gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE)
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
	}

	@Override
	public void run() {
		try {
			convertData();
			log("data from " + config.dbFileName + " have been converted and placed into " + config.dstDir);
		} catch (final Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append(e.getMessage() + "\n");
			for (StackTraceElement el : e.getStackTrace()) {
				sb.append("\t").append(el.toString()).append("\n");
			}
			log(sb.toString());
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
	 */
	private void convertData() throws IOException {
		Files.createDirectories(Paths.get(config.dstDir, "data"));
		Files.createDirectories(Paths.get(config.dstDir, "files"));
		Properties props = new Properties();
		props.put("javax.persistence.jdbc.url", "jdbc:ucanaccess://" + config.dbFileName);
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
		// Бывают слуючаи, что разные документы ссылаются на один и тот же файл
		// тогда надо вести учет скопированных файлов. Но с другой стороны
		// При удалении одного документа у нас удалится файл, на который ссылается
		// другой документ. Исходя из этого было решено не учитывать возможность повторения
		// файлов, и копировать все файлы, как-будто они все разные

		String currentPrefix = "";
		int index = 1;
		long docIndex = 1;
		for (Journal journal : emAccess.createNamedQuery("Journal.findAll", Journal.class).getResultList()) {
			String caseType = journal.getCaseType();
			String prefix = caseTypeAttrCodes.get(caseType);
			if (prefix == null) {
				log(caseType + " has no prefix! Skip.");
				continue;
			}
			if (!prefix.equals(currentPrefix)) {
				currentPrefix = prefix;
				index = helper.queryMaxCaseNumberForPrefix(prefix) + 1;
			}
			String caseNumber = journal.getCaseNumber();
			if (caseNumber == null || caseNumber.isEmpty()) {
//				caseNumber = prefix + "-" + journal.getId();
				caseNumber = prefix + "-" + index++;
			} else {
				caseNumber = prefix + "-" + caseNumber.trim();
			}
			// Предполагаем, что у одного дела может быть несколько документов
			// Каждая запись соответствует одному документу
			// Если номер делу назначаем мы, тогда на один документ приходится одно дело
			Case acase = cases.get(caseNumber);

			String storeLife = journal.getStoreLife();
			if (storeLife == null || storeLife.isEmpty()) {
				storeLife = "Не хранится";
			}
			if (acase == null) {
				acase = new Case(caseNumber, caseTypeCodes.get(caseType),
						caseStoreLifeCodes.get(storeLife),
						journal.getCaseTitle(), journal.getRemark());
				log("Наполнение дела " + caseNumber);
				if (!validate(acase, " дела")) {
					continue;
				}
				cases.put(caseNumber, acase);
			}
			// Определяем топографический указатель
			String toporef = journal.getToporef();
			if (toporef != null && !toporef.isEmpty()) {
				Matcher matcher = toporefPattern.matcher(toporef.trim());
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

			if (graph.startsWith("#")) {
				graph = graph.substring(1);
			}
			if (graph.endsWith("#")) {
				graph = graph.substring(0, graph.length() - 1);
			}

			// Определяем местоположение исходного файла pdf
			Path srcFileName;
			int idx = graph.indexOf('#');
			if (idx != -1) {
				srcFileName = Paths.get(config.srcPdfDir, graph.substring(0, idx));
			} else {
				idx = graph.indexOf("\\");
				if (idx != -1) {
					srcFileName = Paths.get(config.srcPdfDir, graph.substring(0, idx),
							graph.substring(idx + 1));
				} else {
					srcFileName = Paths.get(config.srcPdfDir, graph);
				}
			}

			String docNumber = journal.getDocNumber();
			if (docNumber == null || docNumber.trim().isEmpty()) {// если не указан номер документа то "б/н" 
				docNumber = "б/н";
			}

			graph = caseNumber + "_" + (docIndex++) + ".pdf";
			Integer pages = journal.getDocPages();
			if (pages == null) { // В некоторых случаях может быть не указано кол-во страниц
				pages = getPagesOfPdf(srcFileName.toString());
			}

			Date docDate = journal.getDocDate();
			if (docDate == null) {
				docDate = new Date();
			}
			String docType = journal.getDocType();
			Document doc = new Document(docNumber,
					docTypeCodes.get(docType),
					journal.getDocTitle(), pages,
					docDate, journal.getRemark(),
					journal.getCourt(), journal.getFio(), graph);
			if (!validate(doc, " документа " + docNumber + ", тип " + docType)) {
				continue;
			}
			// Копируем pdf файл
			Path dstFileName = Paths.get(config.dstDir, "files", graph);
			copyPdf(srcFileName, dstFileName);
			acase.addDocument(doc);
		}

		emDict.close();
		emAccess.close();
		for (String key : cases.keySet()) {
			Files.write(Paths.get(config.dstDir, "data", key + ".json"), gson.toJson(cases.get(key)).getBytes());
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
//			log(srcPdfName + " have been copied to " + dstPdfName);
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

	/**
	 * Произовдит проверку обязательных полей дела или документа
	 *
	 * @param item дело или документ
	 * @param suffix отличающая строка, участвующая в сообщении
	 * @return в случае отрицательных результатов возвращается false
	 */
	private <T> boolean validate(T item, String suffix) {
		Set<ConstraintViolation<T>> errors = validator.validate(item);
		if (!errors.isEmpty()) {
			log("Ошибки при создании" + suffix);
			for (ConstraintViolation c : errors) {
				log("\t" + c.getMessage());
			}
			return false;
		}
		return true;
	}
}
