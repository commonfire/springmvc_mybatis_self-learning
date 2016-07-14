package edu.bupt.ssm.mapper;

import java.util.List;

import edu.bupt.ssm.po.ItemsCustom;
import edu.bupt.ssm.po.ItemsQueryVo;

public interface ItemsMapperCustom {
	public List<ItemsCustom> findItemsList(ItemsQueryVo itemsQueryVo) throws Exception;				
}
