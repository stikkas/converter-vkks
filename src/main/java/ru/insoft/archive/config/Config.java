package ru.insoft.archive.config;

/**
 *
 * @author stikkas<stikkas@yandex.ru>
 */
public class Config {

	/**
	 * путь к файлу БД Access
	 */
	public String dbFileName;
	/**
	 * путь к папке куда следует поместить преобразованные данные и pdf файлы.
	 * преобразованные данные в dstDirName/data, pdf файлы в dstDirName/files
	 */
	public String dstDir;
	/**
	 * путь к папке с pdf файлами, на которые есть ссылки в БД Access
	 */
	public String srcPdfDir;
	// Настройки подключения к БД справочников
	public String dbPort;
	public String dbDriver;
	public String dbHost;
	public String dbUser;
	public String dbPassword;
	public String db;
	public String httpPort;

	private Config(String dbFileName, String dstDir, String srcPdfDir,
			String dbPort, String dbDriver, String dbHost, String dbUser,
			String dbPassword, String db, String httpPort) {
		this.dbFileName = dbFileName;
		this.srcPdfDir = srcPdfDir;
		this.dstDir = dstDir;
		this.dbPort = dbPort;
		this.dbDriver = dbDriver;
		this.dbHost = dbHost;
		this.dbUser = dbUser;
		this.dbPassword = dbPassword;
		this.db = db;
	}

	public Config() {
		this("", "", "", "", "", "", "", "", "", "");
	}

	@Override
	public Config clone() throws CloneNotSupportedException {
		/*
		 System.out.println("dbFileName = " + dbFileName);
		 System.out.println("dstDir = " + dstDir);
		 System.out.println("srcPdfDir = " + srcPdfDir);
		 System.out.println("dbPort = " + dbPort);
		 System.out.println("dbDriver = " + dbDriver);
		 System.out.println("dbHost = " + dbHost);
		 System.out.println("dbUser = " + dbUser);
		 System.out.println("dbPassword = " + dbPassword);
		 System.out.println("db = " + db);
		 */
		return new Config(dbFileName, dstDir, srcPdfDir,
				dbPort, dbDriver, dbHost, dbUser, dbPassword, db, httpPort);
	}

}
