package org.jeecg.modules.readhappy.categories.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: categories
 * @Author: jeecg-boot
 * @Date:   2020-06-11
 * @Version: V1.0
 */
@Data
@TableName("read_categories")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="read_categories对象", description="categories")
public class Categories implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键ID*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键ID")
    private java.lang.Integer id;
	/**父id*/
	@Excel(name = "父id", width = 15)
    @ApiModelProperty(value = "父id")
    private java.lang.Integer catePid;
	/**分类名称*/
	@Excel(name = "分类名称", width = 15)
    @ApiModelProperty(value = "分类名称")
    private java.lang.String cateName;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
    private java.lang.Integer cateOrder;
}
