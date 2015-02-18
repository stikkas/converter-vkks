package ru.insoft.archive.db.entity.dict;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "descriptor_group")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "DescriptorGroup.findAll", query = "SELECT d FROM DescriptorGroup d"),
	@NamedQuery(name = "DescriptorGroup.findByDescriptorGroupId", query = "SELECT d FROM DescriptorGroup d WHERE d.descriptorGroupId = :descriptorGroupId"),
	@NamedQuery(name = "DescriptorGroup.findByGroupName", query = "SELECT d FROM DescriptorGroup d WHERE d.groupName = :groupName"),
	@NamedQuery(name = "DescriptorGroup.findByGroupCode", query = "SELECT d FROM DescriptorGroup d WHERE d.groupCode = :groupCode"),
	@NamedQuery(name = "DescriptorGroup.findBySortOrder", query = "SELECT d FROM DescriptorGroup d WHERE d.sortOrder = :sortOrder"),
	@NamedQuery(name = "DescriptorGroup.findByIsSystem", query = "SELECT d FROM DescriptorGroup d WHERE d.isSystem = :isSystem"),
	@NamedQuery(name = "DescriptorGroup.findByIsHierarchical", query = "SELECT d FROM DescriptorGroup d WHERE d.isHierarchical = :isHierarchical"),
	@NamedQuery(name = "DescriptorGroup.findByShortValueSupported", query = "SELECT d FROM DescriptorGroup d WHERE d.shortValueSupported = :shortValueSupported"),
	@NamedQuery(name = "DescriptorGroup.findByAlphabeticSort", query = "SELECT d FROM DescriptorGroup d WHERE d.alphabeticSort = :alphabeticSort")})
public class DescriptorGroup implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
    @Basic(optional = false)
    @Column(name = "descriptor_group_id")
	private Long descriptorGroupId;
	@Basic(optional = false)
    @Column(name = "group_name")
	private String groupName;
	@Column(name = "group_code")
	private String groupCode;
	@Basic(optional = false)
    @Column(name = "sort_order")
	private int sortOrder;
	@Basic(optional = false)
    @Column(name = "is_system")
	private boolean isSystem;
	@Basic(optional = false)
    @Column(name = "is_hierarchical")
	private boolean isHierarchical;
	@Basic(optional = false)
    @Column(name = "short_value_supported")
	private boolean shortValueSupported;
	@Basic(optional = false)
    @Column(name = "alphabetic_sort")
	private boolean alphabeticSort;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "descriptorGroupId")
	private Collection<DescriptorValue> descriptorValueCollection;
	@JoinColumn(name = "subsystem_number", referencedColumnName = "subsystem_number")
    @ManyToOne
	private CoreSubsystem subsystemNumber;

	public DescriptorGroup() {
	}

	public DescriptorGroup(Long descriptorGroupId) {
		this.descriptorGroupId = descriptorGroupId;
	}

	public DescriptorGroup(Long descriptorGroupId, String groupName, int sortOrder, boolean isSystem, boolean isHierarchical, boolean shortValueSupported, boolean alphabeticSort) {
		this.descriptorGroupId = descriptorGroupId;
		this.groupName = groupName;
		this.sortOrder = sortOrder;
		this.isSystem = isSystem;
		this.isHierarchical = isHierarchical;
		this.shortValueSupported = shortValueSupported;
		this.alphabeticSort = alphabeticSort;
	}

	public Long getDescriptorGroupId() {
		return descriptorGroupId;
	}

	public void setDescriptorGroupId(Long descriptorGroupId) {
		this.descriptorGroupId = descriptorGroupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	public boolean getIsSystem() {
		return isSystem;
	}

	public void setIsSystem(boolean isSystem) {
		this.isSystem = isSystem;
	}

	public boolean getIsHierarchical() {
		return isHierarchical;
	}

	public void setIsHierarchical(boolean isHierarchical) {
		this.isHierarchical = isHierarchical;
	}

	public boolean getShortValueSupported() {
		return shortValueSupported;
	}

	public void setShortValueSupported(boolean shortValueSupported) {
		this.shortValueSupported = shortValueSupported;
	}

	public boolean getAlphabeticSort() {
		return alphabeticSort;
	}

	public void setAlphabeticSort(boolean alphabeticSort) {
		this.alphabeticSort = alphabeticSort;
	}

	@XmlTransient
	public Collection<DescriptorValue> getDescriptorValueCollection() {
		return descriptorValueCollection;
	}

	public void setDescriptorValueCollection(Collection<DescriptorValue> descriptorValueCollection) {
		this.descriptorValueCollection = descriptorValueCollection;
	}

	public CoreSubsystem getSubsystemNumber() {
		return subsystemNumber;
	}

	public void setSubsystemNumber(CoreSubsystem subsystemNumber) {
		this.subsystemNumber = subsystemNumber;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (descriptorGroupId != null ? descriptorGroupId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof DescriptorGroup)) {
			return false;
		}
		DescriptorGroup other = (DescriptorGroup) object;
		if ((this.descriptorGroupId == null && other.descriptorGroupId != null) || (this.descriptorGroupId != null && !this.descriptorGroupId.equals(other.descriptorGroupId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ru.insoft.archive.db.entity.dict.DescriptorGroup[ descriptorGroupId=" + descriptorGroupId + " ]";
	}

}
