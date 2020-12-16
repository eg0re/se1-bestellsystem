package datamodel;

public class OrderItem {
	private String description;
	private Article article;
	private int unitsOrdered;

	protected OrderItem(String descr, Article article, int units) {
		this.setDescription(descr);
		this.article = article;
		this.setUnitsOrdered(units);
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String descr) {
		if( descr == null) {
			this.description = "";
		} else {
			this.description = descr;
		}
	}

	public Article getArticle() {
		return article;
	}

	public int getUnitsOrdered() {
		return unitsOrdered;
	}

	public void setUnitsOrdered(int number) {
		if ( number < 0) {
			this.unitsOrdered = 0;
		} else {
			this.unitsOrdered = number;
		}
	}
}
