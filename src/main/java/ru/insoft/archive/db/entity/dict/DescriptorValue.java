package ru.insoft.archive.db.entity.dict;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
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
			query = "SELECT d FROM DescriptorValue d LEFT JOIN d.descriptorGroup g WHERE g.groupCode = :code"),
	@NamedQuery(name = "DescriptorValue.maxSortOrderByGroup",
			query = "SELECT MAX(d.sortOrder) FROM DescriptorValue d LEFT JOIN d.descriptorGroup g WHERE g.groupCode = :code"),
	@NamedQuery(name = "DescriptorValue.lastCodeByGroup",
			query = "SELECT d.valueCode FROM DescriptorValue d LEFT JOIN d.descriptorGroup g WHERE g.groupCode = :code ORDER BY d.valueCode DESC")
})
public class DescriptorValue implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "descGenerator", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "descGenerator", sequenceName = "SEQ_DESCRIPTOR_VALUE", allocationSize = 1)
	@Column(name = "descriptor_value_id")
	private Long descriptorValueId;

	@Basic(optional = false)
	@Column(name = "full_value")
	private String fullValue;

	@Basic(optional = false)
	@Column(name = "descriptor_group_id")
	private Long descriptorGroupId;

	@Basic(optional = false)
	@Column(name = "sort_order")
	private Integer sortOrder;

	@Column(name = "value_code")
	private String valueCode;

	@JoinColumn(name = "descriptor_group_id", referencedColumnName = "descriptor_group_id",
			insertable = false, updatable = false)
	@ManyToOne
	private DescriptorGroup descriptorGroup;

	@OneToMany(mappedBy = "descriptorValueId")
	private List<DescriptorValueAttr> descriptorValueAttrCollection;

	public DescriptorValue() {
	}

	public DescriptorValue(Long descriptorGroupId, String fullValue, String valueCode, Integer sortOrder) {
		this.descriptorGroupId = descriptorGroupId;
		this.fullValue = fullValue;
		this.valueCode = valueCode;
		this.sortOrder = sortOrder;
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

	public DescriptorGroup getDescriptorGroup() {
		return descriptorGroup;
	}

	public void setDescriptorGroup(DescriptorGroup descriptorGroup) {
		this.descriptorGroup = descriptorGroup;
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

	public Long getDescriptorGroupId() {
		return descriptorGroupId;
	}

	public void setDescriptorGroupId(Long descriptorGroupId) {
		this.descriptorGroupId = descriptorGroupId;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

}
