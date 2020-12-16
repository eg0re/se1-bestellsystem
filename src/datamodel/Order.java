package datamodel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
	private long id;
	private Date date;
	private Customer customer;
	private List<OrderItem> items;

	protected Order(long id, Date date, Customer customer) {
		this.id = id;

		if (date == null) {
			this.date = new Date();
		} else {
			this.date = date;
		}

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
		if (items.contains(item) || item == null) {
			return this;
		}
		this.items.add(item);
		return this;
	}

	public Order removeItem(OrderItem item) {
		this.items.remove(item);
		return this;

	}

	public Order clearItems() {
		this.items.clear();
		return this;
	}
}
