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
@Table(name = "core_subsystem")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "CoreSubsystem.findAll", query = "SELECT c FROM CoreSubsystem c"),
	@NamedQuery(name = "CoreSubsystem.findBySubsystemNumber", query = "SELECT c FROM CoreSubsystem c WHERE c.subsystemNumber = :subsystemNumber"),
	@NamedQuery(name = "CoreSubsystem.findBySubsystemName", query = "SELECT c FROM CoreSubsystem c WHERE c.subsystemName = :subsystemName"),
	@NamedQuery(name = "CoreSubsystem.findBySubsystemCode", query = "SELECT c FROM CoreSubsystem c WHERE c.subsystemCode = :subsystemCode")})
public class CoreSubsystem implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
    @Basic(optional = false)
    @Column(name = "subsystem_number")
	private Long subsystemNumber;
	@Basic(optional = false)
    @Column(name = "subsystem_name")
	private String subsystemName;
	@Basic(optional = false)
    @Column(name = "subsystem_code")
	private String subsystemCode;
	@OneToMany(mappedBy = "subsystemNumber")
	private Collection<DescriptorGroup> descriptorGroupCollection;

	public CoreSubsystem() {
	}

	public CoreSubsystem(Long subsystemNumber) {
		this.subsystemNumber = subsystemNumber;
	}

	public CoreSubsystem(Long subsystemNumber, String subsystemName, String subsystemCode) {
		this.subsystemNumber = subsystemNumber;
		this.subsystemName = subsystemName;
		this.subsystemCode = subsystemCode;
	}

	public Long getSubsystemNumber() {
		return subsystemNumber;
	}

	public void setSubsystemNumber(Long subsystemNumber) {
		this.subsystemNumber = subsystemNumber;
	}

	public String getSubsystemName() {
		return subsystemName;
	}

	public void setSubsystemName(String subsystemName) {
		this.subsystemName = subsystemName;
	}

	public String getSubsystemCode() {
		return subsystemCode;
	}

	public void setSubsystemCode(String subsystemCode) {
		this.subsystemCode = subsystemCode;
	}

	@XmlTransient
	public Collection<DescriptorGroup> getDescriptorGroupCollection() {
		return descriptorGroupCollection;
	}

	public void setDescriptorGroupCollection(Collection<DescriptorGroup> descriptorGroupCollection) {
		this.descriptorGroupCollection = descriptorGroupCollection;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (subsystemNumber != null ? subsystemNumber.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof CoreSubsystem)) {
			return false;
		}
		CoreSubsystem other = (CoreSubsystem) object;
		if ((this.subsystemNumber == null && other.subsystemNumber != null) || (this.subsystemNumber != null && !this.subsystemNumber.equals(other.subsystemNumber))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ru.insoft.archive.db.entity.dict.CoreSubsystem[ subsystemNumber=" + subsystemNumber + " ]";
	}

}
