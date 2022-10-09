package com.boyiz.gulimall.product.entity;

import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.boyiz.common.validator.group.AddGroup;
import com.boyiz.common.validator.group.ListValue;
import com.boyiz.common.validator.group.UpdateGroup;
import com.boyiz.common.validator.group.UpdateStatusGroup;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

/**
 * 品牌
 * 
 * @author boyiz
 * @email xianpeoplenocome@gmail.com
 * @date 2022-07-25 16:14:05
 */
@Data

@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 品牌id
	 */
	@NotNull(message = "修改必须指定ID",groups = {UpdateGroup.class})  //JSR303分组校验
	@Null(message = "新增无需指定ID",groups = {AddGroup.class})
	@TableId
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotBlank(message = "品牌名必须提交",groups = {UpdateGroup.class,AddGroup.class})
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotEmpty(groups = {AddGroup.class})
	@URL(message = "logo必须是一个合法的url",groups = {UpdateGroup.class,AddGroup.class})
	private String logo;
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@NotNull(message = "不能为空",groups = {AddGroup.class, UpdateStatusGroup.class})
	@ListValue(vals={0,1},groups = {AddGroup.class, UpdateStatusGroup.class})
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@NotEmpty
	@NotNull(message = "不能为空",groups = {AddGroup.class})
	@Pattern(regexp = "^[a-zA-Z]$",message = "检索首字母必须是字母")
	private String firstLetter;
	/**
	 * 排序
	 */
	@NotNull
	@NotNull(message = "不能为空",groups = {AddGroup.class})
	@Min(value = 0,message = "排序必须大于零")
	private Integer sort;

}
