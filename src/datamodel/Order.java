package datamodel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
	final long id;
	final Date date;
	final Customer customer;
	final List<OrderItem> items;

	protected Order(long id, Date date, Customer customer) {
		this.id = id;
		this.date = date;
		this.customer = customer;
		this.items = new ArrayList<OrderItem>();
	}

	public long getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	public Customer getCustomer() {
		return customer;
	}

	public Iterable<OrderItem> getItems() {
		return items;
	}

	public int count() {
		return items.size();
	}

	public Order addItem(OrderItem item) {		
		items.add(item);
		return this;
	}

	public Order removeItem(OrderItem item) {
		items.remove(item);
		return this;

	}

	public Order clearItems() {
		items.clear();
		return this;
	}
}
