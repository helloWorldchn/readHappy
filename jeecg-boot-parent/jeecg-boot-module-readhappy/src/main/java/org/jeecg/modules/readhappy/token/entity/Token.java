package org.jeecg.modules.readhappy.token.entity;

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
 * @Description: access_tokens
 * @Author: jeecg-boot
 * @Date:   2020-06-11
 * @Version: V1.0
 */
@Data
@TableName("read_access_tokens")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="read_access_tokens对象", description="access_tokens")
public class Token implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键id")
    private java.lang.Integer id;
	/**token*/
	@Excel(name = "token", width = 15)
    @ApiModelProperty(value = "token")
    private java.lang.String token;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15)
    @ApiModelProperty(value = "创建时间")
    private java.lang.Integer time;
}
