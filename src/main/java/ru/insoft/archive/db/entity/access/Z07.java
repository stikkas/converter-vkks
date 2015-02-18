package ru.insoft.archive.db.entity.access;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Благодатских С.
 */
@Entity
@Table(name = "Z_07")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Z07.findAll", query = "SELECT z FROM Z07 z")})
public class Z07 implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@Column(name = "NOM")
	private Integer nom;
	@Column(name = "NAMEFOND")
	String namefond;
	@Column(name = "NOMFOND")
	String nomfond;
	@Column(name = "NOMOPIS")
	String nomopis;
	@Column(name = "NOMDELA")
	String nomdela;
	@Column(name = "GODDELA")
	String goddela;
	@Column(name = "NAMEDELO")
	String namedelo;
	@Column(name = "LITDELA")
	String litdela;
	@Column(name = "NAMEORG")
	String nameorg;
	@Column(name = "TYPEDOC")
	String typedoc;
	@Column(name = "FIO")
	String fio;
	@Column(name = "GODROD")
	String godrod;
	@Column(name = "PATHTIF")
	String pathtif;
	@Column(name = "COMMENTS")
	String comments;

	public Z07() {
	}

	public Integer getNom() {
		return nom;
	}

	public void setNom(Integer nom) {
		this.nom = nom;
	}

	public String getNamefond() {
		return namefond;
	}

	public void setNamefond(String namefond) {
		this.namefond = namefond;
	}

	public String getNomfond() {
		return nomfond;
	}

	public void setNomfond(String nomfond) {
		this.nomfond = nomfond;
	}

	public String getNomopis() {
		return nomopis;
	}

	public void setNomopis(String nomopis) {
		this.nomopis = nomopis;
	}

	public String getNomdela() {
		return nomdela;
	}

	public void setNomdela(String nomdela) {
		this.nomdela = nomdela;
	}

	public String getGoddela() {
		return goddela;
	}

	public void setGoddela(String goddela) {
		this.goddela = goddela;
	}

	public String getNamedelo() {
		return namedelo;
	}

	public void setNamedelo(String namedelo) {
		this.namedelo = namedelo;
	}

	public String getLitdela() {
		return litdela;
	}

	public void setLitdela(String litdela) {
		this.litdela = litdela;
	}

	public String getNameorg() {
		return nameorg;
	}

	public void setNameorg(String nameorg) {
		this.nameorg = nameorg;
	}

	public String getTypedoc() {
		return typedoc;
	}

	public void setTypedoc(String typedoc) {
		this.typedoc = typedoc;
	}

	public String getFio() {
		return fio;
	}

	public void setFio(String fio) {
		this.fio = fio;
	}

	public String getGodrod() {
		return godrod;
	}

	public void setGodrod(String godrod) {
		this.godrod = godrod;
	}

	public String getPathtif() {
		return pathtif;
	}

	public void setPathtif(String pathtif) {
		this.pathtif = pathtif;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}


}
