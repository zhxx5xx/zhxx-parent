package com.zhxx.cart.pojo;

public class Cart {
	private long id;
	private long price;
	private String title;
	private int num;
	private String[] images;
	
	public Cart() {
		super();
	}
	public Cart(long id, long price, String title, int num, String[] images) {
		super();
		this.id = id;
		this.price = price;
		this.title = title;
		this.num = num;
		this.images = images;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String[] getImages() {
		return images;
	}
	public void setImages(String[] images) {
		this.images = images;
	}
	
	
	
	
	
	
	

}
