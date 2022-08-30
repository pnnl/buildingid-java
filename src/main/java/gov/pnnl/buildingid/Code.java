package gov.pnnl.buildingid;

import com.google.openlocationcode.OpenLocationCode;

/**
 * Represents a UBID code.
 */
public final class Code {

	/**
	 * Returns the UBID code for the specified center of mass coordinates.
	 * 
	 * @param centerLatitude The latitude coordinate for the center of mass.
	 * @param centerLongitude The longitude coordinate for the center of mass.
	 * @param codeLength The OLC code length.
	 * @return The UBID code for the specified center of mass coordinates.
	 */
	public static Code encode(final double centerLatitude, final double centerLongitude, final int codeLength) {
		return encode(centerLatitude, centerLongitude, centerLatitude, centerLongitude, centerLatitude, centerLongitude, codeLength);
	}

	/**
	 * Returns the UBID code for the specified latitude and longitude coordinates.
	 * 
	 * @param southLatitude The latitude coordinate for the southwest corner.
	 * @param westLongitude The longitude coordinate for the southwest corner.
	 * @param northLatitude The latitude coordinate for the northeast corner.
	 * @param eastLongitude The longitude coordinate for the northeast corner.
	 * @param centerLatitude The latitude coordinate for the center of mass.
	 * @param centerLongitude The longitude coordinate for the center of mass.
	 * @param codeLength The OLC code length.
	 * @return The UBID code for the specified latitude and longitude coordinates.
	 */
	public static Code encode(final double southLatitude, final double westLongitude, final double northLatitude, final double eastLongitude, final double centerLatitude, final double centerLongitude, final int codeLength) {
		final String southwestCode = OpenLocationCode.encode(southLatitude, westLongitude, codeLength);
		final String northeastCode = OpenLocationCode.encode(northLatitude, eastLongitude, codeLength);
		final String centerCode = OpenLocationCode.encode(centerLatitude, centerLongitude, codeLength);

		final OpenLocationCode.CodeArea southwestCodeArea = OpenLocationCode.decode(southwestCode);
		final OpenLocationCode.CodeArea northeastCodeArea = OpenLocationCode.decode(northeastCode);
		final OpenLocationCode.CodeArea centerCodeArea = OpenLocationCode.decode(centerCode);

		final double north = (northeastCodeArea.getNorthLatitude() - centerCodeArea.getNorthLatitude()) / centerCodeArea.getLatitudeHeight();
		final double east = (northeastCodeArea.getEastLongitude() - centerCodeArea.getEastLongitude()) / centerCodeArea.getLongitudeWidth();
		final double south = (centerCodeArea.getSouthLatitude() - southwestCodeArea.getSouthLatitude()) / centerCodeArea.getLatitudeHeight();
		final double west = (centerCodeArea.getWestLongitude() - southwestCodeArea.getWestLongitude()) / centerCodeArea.getLongitudeWidth();

		final String value = String.format("%s-%.0f-%.0f-%.0f-%.0f", centerCode, north, east, south, west);

		return new Code(value);
	}

	/**
	 * The UBID code string.
	 */
	private final String value;

	/**
	 * Initializes a new instance of this class.
	 * 
	 * @param value The UBID code string.
	 */
	public Code(final String value) {
		this.value = value;
	}

	/**
	 * Returns the UBID code area for this UBID code.
	 * 
	 * @return The UBID code area for this UBID code.
	 */
	public CodeArea decode() {
		final String value = this.getValue();

		if (value == null) {
			throw new IllegalStateException("Expected value to be non-null.");
		}

		final String[] strings = value.split("-");

		if (strings.length != 5) {
			throw new IllegalStateException(String.format("Invalid value: Wrong number of components (expected 5, found %d).", strings.length));
		}

		OpenLocationCode.CodeArea centerCodeArea;

		try {
			centerCodeArea = OpenLocationCode.decode(strings[0]);
		} catch (final IllegalStateException e) {
			throw new IllegalStateException(String.format("Invalid value: Invalid OLC code: %s", strings[0]), e);
		}

		int north;
		int east;
		int south;
		int west;

		try {
			north = Integer.parseInt(strings[1]);
		} catch (final NumberFormatException e) {
			throw new IllegalStateException(String.format("Invalid value: Invalid north component: %s", strings[1]), e);
		}

		try {
			east = Integer.parseInt(strings[2]);
		} catch (final NumberFormatException e) {
			throw new IllegalStateException(String.format("Invalid value: Invalid east component: %s", strings[2]), e);
		}

		try {
			south = Integer.parseInt(strings[3]);
		} catch (final NumberFormatException e) {
			throw new IllegalStateException(String.format("Invalid value: Invalid south component: %s", strings[3]), e);
		}

		try {
			west = Integer.parseInt(strings[4]);
		} catch (final NumberFormatException e) {
			throw new IllegalStateException(String.format("Invalid value: Invalid west component: %s", strings[4]), e);
		}

		final double southLatitude = centerCodeArea.getSouthLatitude() - (centerCodeArea.getLatitudeHeight() * (double) south);
		final double westLongitude = centerCodeArea.getWestLongitude() - (centerCodeArea.getLongitudeWidth() * (double) west);
		final double northLatitude = centerCodeArea.getNorthLatitude() + (centerCodeArea.getLatitudeHeight() * (double) north);
		final double eastLongitude = centerCodeArea.getEastLongitude() + (centerCodeArea.getLongitudeWidth() * (double) east);

		return new CodeArea(southLatitude, westLongitude, northLatitude, eastLongitude, centerCodeArea);
	}

	/**
	 * Returns the UBID code string.
	 * 
	 * @return The UBID code string.
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * Is this UBID code valid?
	 * 
	 * @return True if this UBID code is valid. Otherwise, false.
	 */
	public boolean isValid() {
		try {
			this.decode();

			return true;
		} catch (final Exception e) {
			return false;
		}
	}

}
