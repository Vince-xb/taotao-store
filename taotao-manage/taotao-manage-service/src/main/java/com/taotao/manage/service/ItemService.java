package com.taotao.manage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.manage.mapper.ItemMapper;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.pojo.ItemCat;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.pojo.ItemParamItem;

@Service
public class ItemService extends BaseService<Item>{

    @Autowired
    private ItemDescService itemDescService;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ItemCatService itemCatService;
    @Autowired
    private ItemParamItemService itemParamItemService;
    
    public boolean saveItem(Item item, String desc, String itemParams) {
        //初始值
        item.setStatus(1);
        item.setId(null);//出于安全考虑，强制设置ID为null，通过数据库自增长得到。
        item.setCat(this.itemCatService.queryById(item.getCid()).getName());
        Integer count1 = super.save(item);

        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);

        ItemCat cat = this.itemCatService.queryById(item.getCid());
        item.setCat(cat.getName());

        Integer count2 = this.itemDescService.save(itemDesc);
        //保存规格参数数据
        ItemParamItem itemParamItem = new ItemParamItem();
        itemParamItem.setItemId(item.getId());
        itemParamItem.setParamData(itemParams);
        Integer count3 = this.itemParamItemService.save(itemParamItem);
        return count1.intValue() == 1   && count2.intValue() == 1 && count3.intValue() == 1   ;
    }

    public EasyUIResult queryItemList(Integer page, Integer rows) {

        //设置分页参数
        PageHelper.startPage(page, rows);

        //
        Example example = new Example(Item.class);
        //按照创建时间
        example.setOrderByClause("created DESC");
        List<Item> items = this.itemMapper.selectByExample(example);
        PageInfo<Item> pageInfo = new PageInfo<>(items);
        return new EasyUIResult(pageInfo.getTotal(),pageInfo.getList());

    }

    public boolean updateItem(Item item, String desc, String itemParams) {
        //强制设置不能被修改
        item.setStatus(null);
        Integer count1 = super.updateSelective(item);

        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        Integer count2 = this.itemDescService.updateSelective(itemDesc);
        
        //更新规格参数数据
        Integer count3 = this.itemParamItemService.updateItemParamItem(item.getId(),itemParams);
        return count1.intValue() == 1 && count2.intValue() == 1 && count3.intValue() == 1;
    }


}
