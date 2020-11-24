package application;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import datamodel.Article;
import datamodel.Customer;
import datamodel.Order;
import datamodel.OrderItem;

/**
 * 
 * Main class to launch the application.
 * 
 * @author svgr64
 *
 */
public class Application_1 {

	private final int printLineWidth = 100;

	/**
	 * main() to launch application.
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args) {

		System.out.println("SE1-Bestellsystem");

		Application_1 application = new Application_1();

	}




	/**
	 * Format long-price in 1/100 (cents) to String using DecimalFormatter and
	 * prepend prefix and append postfix String. For example, 299, "(", ")" ->
	 * "(2,99)"
	 * 
	 * @param price   price as long in 1/100 (cents)
	 * @param prefix  String to prepend before price
	 * @param postfix String to append after price
	 * @return price as String
	 */
	private String fmtPrice(long price, String prefix, String postfix) {
		StringBuffer fmtPriceSB = new StringBuffer();
		if (prefix != null) {
			fmtPriceSB.append(prefix);
		}

		fmtPriceSB = fmtPrice(fmtPriceSB, price);

		if (postfix != null) {
			fmtPriceSB.append(postfix);
		}
		return fmtPriceSB.toString();
	}

	/**
	 * Format long-price in 1/100 (cents) to String using DecimalFormatter. For
	 * example, 299 -> "2,99"
	 * 
	 * @param sb    StringBuffer to which price is added
	 * @param price price as long in 1/100 (cents)
	 * @return StringBuffer with added price
	 */
	private StringBuffer fmtPrice(StringBuffer sb, long price) {
		if (sb == null) {
			sb = new StringBuffer();
		}
		double dblPrice = ((double) price) / 100.0; // convert cent to Euro
		DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("de"))); // rounds double
																										// to 2 digits

		String fmtPrice = df.format(dblPrice); // convert result to String in DecimalFormat
		sb.append(fmtPrice);
		return sb;
	}

	/**
	 * Pad string to minimum width, either right-aligned or left-aligned
	 * 
	 * @param str          String to pad
	 * @param width        minimum width to which result is padded
	 * @param rightAligned flag to chose left- or right-alignment
	 * @return padded String
	 */
	private String pad(String str, int width, boolean rightAligned) {
		String fmtter = (rightAligned ? "%" : "%-") + width + "s";
		String padded = String.format(fmtter, str);
		return padded;
	}


}
