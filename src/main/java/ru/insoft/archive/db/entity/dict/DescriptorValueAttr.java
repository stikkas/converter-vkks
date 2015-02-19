package ru.insoft.archive.db.entity.dict;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Благодатских С.
 */
@Entity
@Table(name = "descriptor_value_attr")
@NamedQueries({
	@NamedQuery(name = "DescriptorValueAttr.findAll", query = "SELECT d FROM DescriptorValueAttr d")})
public class DescriptorValueAttr implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
    @Basic(optional = false)
    @Column(name = "descriptor_value_attr_id")
	private Long descriptorValueAttrId;

	@Column(name = "attr_value")
	private String attrValue;

	@JoinColumn(name = "descriptor_value_id", referencedColumnName = "descriptor_value_id")
    @ManyToOne(optional = false)
	private DescriptorValue descriptorValueId;
	

	public DescriptorValueAttr() {
	}

	public DescriptorValueAttr(Long descriptorValueAttrId) {
		this.descriptorValueAttrId = descriptorValueAttrId;
	}

	public Long getDescriptorValueAttrId() {
		return descriptorValueAttrId;
	}

	public void setDescriptorValueAttrId(Long descriptorValueAttrId) {
		this.descriptorValueAttrId = descriptorValueAttrId;
	}

	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

	public DescriptorValue getDescriptorValueId() {
		return descriptorValueId;
	}

	public void setDescriptorValueId(DescriptorValue descriptorValueId) {
		this.descriptorValueId = descriptorValueId;
	}

}
