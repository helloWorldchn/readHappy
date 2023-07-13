package org.jeecg.modules.readhappy.article.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: article
 * @Author: jeecg-boot
 * @Date:   2020-06-11
 * @Version: V1.0
 */
@Data
@TableName("read_articles")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="read_articles对象", description="article")
public class Article implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键ID*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键ID")
    private java.lang.Integer id;
	/**文章分类*/
	@Excel(name = "文章分类", width = 15)
    @ApiModelProperty(value = "文章分类")
    private java.lang.Integer artCate;
	/**文章标题*/
	@Excel(name = "文章标题", width = 15)
    @ApiModelProperty(value = "文章标题")
    private java.lang.String artTitle;
	/**文章发表用户id*/
	@Excel(name = "文章发表用户id", width = 15)
    @ApiModelProperty(value = "文章发表用户id")
    private java.lang.Integer artUid;
	/**文章内容*/
	@Excel(name = "文章内容", width = 15)
    @ApiModelProperty(value = "文章内容")
    private java.lang.String artContent;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15)
    @ApiModelProperty(value = "创建时间")
    private java.lang.Integer artCreatetime;
}
