package edu.bupt.ssm.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import edu.bupt.ssm.controller.exception.CustomException;
import edu.bupt.ssm.mapper.ItemsMapper;
import edu.bupt.ssm.mapper.ItemsMapperCustom;
import edu.bupt.ssm.po.Items;
import edu.bupt.ssm.po.ItemsCustom;
import edu.bupt.ssm.po.ItemsQueryVo;
import edu.bupt.ssm.service.ItemsService;

public class ItemsServiceImpl implements ItemsService{
	
	@Autowired
	private ItemsMapperCustom itemsMapperCustom;
	
	@Autowired
	private ItemsMapper itemsMapper;
	
	@Override
	public List<ItemsCustom> findItemsList(ItemsQueryVo itemsQueryVo) throws Exception {
		// 通过ItemsCustomMapper查询数据库
		return itemsMapperCustom.findItemsList(itemsQueryVo);
	}

	@Override
	public ItemsCustom findItemsById(Integer id) throws Exception {
		// TODO Auto-generated method stub
		Items items = itemsMapper.selectByPrimaryKey(id);
		if (null == items) {
			throw new CustomException("修改的商品信息未存在");
		}
		// 中间对商品信息进行业务处理
		ItemsCustom itemsCustom = null;
		// 将items的属性值拷贝到itemsCustom
		if (items != null) {
			itemsCustom = new ItemsCustom();
			BeanUtils.copyProperties(items, itemsCustom);
		}
		return itemsCustom;
	}

	@Override
	public void updateItems(Integer id, ItemsCustom itemsCustom) throws Exception {
		// TODO Auto-generated method stub
		// 添加业务校验，通常在Service接口对关键参数进行校验
		// 校验id是否为空，抛出异常
		
		// 更新商品信息，使用updateByPrimaryKeyWithBLOBs可以根据id更新所有字段，包括大文本类型
		// updateByPrimaryKeyWithBLOBs要求必须传入id
		itemsCustom.setId(id);
		itemsMapper.updateByPrimaryKeySelective(itemsCustom);
		 
	}
}
