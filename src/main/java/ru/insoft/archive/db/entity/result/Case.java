package ru.insoft.archive.db.entity.result;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Благодатских С.
 */
public class Case {

	// number: "МПО-324"
	String number;

//"type": "CT_08",                                                               
	String type;

//"storeLife": "PERSONAL_DATA",                                                  
	String storeLife;

//"title": "административное дело в отношении Сидорова П.И.",                    
	String title;

//"toporef": {"room":  202,"rack": 5,"shelf":17},                                                                             
	TopoRef toporef;

//"remark": "Ивановская область_2011 год (взятие взятки не подтвердилось)",      
	String remark;

//"documents":  [{},{}...] 
	Set<Document> documents;

	public Case(String number, String type, String storeLife, 
			String title, TopoRef toporef, String remark) {
		this.number = number;
		this.type = type;
		this.storeLife = storeLife;
		this.title = title;
		this.toporef = toporef;
		this.remark = remark;
		documents = new HashSet<>();
	}

	public Case(String number, String type, String storeLife, String title, 
			String remark) {
		this.number = number;
		this.type = type;
		this.storeLife = storeLife;
		this.title = title;
		this.remark = remark;
		documents = new HashSet<>();
	}

	public void addDocument(Document document) {
		documents.add(document);
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

	public String getStoreLife() {
		return storeLife;
	}

	public void setStoreLife(String storeLife) {
		this.storeLife = storeLife;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public TopoRef getToporef() {
		return toporef;
	}

	public void setToporef(TopoRef toporef) {
		this.toporef = toporef;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Set<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(Set<Document> documents) {
		this.documents = documents;
	}

}
