package ru.insoft.archive.db.entity.dict;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Благодатских С.
 */
@Entity
@Table(name = "descriptor_value")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "DescriptorValue.findAll", query = "SELECT d FROM DescriptorValue d"),
	@NamedQuery(name = "DescriptorValue.findByGroup",
			query = "SELECT d FROM DescriptorValue d LEFT JOIN d.descriptorGroupId g where g.groupCode = :code")
})
public class DescriptorValue implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "descriptor_value_id")
	private Long descriptorValueId;

	@Basic(optional = false)
	@Column(name = "full_value")
	private String fullValue;

	@Column(name = "value_code")
	private String valueCode;

	@JoinColumn(name = "descriptor_group_id", referencedColumnName = "descriptor_group_id")
	@ManyToOne(optional = false)
	private DescriptorGroup descriptorGroupId;

	@OneToMany(mappedBy = "descriptorValueId")
	private List<DescriptorValueAttr> descriptorValueAttrCollection;

	public DescriptorValue() {
	}

	public DescriptorValue(Long descriptorValueId) {
		this.descriptorValueId = descriptorValueId;
	}

	public Long getDescriptorValueId() {
		return descriptorValueId;
	}

	public void setDescriptorValueId(Long descriptorValueId) {
		this.descriptorValueId = descriptorValueId;
	}

	public String getFullValue() {
		return fullValue;
	}

	public void setFullValue(String fullValue) {
		this.fullValue = fullValue;
	}

	public DescriptorGroup getDescriptorGroupId() {
		return descriptorGroupId;
	}

	public void setDescriptorGroupId(DescriptorGroup descriptorGroupId) {
		this.descriptorGroupId = descriptorGroupId;
	}

	public String getValueCode() {
		return valueCode;
	}

	public void setValueCode(String valueCode) {
		this.valueCode = valueCode;
	}

	public List<DescriptorValueAttr> getDescriptorValueAttrCollection() {
		return descriptorValueAttrCollection;
	}

	public void setDescriptorValueAttrCollection(List<DescriptorValueAttr> descriptorValueAttrCollection) {
		this.descriptorValueAttrCollection = descriptorValueAttrCollection;
	}

}
