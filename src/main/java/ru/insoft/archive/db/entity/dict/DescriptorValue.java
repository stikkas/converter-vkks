package ru.insoft.archive.db.entity.dict;

import java.io.Serializable;
import java.util.Collection;
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
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Благодатских С.
 */
@Entity
@Table(name = "descriptor_value")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "DescriptorValue.findAll", query = "SELECT d FROM DescriptorValue d"),
	@NamedQuery(name = "DescriptorValue.findByDescriptorValueId", query = "SELECT d FROM DescriptorValue d WHERE d.descriptorValueId = :descriptorValueId"),
	@NamedQuery(name = "DescriptorValue.findByFullValue", query = "SELECT d FROM DescriptorValue d WHERE d.fullValue = :fullValue"),
	@NamedQuery(name = "DescriptorValue.findByShortValue", query = "SELECT d FROM DescriptorValue d WHERE d.shortValue = :shortValue"),
	@NamedQuery(name = "DescriptorValue.findByValueCode", query = "SELECT d FROM DescriptorValue d WHERE d.valueCode = :valueCode"),
	@NamedQuery(name = "DescriptorValue.findBySortOrder", query = "SELECT d FROM DescriptorValue d WHERE d.sortOrder = :sortOrder")})
public class DescriptorValue implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
    @Basic(optional = false)
    @Column(name = "descriptor_value_id")
	private Long descriptorValueId;
	@Basic(optional = false)
    @Column(name = "full_value")
	private String fullValue;
	@Column(name = "short_value")
	private String shortValue;
	@Column(name = "value_code")
	private String valueCode;
	@Basic(optional = false)
    @Column(name = "sort_order")
	private long sortOrder;
	@OneToMany(mappedBy = "parentValueId")
	private Collection<DescriptorValue> descriptorValueCollection;
	@JoinColumn(name = "parent_value_id", referencedColumnName = "descriptor_value_id")
    @ManyToOne
	private DescriptorValue parentValueId;
	@JoinColumn(name = "descriptor_group_id", referencedColumnName = "descriptor_group_id")
    @ManyToOne(optional = false)
	private DescriptorGroup descriptorGroupId;

	public DescriptorValue() {
	}

	public DescriptorValue(Long descriptorValueId) {
		this.descriptorValueId = descriptorValueId;
	}

	public DescriptorValue(Long descriptorValueId, String fullValue, long sortOrder) {
		this.descriptorValueId = descriptorValueId;
		this.fullValue = fullValue;
		this.sortOrder = sortOrder;
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

	public String getShortValue() {
		return shortValue;
	}

	public void setShortValue(String shortValue) {
		this.shortValue = shortValue;
	}

	public String getValueCode() {
		return valueCode;
	}

	public void setValueCode(String valueCode) {
		this.valueCode = valueCode;
	}

	public long getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(long sortOrder) {
		this.sortOrder = sortOrder;
	}

	@XmlTransient
	public Collection<DescriptorValue> getDescriptorValueCollection() {
		return descriptorValueCollection;
	}

	public void setDescriptorValueCollection(Collection<DescriptorValue> descriptorValueCollection) {
		this.descriptorValueCollection = descriptorValueCollection;
	}

	public DescriptorValue getParentValueId() {
		return parentValueId;
	}

	public void setParentValueId(DescriptorValue parentValueId) {
		this.parentValueId = parentValueId;
	}

	public DescriptorGroup getDescriptorGroupId() {
		return descriptorGroupId;
	}

	public void setDescriptorGroupId(DescriptorGroup descriptorGroupId) {
		this.descriptorGroupId = descriptorGroupId;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (descriptorValueId != null ? descriptorValueId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof DescriptorValue)) {
			return false;
		}
		DescriptorValue other = (DescriptorValue) object;
		if ((this.descriptorValueId == null && other.descriptorValueId != null) || (this.descriptorValueId != null && !this.descriptorValueId.equals(other.descriptorValueId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ru.insoft.archive.db.entity.dict.DescriptorValue[ descriptorValueId=" + descriptorValueId + " ]";
	}

}
