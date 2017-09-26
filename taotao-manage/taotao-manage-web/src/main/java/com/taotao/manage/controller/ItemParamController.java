package com.taotao.manage.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.common.bean.EasyUIResult;
import com.taotao.manage.pojo.ItemParam;
import com.taotao.manage.service.ItemCatService;
import com.taotao.manage.service.ItemParamService;


@Controller
@RequestMapping("item/param")
public class ItemParamController {

    @Autowired
    private ItemParamService itemParamService;
    @Autowired
    private ItemCatService itemCatService;
    
    
    /**
     * 根据商品类目ID查询商品类目模板
     * @param itmeCatId
     * @return
     */
    @RequestMapping(value = "{itemCatId}" , method = RequestMethod.GET)
    public ResponseEntity<ItemParam> queryByItemCatId(@PathVariable("itemCatId") Long itmeCatId){
        try {
            ItemParam record = new ItemParam();
            record.setItemCatId(itmeCatId);
            ItemParam itemParam = this.itemParamService.queryOne(record);
            if (null == itemParam) {
                //404
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(itemParam);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    /**
     * 新增规格参数模板
     * @param itemCatId
     * @param paramData
     * @return
     */
    @RequestMapping(value = "{itemCatId}" , method = RequestMethod.POST)
    public ResponseEntity<Void> saveItemParam(@PathVariable("itemCatId") Long itemCatId , @RequestParam("paramData") String paramData){
        try {
            ItemParam itemParam = new ItemParam();
            itemParam.setId(null);
            itemParam.setItemCatId(itemCatId);
            itemParam.setParamData(paramData);
            itemParam.setItemCatName(this.itemCatService.queryById(itemCatId).getName());
            this.itemParamService.save(itemParam);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    
    @RequestMapping(value = "list" , method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryItemParam(
            @RequestParam(value = "page" ,defaultValue = "1") Integer page , 
            @RequestParam(value = "rows " , defaultValue = "30")Integer rows){    
        try {
            EasyUIResult result = this.itemParamService.queryItemList(page , rows);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
