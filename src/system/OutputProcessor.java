package system;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import datamodel.Customer;
import datamodel.Order;
import datamodel.OrderItem;

final class OutputProcessor implements Components.OutputProcessor {
	
	private final int printLineWidth = 95;
	private final InventoryManager inventoryManager;
	private final OrderProcessor orderProcessor;

	OutputProcessor(InventoryManager inventoryManager, OrderProcessor orderProcessor) {
		this.inventoryManager = inventoryManager;
		this.orderProcessor = orderProcessor;
	}

	@Override
	/**
	 * Print orders to System.out in format (example): ------------- #5234968294,
	 * Eric's Bestellung: 1x Kanne 20,00 EUR #8592356245, Eric's Bestellung: 4x
	 * Teller, 8x Becher, 4x Tassen 49,84 EUR #3563561357, Anne's Bestellung: 1x
	 * Kanne aus Porzellan 20,00 EUR #6135735635, Nadine Ulla's Bestellung: 12x
	 * Teller blau/weiss Ker.. 77,88 EUR #4835735356, Timo's Bestellung: 1x
	 * Kaffeemaschine, 6x Tasse 47,93 EUR #6399437335, Sandra's Bestellung: 1x
	 * Teekocher, 4x Becher, 4x Te.. 51,91 EUR ------------- Gesamtwert aller
	 * Bestellungen: 267,56 EUR
	 * 
	 * |<----------------------------<printLineWidth>----------------------------->|
	 * 
	 * @param orders list of orders to print
	 * 
	 */
	public void printOrders(List<Order> orders, boolean printVAT) {

		
		StringBuffer sbAllOrders = new StringBuffer("-------------");
		StringBuffer sbLineItem = new StringBuffer();

		/*
		 * Insert code to print orders with all order items:
		 */
		long allsum = 0;
		for (Order o : orders) {
			long sum = 0;
			
			String itemsinlist = "";
			for (OrderItem i : o.getItems()) {
				sum += i.getArticle().getUnitPrice() * i.getUnitsOrdered();
				int x = i.getUnitsOrdered();
				itemsinlist += ", " + x + "x " + i.getDescription();
				itemsinlist = itemsinlist.replaceAll(", $", "");
				itemsinlist = itemsinlist.replaceAll("^,", "");	
			}
			Customer customer = o.getCustomer();
			String customerName = splitName( customer, customer.getFirstName() + " " + customer.getLastName());
			allsum += sum;
			sbLineItem = fmtLine(
					"#" + o.getId() + ", " + customerName + "'s Bestellung:" + itemsinlist,
					fmtPrice(sum, "EUR", 14), printLineWidth);
			sbAllOrders.append("\n");
			sbAllOrders.append(sbLineItem);
			
		}

		// convert price (long: 2345 in cent) to String of length 14, right-aligned -> "
		// 23,45 EUR"
		String fmtPrice1 = fmtPrice(2345, "EUR", 14);

		// format line item with left-aligned part1 and right-aligned-part2 of total
		// length printLineWidth
		// and append to sbLineItem StringBuffer
		// sbLineItem = fmtLine("Erste Bestellung: einzelne Bestellpositionen
		// (orderItems)", fmtPrice1, printLineWidth);
		// Erste Bestellung: einzelne Bestellpositionen (orderItems) 23,45 EUR

		// append sbLineItem to sbAllOrders StringBuffer where all orders are collected

		// format another line for second order
		// String fmtPrice2 = fmtPrice(67890, "EUR", 14);
		// sbLineItem = fmtLine("Zweite Bestellung: einzelne Bestellpositionen
		// (orderItems)", fmtPrice2, printLineWidth);
		// Zweite Bestellung: einzelne Bestellpositionen (orderItems) 678,90 EUR

		// calculate total price
		String fmtPriceTotal = pad(fmtPrice(allsum, "", " EUR"), 14, true);

		// append final line with totals
		sbAllOrders.append("\n").append(fmtLine("-------------", "-------------", printLineWidth)).append("\n")
				.append(fmtLine("Gesamtwert aller Bestellungen:", fmtPriceTotal, printLineWidth));
		
		long vat = orderProcessor.vat(allsum);
		
		if(printVAT) {
			String vatf = pad(fmtPrice(vat, "", " EUR"), 14, true);
			sbAllOrders.append("\n").append(fmtLine("Im Gesamtbetrag enthaltene Mehrwertsteuer (19%):", vatf , printLineWidth));
		}
		
		

		// print sbAllOrders StringBuffer with all output to System.out
		System.out.println(sbAllOrders.toString());
	}

