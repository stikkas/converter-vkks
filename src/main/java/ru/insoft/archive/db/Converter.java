package ru.insoft.archive.db;

import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import ru.insoft.archive.config.Config;
import ru.insoft.archive.eavkks.ejb.es.EsSearchHelperRemote;
import ru.insoft.archive.eavkks.load.ejb.LoaderRemote;

/**
 *
 * @author Благодатских С.
 */
public class Converter extends javax.swing.JFrame {

	private final Config props;

	/**
	 * Creates new form Converter
	 */
	public Converter() {
		initComponents();
		props = new Config();

		props.db = dbEdit.getText();
		props.dbUser = loginEdit.getText();
		props.dbPassword = new String(passwordEdit.getPassword());
		props.dbPort = portEdit.getText();
		props.dbHost = hostEdit.getText();
		props.dbDriver = (String) driverBox.getItemAt(driverBox.getSelectedIndex());
		props.httpPort = httpPortEdit.getText();

		chooser = new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(false);
		setExtendedChangeTextListener(dbFileEdit, "dbFileName");
		setExtendedChangeTextListener(srcDirEdit, "srcPdfDir");
		setExtendedChangeTextListener(dstDirEdit, "dstDir");
		setMainChangeTextListener(dbEdit, "db");
		setMainChangeTextListener(portEdit, "dbPort");
		setMainChangeTextListener(hostEdit, "dbHost");
		setMainChangeTextListener(loginEdit, "dbUser");
		setMainChangeTextListener(httpPortEdit, "httpPort");
		setPasswordChangeListener(passwordEdit, "dbPassword");
	}

