package edu.bupt.ssm.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import edu.bupt.ssm.controller.exception.CustomException;
import edu.bupt.ssm.controller.validation.ValidGroup1;
import edu.bupt.ssm.po.ItemsCustom;
import edu.bupt.ssm.po.ItemsQueryVo;
import edu.bupt.ssm.service.ItemsService;

@Controller
@RequestMapping("/items")
public class ItemsController {
	
	@Autowired
	private ItemsService itemsService;
	
	// 商品分类
	@ModelAttribute("itemtypes")
	public Map<String, String> getItemTypes() {

		Map<String, String> itemTypes = new HashMap<String, String>();
		itemTypes.put("101", "数码");
		itemTypes.put("102", "母婴");

		return itemTypes;
	}
	
	// 商品查询
	@RequestMapping("/queryItems")
	public ModelAndView queryItems(HttpServletRequest request,ItemsQueryVo itemsQueryVo)throws Exception{
		
		//调用service查找 数据库，查询商品列表，这里使用静态数据模拟
		List<ItemsCustom> itemsList = itemsService.findItemsList(itemsQueryVo);
		//向list中填充静态数据
		
		//返回ModelAndView
		ModelAndView modelAndView =  new ModelAndView();
		//相当 于request的setAttribut，在jsp页面中通过itemsList取数据
		modelAndView.addObject("itemsList", itemsList);
		
		//指定视图
		//下边的路径，如果在视图解析器中配置jsp路径的前缀和jsp路径的后缀，修改为
		//modelAndView.setViewName("/WEB-INF/jsp/items/itemsList.jsp");
		//上边的路径配置可以不在程序中指定jsp路径的前缀和jsp路径的后缀
		modelAndView.setViewName("items/itemsList");
		
		return modelAndView;
	}
	
	
	// 商品信息修改页面显示
	// @RequestMapping("/editItems")
/*	@RequestMapping(value="/editItems",method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView editItems()throws Exception{
		ModelAndView modelAndView =  new ModelAndView();
		// 调用Service根据id查询商品信息
		ItemsCustom itemsCustom = itemsService.findItemsById(1);
		modelAndView.addObject("itemsCustom", itemsCustom);
		// 商品修改页面
		modelAndView.setViewName("items/editItems");
		return modelAndView;
	}*/
	
	@RequestMapping(value="/editItems",method={RequestMethod.POST,RequestMethod.GET})
	public String editItems(Model model,@RequestParam(value="id") Integer items_id)throws Exception{
		// 调用Service根据id查询商品信息
		ItemsCustom itemsCustom = itemsService.findItemsById(items_id);
		// 判断商品是否为空，若根据id未查到商品，则抛出异常
		if (null == itemsCustom) {
			throw new CustomException("修改的商品信息未存在");
		}
		
		// 通过形参中的Model将model数据传到页面
		model.addAttribute("items", itemsCustom);
		return "items/editItems";
	}
	
	// 商品信息修改提交
	//在需要校验的pojo前边添加@Validated，在需要校验的pojo后边添加BindingResult bindingResult接收校验出错信息
	//注意：@Validated和BindingResult bindingResult是配对出现，并且形参顺序是固定的（一前一后）。
	// @ModelAttribute可以指定pojo回显到页面的request的key
	@RequestMapping("/editItemsSubmit")
	public String editItemsSubmit(Model model, Integer id, @ModelAttribute("items") @Validated(value={ValidGroup1.class}) ItemsCustom itemsCustom, 
			BindingResult bindingResult,MultipartFile items_pic)throws Exception{
		// 获取校验错误信息
		if (bindingResult.hasErrors()) {
			List<ObjectError> allErrors = bindingResult.getAllErrors();
			for (ObjectError objectError : allErrors) {
				System.out.println(objectError.getDefaultMessage());
			}
			// 将错误信息传入到页面中
			model.addAttribute("allErrors", allErrors);
			return "items/editItems";
		}
		
		//注意判断条件
		if (items_pic != null && items_pic.getOriginalFilename() != null && items_pic.getOriginalFilename().length() > 0) {
			// 存储图片的物理路径
			String pic_path = "E:\\pic_tmp\\";
			// 上传图片的原始名称
			String originalFilename = items_pic.getOriginalFilename();
			// 新的图片名称:UUID+扩展名
			String newFilename = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf('.'));
			// 新图片
			File newFile = new File(pic_path + newFilename);
			// 将内存中的数据写入磁盘(指定的物理路径文件)
			items_pic.transferTo(newFile);
			// 将新的图片名称写到itemsCustom中
			itemsCustom.setPic(newFilename);
		}
		
		// 调用Service更新商品信息，页面需要将商品信息传到此方法
		itemsService.updateItems(id, itemsCustom);
		
		// 返回一个成功页面
		// return "redirect:queryItems.action";  // 不用写/items/queryItems.action,因为有类级别的items映射，且要写.action后缀
		//return "forward:queryItems.action";
		// 出错重新到商品修改页面
		return "success";
	}
	
	//查询商品信息，输出json
	//itemsView/{id}里边的{id}表示占位符，通过@PathVariable获取占位符中的参数，
	//如果占位符中的名称和形参名一致，在@PathVariable可以不指定名称
	@RequestMapping("/itemsView/{id}")
	public @ResponseBody ItemsCustom itemsView(@PathVariable("id") Integer id) throws Exception {
		//调用service查询商品信息
		ItemsCustom itemsCustom = itemsService.findItemsById(id);
		return itemsCustom;
	}
	
	
	
	// 批量删除商品信息
	@RequestMapping("/deleteItems")
	public String deleteItems(Integer[] items_id)throws Exception{
		//调用Service进行批量删除
		// ...
		return "success";
	}
	
	// 批量修改商品页面，将商品信息查询出，在页面中可以进行编辑
	@RequestMapping("/editItemsQuery")
	public ModelAndView editItemsQuery(HttpServletRequest request,ItemsQueryVo itemsQueryVo)throws Exception{
		
		//调用service查找 数据库，查询商品列表，这里使用静态数据模拟
		List<ItemsCustom> itemsList = itemsService.findItemsList(itemsQueryVo);
		//向list中填充静态数据
		
		//返回ModelAndView
		ModelAndView modelAndView =  new ModelAndView();
		//相当 于request的setAttribut，在jsp页面中通过itemsList取数据
		modelAndView.addObject("itemsList", itemsList);
		
		//指定视图
		//下边的路径，如果在视图解析器中配置jsp路径的前缀和jsp路径的后缀，修改为
		//modelAndView.setViewName("/WEB-INF/jsp/items/itemsList.jsp");
		//上边的路径配置可以不在程序中指定jsp路径的前缀和jsp路径的后缀
		modelAndView.setViewName("items/editItemsQuery");
		
		return modelAndView;
	}
	
	// 批量修改商品提交
	// 通过ItemsQueryVo批量接收商品信息，存储到其list属性
	@RequestMapping("/editItemsAllSubmit")
	public String editItemsAllSubmit(ItemsQueryVo itemsQueryVo) {
		return "success";
	}
}
