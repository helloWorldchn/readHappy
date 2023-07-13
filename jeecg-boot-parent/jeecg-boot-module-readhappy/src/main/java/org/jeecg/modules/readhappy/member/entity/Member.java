package org.jeecg.modules.readhappy.member.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @Description: member
 * @Author: jeecg-boot
 * @Date:   2020-06-10
 * @Version: V1.0
 */
@Data
@TableName("read_members")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="read_members对象", description="member")
public class Member implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键用户id*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键用户id")
    private java.lang.Integer id;
	/**openid*/
	@Excel(name = "openid", width = 15)
    @ApiModelProperty(value = "openid")
    private java.lang.String uOpenid;
	/**用户昵称*/
	@Excel(name = "用户昵称", width = 15)
    @ApiModelProperty(value = "用户昵称")
    private java.lang.String uName;
	/**用户头像*/
	@Excel(name = "用户头像", width = 15)
    @ApiModelProperty(value = "用户头像")
    private java.lang.String uFace;
	/**用户随机码*/
	@Excel(name = "用户随机码", width = 15)
    @ApiModelProperty(value = "用户随机码")
    private java.lang.String uRandom;
	/**积分*/
	@Excel(name = "积分", width = 15)
    @ApiModelProperty(value = "积分")
    private java.lang.Integer uIntegral;
	/**余额*/
	@Excel(name = "余额", width = 15)
    @ApiModelProperty(value = "余额")
    private java.lang.Integer uRemainder;
	/**用户注册时间*/
	@Excel(name = "用户注册时间", width = 15)
    @ApiModelProperty(value = "用户注册时间")
    private java.lang.Long uRegtime;
	/** 用户发表文章数量 */
	@TableField(exist = false)
	private Integer artCount;
}
