package gov.pnnl.buildingid;

import com.google.openlocationcode.OpenLocationCode;

/**
 * Represents a UBID code area.
 */
public final class CodeArea extends OpenLocationCode.CodeArea {

	/**
	 * The OLC code area for the center of mass.
	 */
	private final OpenLocationCode.CodeArea centerCodeArea;

	/**
	 * Initializes a new instance of this class.
	 * 
	 * @param southLatitude The latitude coordinate for the southwest corner.
	 * @param westLongitude The longitude coordinate for the southwest corner.
	 * @param northLatitude The latitude coordinate for the northeast corner.
	 * @param eastLongitude The longitude coordinate for the northeast corner.
	 * @param centerCodeArea The OLC code area for the center of mass.
	 */
	public CodeArea(final double southLatitude, final double westLongitude, final double northLatitude, final double eastLongitude, final OpenLocationCode.CodeArea centerCodeArea) {
		super(southLatitude, westLongitude, northLatitude, eastLongitude, centerCodeArea.getLength());

		this.centerCodeArea = centerCodeArea;
	}

	/**
	 * Returns the UBID code for this UBID code area.
	 * 
	 * @return The UBID code for this code area.
	 */
	public Code encode() {
		return Code.encode(
				this.getSouthLatitude(), this.getWestLongitude(), 
				this.getNorthLatitude(), this.getEastLongitude(), 
				this.getCenterCodeArea().getCenterLatitude(), this.getCenterCodeArea().getCenterLongitude(), 
				this.getLength());
	}

	/**
	 * Returns the OLC code area for the center of mass.
	 * 
	 * @return The OLC code area for the center of mass.
	 */
	public OpenLocationCode.CodeArea getCenterCodeArea() {
		return this.centerCodeArea;
	}

	/**
	 * Returns a resized version of this UBID code area, where the latitude and
	 * longitude of the lower left and upper right corners of the OLC bounding
	 * box are moved inwards by dimensions that correspond to half of the height
	 * and width of the OLC grid reference cell for the center of mass.
	 * 
	 * @return The resized version of this UBID code area.
	 */
	public CodeArea resize() {
		final double halfHeight = this.getCenterCodeArea().getLatitudeHeight() / 2d;
		final double halfWidth = this.getCenterCodeArea().getLongitudeWidth() / 2d;

		return new CodeArea(
				this.getSouthLatitude() + halfHeight, this.getWestLongitude() + halfWidth, 
				this.getNorthLatitude() - halfHeight, this.getEastLongitude() - halfWidth, 
				this.getCenterCodeArea());
	}

}
