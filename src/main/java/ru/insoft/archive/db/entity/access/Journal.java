package ru.insoft.archive.db.entity.access;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Благодатских С.
 */
@Entity
@Table(name = "Journal")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Journal.findAll", query = "SELECT j FROM Journal j"),
	@NamedQuery(name = "Journal.findPart", query = "SELECT j FROM Journal j where j.id in (:start, :end)")
})

public class Journal implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;

	String caseTitle;

	String storeLife;

	String docType;

	String court;

	Date docDate;

	String fio;

	String docTitle;

	String docNumber;

	Integer docPages;

	String remark;

	String toporef;

	String graph;

	String caseNumber;

	String caseType;

	public Journal() {
	}


	@Id
	@Basic(optional = false)
	@Column(name = "id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "case_title")
	public String getCaseTitle() {
		return caseTitle;
	}

	public void setCaseTitle(String caseTitle) {
		this.caseTitle = caseTitle;
	}

	@Column(name = "store_life")
	public String getStoreLife() {
		return storeLife;
	}

	public void setStoreLife(String storeLife) {
		this.storeLife = storeLife;
	}

	@Column(name = "doc_type")
	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	@Column(name = "court")
	public String getCourt() {
		return court;
	}

	public void setCourt(String court) {
		this.court = court;
	}

	@Column(name = "doc_date")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDocDate() {
		return docDate;
	}

	public void setDocDate(Date docDate) {
		this.docDate = docDate;
	}

	@Column(name = "fio")
	public String getFio() {
		return fio;
	}

	public void setFio(String fio) {
		this.fio = fio;
	}

	@Column(name = "doc_title")
	public String getDocTitle() {
		if (docTitle == null || docTitle.trim().isEmpty()) {
			return docType;
		}
		return docTitle;
	}

	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}

	@Column(name = "doc_number")
	public String getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

	@Column(name = "pages")
	public Integer getDocPages() {
		return docPages;
	}

	public void setDocPages(Integer docPages) {
		this.docPages = docPages;
	}

	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "toporef")
	public String getToporef() {
		return toporef;
	}

	public void setToporef(String toporef) {
		this.toporef = toporef;
	}

	@Column(name = "graph")
	public String getGraph() {
		return graph;
	}

	public void setGraph(String graph) {
		this.graph = graph;
	}

	@Column(name = "case_number")
	public String getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}

	@Column(name = "case_type")
	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

}
