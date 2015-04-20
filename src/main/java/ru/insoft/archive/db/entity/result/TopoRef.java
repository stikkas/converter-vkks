package ru.insoft.archive.db.entity.result;

import javax.validation.constraints.NotNull;

/**
 *
 * @author Благодатских С.
 */
public class TopoRef {
// {"rack": 5,"shelf":17}

	@NotNull(message = "Номер стелажа отсутствует")
	Integer rack;
	@NotNull(message = "Номер полки отсутствует")
	Integer shelf;

	public TopoRef(Integer rack, Integer shelf) {
		this.rack = rack;
		this.shelf = shelf;
	}

	public TopoRef() {
	}

	public Integer getRack() {
		return rack;
	}

	public void setRack(Integer rack) {
		this.rack = rack;
	}

	public Integer getShelf() {
		return shelf;
	}

	public void setShelf(Integer shelf) {
		this.shelf = shelf;
	}

}
