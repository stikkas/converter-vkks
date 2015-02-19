package ru.insoft.archive.db.entity.result;

import java.util.Date;

/**
 *
 * @author Благодатских С.
 */
public class Document {

//"number": "3",                                                                 
	String number;

//"type": "DT_03",                                                               
	String type;

//"title": "Опись",                                                              
	String title;

	Integer pages;

//"date": "2010-03-25T07:59:00",                                                 
	Date date;

//"remark": "часть документов отдана адвокату",                                  
	String remark;

//"court": "хамовнический",                                                      
	String court;

//"fio": "Сидоров П.И.",                                                         
	String fio;

//"graph": "тестовый документ.pdf" 
	String graph;

	public Document() {
	}

	public Document(String number, String type, String title, Integer pages, 
			Date date, String remark, String court, String fio, String graph) {
		this.number = number;
		this.type = type;
		this.title = title;
		this.pages = pages;
		this.date = date;
		this.remark = remark;
		this.court = court;
		this.fio = fio;
		this.graph = graph;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getPages() {
		return pages;
	}

	public void setPages(Integer pages) {
		this.pages = pages;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCourt() {
		return court;
	}

	public void setCourt(String court) {
		this.court = court;
	}

	public String getFio() {
		return fio;
	}

	public void setFio(String fio) {
		this.fio = fio;
	}

	public String getGraph() {
		return graph;
	}

	public void setGraph(String graph) {
		this.graph = graph;
	}


}
