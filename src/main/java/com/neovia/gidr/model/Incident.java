package com.neovia.gidr.model;


/**
 * The Class Incident.
 */
public class Incident {

	/** The rma. */
	private String rma;
	
	/** The part number. */
	private String partNumber;
	
	/** The origin. */
	private String origin;
		
	/**
	 * Instantiates a new incidence.
	 */
	public Incident() {
	}

	/**
	 * Gets the rma.
	 *
	 * @return the rma
	 */
	public String getRma() {
		return rma;
	}

	/**
	 * Sets the rma.
	 *
	 * @param rma the new rma
	 */
	public void setRma(String rma) {
		this.rma = rma;
	}

	/**
	 * Gets the part number.
	 *
	 * @return the part number
	 */
	public String getPartNumber() {
		return partNumber;
	}

	/**
	 * Sets the part number.
	 *
	 * @param partNumber the new part number
	 */
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	/**
	 * Gets the origin.
	 *
	 * @return the origin
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * Sets the origin.
	 *
	 * @param origin the new origin
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	
	
}