	/**
	 * делаем доступной кнопку выполнения
	 */
	private void setExecEnabled() {

		if (props.dbFileName.isEmpty() || props.dstDir.isEmpty() || props.srcPdfDir.isEmpty()) {
			convertButton.setEnabled(false);
		} else {
			Path srcFile = Paths.get(props.dbFileName);
			convertButton.setEnabled(Files.isRegularFile(srcFile) && Files.isReadable(srcFile)
					&& Files.isDirectory(Paths.get(props.dstDir))
					&& Files.isDirectory(Paths.get(props.srcPdfDir)));
		}
		saveToDBButton.setEnabled(!props.dstDir.isEmpty() && Files.isDirectory(Paths.get(props.dstDir)));
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToggleButton1 = new javax.swing.JToggleButton();
        dbFileEdit = new javax.swing.JTextField();
        dbFileChoose = new javax.swing.JButton();
        dstDirEdit = new javax.swing.JTextField();
        dstDirChoose = new javax.swing.JButton();
        convertButton = new javax.swing.JButton();
        srcDirEdit = new javax.swing.JTextField();
        srcDirChoose = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        driverBox = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        hostEdit = new javax.swing.JTextField();
        portEdit = new javax.swing.JTextField();
        loginEdit = new javax.swing.JTextField();
        dbPropsCheck = new javax.swing.JCheckBox();
        passwordEdit = new javax.swing.JPasswordField();
        jLabel6 = new javax.swing.JLabel();
        dbEdit = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        logPanel = new javax.swing.JTextArea();
        saveToDBButton = new javax.swing.JButton();
        httpCheckBox = new javax.swing.JCheckBox();
        httpPortEdit = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        createSchemeBox = new javax.swing.JCheckBox();
        countPages = new javax.swing.JCheckBox();

        jToggleButton1.setText("jToggleButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Конвертор данных из MS Access в ВККС");

        dbFileEdit.setToolTipText("Путь к файлу с БД MS Access");

        dbFileChoose.setText("Файл БД");
        dbFileChoose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dbFileChooseActionPerformed(evt);
            }
        });

        dstDirEdit.setToolTipText("Путь к папке с результатами конвертации");

        dstDirChoose.setText("Папка назначения");
        dstDirChoose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dstDirChooseActionPerformed(evt);
            }
        });

        convertButton.setText("Преобразовать");
        convertButton.setEnabled(false);
        convertButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                convertButtonActionPerformed(evt);
            }
        });

        srcDirEdit.setToolTipText("Путь к папке с файлами pdf");

        srcDirChoose.setText("Папка с PDFs");
        srcDirChoose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                srcDirChooseActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Настройки подключения к БД справочников", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP));

        jLabel1.setText("Драйвер:");

        driverBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "org.postgresql.Driver", "oracle.jdbc.OracleDriver", "com.mysql.jdbc.Driver" }));
        driverBox.setEnabled(false);
        driverBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                driverChanged(evt);
            }
        });

        jLabel2.setText("Сервер:");

        jLabel3.setText("Порт:");

        jLabel4.setText("Логин:");

        jLabel5.setText("Пароль:");

        hostEdit.setText("localhost");
        hostEdit.setEnabled(false);

        portEdit.setText("5432");
        portEdit.setEnabled(false);

        loginEdit.setText("vkks");
        loginEdit.setEnabled(false);

        dbPropsCheck.setText("Изменить");
        dbPropsCheck.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                dbPropsCheckItemStateChanged(evt);
            }
        });

        passwordEdit.setText("vkks");
        passwordEdit.setEnabled(false);

        jLabel6.setText("База:");

        dbEdit.setText("calypso");
        dbEdit.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(driverBox, 0, 391, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(portEdit, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                            .addComponent(loginEdit)
                            .addComponent(passwordEdit))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(dbPropsCheck))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(dbEdit))))
                    .addComponent(hostEdit))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(driverBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(hostEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(portEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(dbEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(loginEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(passwordEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 16, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dbPropsCheck)
                        .addContainerGap())))
        );

        logPanel.setEditable(false);
        logPanel.setColumns(20);
        logPanel.setRows(5);
        jScrollPane1.setViewportView(logPanel);

        saveToDBButton.setText("Записать в ВККС");
        saveToDBButton.setEnabled(false);
        saveToDBButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveToDBButtonActionPerformed(evt);
            }
        });

        httpCheckBox.setText("HTTP клиент");
        httpCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                httpCheckBoxActionPerformed(evt);
            }
        });

        httpPortEdit.setText("8080");
        httpPortEdit.setEnabled(false);

        jLabel7.setText("Порт:");

        createSchemeBox.setText("Создать схему");
        createSchemeBox.setToolTipText("При выбранной опции существующая схема и файлы будут удалены");

        countPages.setText("Считать листы");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dstDirEdit, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dbFileEdit, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(srcDirEdit, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dbFileChoose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dstDirChoose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(srcDirChoose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(convertButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(saveToDBButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(httpCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(26, 26, 26)))
                    .addComponent(createSchemeBox)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(httpPortEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(countPages, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dbFileEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dbFileChoose))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(srcDirEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(srcDirChoose))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dstDirChoose)
                    .addComponent(dstDirEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(countPages)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(convertButton))
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(httpCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(httpPortEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(createSchemeBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveToDBButton)))
                .addContainerGap())
        );

        dbFileEdit.getAccessibleContext().setAccessibleName("dbFileEdit");
        dbFileChoose.getAccessibleContext().setAccessibleDescription("Выбор файла с исходными данными");
        dstDirEdit.getAccessibleContext().setAccessibleName("dstDirEdit");
        dstDirEdit.getAccessibleContext().setAccessibleDescription("Путь к преобразованным данным");
        dstDirChoose.getAccessibleContext().setAccessibleDescription("Выбор папки назначения");
        convertButton.getAccessibleContext().setAccessibleName("execButton");
        convertButton.getAccessibleContext().setAccessibleDescription("");
        jPanel1.getAccessibleContext().setAccessibleName("");
        jPanel1.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dbFileChooseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dbFileChooseActionPerformed
		chooser.setDialogTitle("Выбор файла с БД MS Access");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setFileFilter(fileFilter);
		if (sourceFile != null) {
			chooser.setCurrentDirectory(sourceFile.getParentFile());
		}
		int result = chooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			sourceFile = chooser.getSelectedFile();
			dbFileEdit.setText(sourceFile.getPath());
		}
    }//GEN-LAST:event_dbFileChooseActionPerformed

    private void dstDirChooseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dstDirChooseActionPerformed
		chooser.setDialogTitle("Выбор папки для измененных данных");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setFileFilter(dirFilter);
		if (destinationDir != null) {
			chooser.setCurrentDirectory(destinationDir.getParentFile());
		}
		int result = chooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			destinationDir = chooser.getSelectedFile();
			dstDirEdit.setText(destinationDir.getPath());
		}

    }//GEN-LAST:event_dstDirChooseActionPerformed

    private void srcDirChooseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_srcDirChooseActionPerformed
		chooser.setDialogTitle("Выбор папки с файлами pdf");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setFileFilter(dirFilter);
		if (sourceFile != null) {
			chooser.setCurrentDirectory(sourceFile.getParentFile());
		}
		int result = chooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			srcDirEdit.setText(chooser.getSelectedFile().getPath());
		}

    }//GEN-LAST:event_srcDirChooseActionPerformed

    private void convertButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_convertButtonActionPerformed
		try {
			new Worker(logPanel, props.clone(), helperBean, countPages.isSelected()).start();
		} catch (CloneNotSupportedException ex) {
			Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
		}

    }//GEN-LAST:event_convertButtonActionPerformed

	/**
	 * Записывает преобразованные данные в базы ElsticSearch ВККС
	 *
	 * @param dstDirName папка с преобразованными данными
	 */
    private void saveToDBButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveToDBButtonActionPerformed
		String dstDir = dstDirEdit.getText();
		if (httpCheckBox.isSelected()) { // Выполняем заливку через http 
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://localhost:" + props.httpPort + "/ea-vkks-web/load");

			try {
				post.addHeader("Accept", "text/plain");
				post.setEntity(new StringEntity(dstDir, "text/plain", Charset.defaultCharset().toString()));
				ResponseHandler<String> rh = new BasicResponseHandler();
				logPanel.append(client.execute(post, rh) + "\n");
			} catch (IOException ex) {
				logPanel.append(ex.getMessage() + "\n");
			} finally {
				client.getConnectionManager().shutdown();
			}
		} else { // Делаем все через ejb бин
			try {
				logPanel.append(remoteBean.loadRemote(dstDir, createSchemeBox.isSelected()) + "\n");
			} catch (Exception e) {
				logPanel.append(e.getMessage() + "\n");
			}
		}
    }//GEN-LAST:event_saveToDBButtonActionPerformed

    private void dbPropsCheckItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_dbPropsCheckItemStateChanged
		enableDisableDbProps(evt.getStateChange() == ItemEvent.SELECTED);
    }//GEN-LAST:event_dbPropsCheckItemStateChanged

    private void driverChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_driverChanged
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			props.dbDriver = evt.getItem().toString();
		}
    }//GEN-LAST:event_driverChanged

    private void httpCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_httpCheckBoxActionPerformed
		httpPortEdit.setEnabled(httpCheckBox.isSelected());
    }//GEN-LAST:event_httpCheckBoxActionPerformed

	/**
	 * Включает или отключает доступность изменения настроек подключиения к БД
	 * со справочниками.
	 *
	 * @param stat состояние true - сделать доступными, false - сделать
	 * недоступными
	 */
	private void enableDisableDbProps(boolean stat) {
		portEdit.setEnabled(stat);
		hostEdit.setEnabled(stat);
		loginEdit.setEnabled(stat);
		passwordEdit.setEnabled(stat);
		dbEdit.setEnabled(stat);
		driverBox.setEnabled(stat);
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		System.setProperty("awt.useSystemAAFontSettings", "lcd");
		System.setProperty("swing.aatext", "true");

		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;

				}
			}
		} catch (ClassNotFoundException | InstantiationException |
				IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(Converter.class
					.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Converter converter = new Converter();
				converter.setMinimumSize(converter.getSize());
				converter.setLocationRelativeTo(null);
				converter.setVisible(true);
				try {
					setBeans();
				} catch (NamingException ex) {
					JOptionPane.showMessageDialog(null, "Не могу получить удаленный бин.\n"
							+ "Проверьте настройки подключения к серверу приложений", "Ошибка",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton convertButton;
    private javax.swing.JCheckBox countPages;
    private javax.swing.JCheckBox createSchemeBox;
    private javax.swing.JTextField dbEdit;
    private javax.swing.JButton dbFileChoose;
    private javax.swing.JTextField dbFileEdit;
    private javax.swing.JCheckBox dbPropsCheck;
    private javax.swing.JComboBox driverBox;
    private javax.swing.JButton dstDirChoose;
    private javax.swing.JTextField dstDirEdit;
    private javax.swing.JTextField hostEdit;
    private javax.swing.JCheckBox httpCheckBox;
    private javax.swing.JTextField httpPortEdit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JTextArea logPanel;
    private javax.swing.JTextField loginEdit;
    private javax.swing.JPasswordField passwordEdit;
    private javax.swing.JTextField portEdit;
    private javax.swing.JButton saveToDBButton;
    private javax.swing.JButton srcDirChoose;
    private javax.swing.JTextField srcDirEdit;
    // End of variables declaration//GEN-END:variables
	private static LoaderRemote remoteBean;
	private static EsSearchHelperRemote helperBean;
	private File sourceFile;
	private File destinationDir;
	private final JFileChooser chooser;
	private final FileFilter fileFilter = new FileFilter() {

		@Override
		public boolean accept(File f) {
			return f.isDirectory() || f.getName().endsWith(".mdb");
		}

		@Override
		public String getDescription() {
			return "MS Access (*.mdb)";
		}

	};

	private final FileFilter dirFilter = new FileFilter() {

		@Override
		public boolean accept(File f) {
			return f.isDirectory();
		}

		@Override
		public String getDescription() {
			return "Directories";
		}

	};

	/**
	 * Получает доступ к ejb бину для заливки данных на сервер
	 *
	 * @throws NamingException
	 */
	private static void setBeans() throws NamingException {
		final Properties jndiProperties = new Properties();
		jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		jndiProperties.put("scoped.context", "true");
// username
//		jndiProperties.put(Context.SECURITY_PRINCIPAL, "LOAD");
// password
//		jndiProperties.put(Context.SECURITY_CREDENTIALS, "loader");
		final Context context = new InitialContext(jndiProperties);

//		final String appName = "";
		final String moduleName = "ea-vkks-web-1.1.0";
//		final String distinctName = "";
		final String beanName = "LoaderImpl";
		final String viewClassName = LoaderRemote.class
				.getName();

		String connectionName = "ejb:" + /*appName +*/ "/" + moduleName + "/" /*+ distinctName */
				+ "/" + beanName + "!" + viewClassName;

		remoteBean = (LoaderRemote) context.lookup(connectionName);
		connectionName = "ejb:/" + moduleName + "//" + "EsSearchHelperImpl!" + EsSearchHelperRemote.class.getName();
		helperBean = (EsSearchHelperRemote) context.lookup(connectionName);
	}

	private void setConfigValue(JTextField fieldSource, String fieldName) {
		try {
			props.getClass().getDeclaredField(fieldName).set(props, fieldSource.getText());
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
			Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	private void setPasswordValue(JPasswordField fieldSource, String fieldName) {
		try {
			props.getClass().getDeclaredField(fieldName).set(props, new String(fieldSource.getPassword()));
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
			Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	private void setMainChangeTextListener(final JTextField field, final String fieldName) {
		field.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				setConfigValue(field, fieldName);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				setConfigValue(field, fieldName);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				setConfigValue(field, fieldName);
			}
		});

	}

	private void setPasswordChangeListener(final JPasswordField field, final String fieldName) {
		field.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				setPasswordValue(field, fieldName);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				setPasswordValue(field, fieldName);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				setPasswordValue(field, fieldName);
			}
		});

	}

	private void setExtendedChangeTextListener(final JTextField field, final String fieldName) {
		field.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				setConfigValue(field, fieldName);
				setExecEnabled();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				setConfigValue(field, fieldName);
				setExecEnabled();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				setConfigValue(field, fieldName);
				setExecEnabled();
			}
		});

	}
}