	@Override
	public void printInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	/**
	 * Format long-price in 1/100 (cents) to String using DecimalFormatter. Append
	 * currency. For example, 299, "EUR" -> "2,99 EUR"
	 * 
	 * @param price    price as long in 1/100 (cents)
	 * @param currency currency as String, e.g. "EUR"
	 * @return price as String with currency and padded to minimum width
	 */
	public String fmtPrice(long price, String currency) {
		String fmtPrice = pad(fmtPrice(price, "", " " + currency), 14, true);
		return fmtPrice;
	}


	@Override
	/**
	 * Format long-price in 1/100 (cents) to String using DecimalFormatter, add
	 * currency and pad to minimum width right-aligned. For example, 299, "EUR", 12
	 * -> " 2,99 EUR"
	 * 
	 * @param price    price as long in 1/100 (cents)
	 * @param currency currency as String, e.g. "EUR"
	 * @param width    minimum width to which result is padded
	 * @return price as String with currency and padded to minimum width
	 */
	public String fmtPrice(long price, String currency, int width) {
		String fmtPrice = pad(fmtPrice(price, "", " " + currency), 14, true);
		return fmtPrice;
	}

	@Override
	/**
	 * Format line to a left-aligned part followed by a right-aligned part padded to
	 * a minimum width. For example:
	 * 
	 * <left-aligned part> <> <right-aligned part>
	 * 
	 * "#5234968294, Eric's Bestellung: 1x Kanne 20,00 EUR (3,19 MwSt)"
	 * 
	 * |<-------------------------------<width>--------------------------------->|
	 * 
	 * @param leftStr    left-aligned String
	 * @param rightStr   right-aligned String
	 * @param totalWidth minimum width to which result is padded
	 * @return String with left- and right-aligned parts padded to minimum width
	 */
	public StringBuffer fmtLine(String leftStr, String rightStr, int totalWidth) {
		StringBuffer sb = new StringBuffer(leftStr);
		int shiftable = 0; // leading spaces before first digit
		for (int i = 1; rightStr.charAt(i) == ' ' && i < rightStr.length(); i++) {
			shiftable++;
		}
		final int tab1 = totalWidth - rightStr.length() + 1; // - ( seperator? 3 : 0 );
		int sbLen = sb.length();
		int excess = sbLen - tab1 + 1;
		int shift2 = excess - Math.max(0, excess - shiftable);
		if (shift2 > 0) {
			rightStr = rightStr.substring(shift2, rightStr.length());
			excess -= shift2;
		}
		if (excess > 0) {
			switch (excess) {
			case 1:
				sb.delete(sbLen - excess, sbLen);
				break;
			case 2:
				sb.delete(sbLen - excess - 2, sbLen);
				sb.append("..");
				break;
			default:
				sb.delete(sbLen - excess - 3, sbLen);
				sb.append("...");
				break;
			}
		}
		String strLineItem = String.format("%-" + (tab1 - 1) + "s%s", sb.toString(), rightStr);
		sb.setLength(0);
		sb.append(strLineItem);
		return sb;
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

    /**
     * Split single-String name to first- and last name and set to the customer object,
     * e.g. single-String "Eric Meyer" is split into "Eric" and "Meyer".
     *
     * @param customer object for which first- and lastName are set
     * @param name     single-String name that is split into first- and last name
     * @return returns single-String name extracted from customer object
     */
	@Override
	public String splitName( Customer customer, String name ) {
        String lastName = "";
        String firstName = "";
		
        boolean comma = name.contains(",");
        String[] splittednames = name.split("[, ]+");

        if(comma) {
            lastName = splittednames[0];

            for(int i = 1; i < splittednames.length; i++) {
                firstName += splittednames[i] + " ";
            }
        } else {
            lastName = splittednames[splittednames.length - 1];

            for(int i = 0; i < splittednames.length - 1; i++) {
                firstName += splittednames[i] + " ";
            }
        }

        firstName = firstName.trim();

        customer.setFirstName(firstName);
        customer.setLastName(lastName);

        return singleName(customer);
	}

    /**
     * Returns single-String name obtained from first- and lastName attributes of
     * Customer object, e.g. "Eric", "Meyer" returns single-String "Meyer, Eric".
     *
     * @param customer object referred to
     * @return name derived from first- and lastName attributes
     */
	@Override
	public String singleName( Customer customer ) {
		return customer.getLastName() + ", " + customer.getFirstName();
	}

	

}
