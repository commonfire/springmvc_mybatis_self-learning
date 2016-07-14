package edu.bupt.ssm.po;

import java.util.List;

public class ItemsQueryVo {
	// 商品信息 
	private Items items;
	
	// 为了可扩展性，对原始po类进行扩展
	private ItemsCustom itemsCustom;
	
	// 用户信息
	private User user;
	
	//private UserCustom userCustom;
	
	// 批量商品信息
	private List<ItemsCustom> itemsList;

	public Items getItems() {
		return items;
	}

	public void setItems(Items items) {
		this.items = items;
	}

	public ItemsCustom getItemsCustom() {
		return itemsCustom;
	}

	public void setItemsCustom(ItemsCustom itemsCustom) {
		this.itemsCustom = itemsCustom;
	}

	public List<ItemsCustom> getItemsList() {
		return itemsList;
	}

	public void setItemsList(List<ItemsCustom> itemsList) {
		this.itemsList = itemsList;
	}
}
