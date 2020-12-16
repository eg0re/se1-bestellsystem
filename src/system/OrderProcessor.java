package system;

import java.util.function.Consumer;

import datamodel.Order;
import datamodel.OrderItem;

final class OrderProcessor implements Components.OrderProcessor {
	private final InventoryManager inventoryManager;

	OrderProcessor(InventoryManager inventoryManager) {
		this.inventoryManager = inventoryManager;
	}

	@Override
	public boolean accept(Order order) {
		return false;
	}

	@Override
	public boolean accept(Order order, Consumer<Order> acceptCode, Consumer<Order> rejectCode,
			Consumer<OrderItem> rejectedOrderItemCode) {
		return false;
	}

	@Override
	public long orderValue(Order order) {
		return 0;
	}

	@Override
	public long vat(long grossValue) {
		return vat(grossValue, 1);
	}

	@Override
	public long vat(long grossValue, int rateIndex) {
		double mwst = 0;
		
		switch(rateIndex) {
		case 1:
			mwst = 1.19;
			break;
		case 2:
			mwst = 1.07;
			break;
		}
		
		long vat = grossValue - (long) (grossValue / mwst);
		return vat;
	}

}
