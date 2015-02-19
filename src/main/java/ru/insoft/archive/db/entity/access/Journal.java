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
@NamedQueries({@NamedQuery(name = "Journal.findAll", query = "SELECT j FROM Journal j")})
	
public class Journal implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
    @Basic(optional = false)
    @Column(name = "id")
	private Integer id;

	@Column(name = "case_title")
	String caseTitle;

	@Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
	Date startDate;

	@Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
	Date endDate;

	@Column(name = "store_life")
	String storeLife;

	@Column(name = "doc_type")
	String docType;

	@Column(name = "court")
	String court;

	@Column(name = "doc_date")
    @Temporal(TemporalType.TIMESTAMP)
	Date docDate;

	@Column(name = "fio")
	String fio;

	@Column(name = "doc_title")
	String docTitle;

	@Column(name = "doc_number")
	String docNumber;

	@Column(name = "pages")
	Integer docPages;

	@Column(name = "remark")
	String remark;

	@Column(name = "toporef")
	String toporef;

	@Column(name = "graph")
	String graph;

	@Column(name = "case_number")
	String caseNumber;

	@Column(name = "case_type")
	String caseType;

	public Journal() {
	}

	public Journal(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCaseTitle() {
		return caseTitle;
	}

	public void setCaseTitle(String caseTitle) {
		this.caseTitle = caseTitle;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getStoreLife() {
		return storeLife;
	}

	public void setStoreLife(String storeLife) {
		this.storeLife = storeLife;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getCourt() {
		return court;
	}

	public void setCourt(String court) {
		this.court = court;
	}

	public Date getDocDate() {
		return docDate;
	}

	public void setDocDate(Date docDate) {
		this.docDate = docDate;
	}

	public String getFio() {
		return fio;
	}

	public void setFio(String fio) {
		this.fio = fio;
	}

	public String getDocTitle() {
		return docTitle;
	}

	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}

	public String getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

	public Integer getDocPages() {
		return docPages;
	}

	public void setDocPages(Integer docPages) {
		this.docPages = docPages;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getToporef() {
		return toporef;
	}

	public void setToporef(String toporef) {
		this.toporef = toporef;
	}

	public String getGraph() {
		return graph;
	}

	public void setGraph(String graph) {
		this.graph = graph;
	}

	public String getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}


}
