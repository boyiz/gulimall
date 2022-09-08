package com.boyiz.gulimall.search.vo;

import lombok.Data;

import java.util.List;

/**
 * 封装所有可能传过来的查询条件
 * 过滤条件：
 *  hasStock=0/1
 *  skuPrice=1_500 / _500 /500_
 *  attrs=1_7寸:6寸&attrs=2_8G:6G
 */
@Data
public class SearchParam {

    /**
     * 页面传递过来的全文匹配关键字
     */
    private String keyword;

    /**
     * 品牌id,可以多选
     */
    private List<Long> brandId;

    /**
     * 三级分类id
     */
    private Long catalog3Id;

    /**
     * 排序条件：
     *  sort=hotscore_desc/asc
     *  sort=price_desc/asc
     *  sort=saleCount_desc/asc
     */
    private String sort;

    /**
     * 是否显示有货
     */
    private Integer hasStock;

    /**
     * 价格区间查询
     */
    private String skuPrice;

    /**
     * 按照属性进行筛选
     */
    private List<String> attrs;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 原生的所有查询条件
     */
    private String _queryString;


}
