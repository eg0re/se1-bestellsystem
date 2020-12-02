package system;

import java.util.function.Consumer;

import datamodel.Order;
import datamodel.OrderItem;

final class OrderProcessor implements Components.OrderProcessor {

	OrderProcessor(InventoryManager inventoryManager) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean accept(Order order) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean accept(Order order, Consumer<Order> acceptCode, Consumer<Order> rejectCode,
			Consumer<OrderItem> rejectedOrderItemCode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long orderValue(Order order) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long vat(long grossValue) {
		return vat(grossValue, 1);
	}

	@Override
	public long vat(long grossValue, int rateIndex) {
		double mwst = 1.19;
		
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
