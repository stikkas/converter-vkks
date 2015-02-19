package ru.insoft.archive.db.entity.dict;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
	@NamedQuery(name = "DescriptorGroup.findAll", query = "SELECT d FROM DescriptorGroup d"),})
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

	@OneToMany(mappedBy = "descriptorGroupId")
	private Collection<DescriptorValue> descriptorValueCollection;

	public DescriptorGroup() {
	}

	public DescriptorGroup(Long descriptorGroupId) {
		this.descriptorGroupId = descriptorGroupId;
	}

	public DescriptorGroup(Long descriptorGroupId, String groupName) {
		this.descriptorGroupId = descriptorGroupId;
		this.groupName = groupName;

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

	@XmlTransient
	public Collection<DescriptorValue> getDescriptorValueCollection() {
		return descriptorValueCollection;
	}

	public void setDescriptorValueCollection(Collection<DescriptorValue> descriptorValueCollection) {
		this.descriptorValueCollection = descriptorValueCollection;
	}

}
