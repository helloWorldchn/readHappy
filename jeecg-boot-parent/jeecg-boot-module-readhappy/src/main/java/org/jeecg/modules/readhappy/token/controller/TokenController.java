package org.jeecg.modules.readhappy.token.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.UUIDGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.readhappy.token.entity.Token;
import org.jeecg.modules.readhappy.token.service.ITokenService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: access_tokens
 * @Author: jeecg-boot
 * @Date:   2020-06-11
 * @Version: V1.0
 */
@Api(tags="access_tokens")
@RestController
@RequestMapping("/readHappy/token")
@Slf4j
public class TokenController extends JeecgController<Token, ITokenService> {
	@Autowired
	private ITokenService tokenService;

	@Autowired
	private RedisUtil redisUtil;

	/**
	 * 分页列表查询
	 *
	 * @param token
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "access_tokens-分页列表查询")
	@ApiOperation(value="access_tokens-分页列表查询", notes="access_tokens-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Token token,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Token> queryWrapper = QueryGenerator.initQueryWrapper(token, req.getParameterMap());
		Page<Token> page = new Page<Token>(pageNo, pageSize);
		IPage<Token> pageList = tokenService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param token
	 * @return
	 */
	@AutoLog(value = "access_tokens-添加")
	@ApiOperation(value="access_tokens-添加", notes="access_tokens-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Token token) {
		tokenService.save(token);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param token
	 * @return
	 */
	@AutoLog(value = "access_tokens-编辑")
	@ApiOperation(value="access_tokens-编辑", notes="access_tokens-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Token token) {
		tokenService.updateById(token);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "access_tokens-通过id删除")
	@ApiOperation(value="access_tokens-通过id删除", notes="access_tokens-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		tokenService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "access_tokens-批量删除")
	@ApiOperation(value="access_tokens-批量删除", notes="access_tokens-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.tokenService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "access_tokens-通过id查询")
	@ApiOperation(value="access_tokens-通过id查询", notes="access_tokens-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Token token = tokenService.getById(id);
		if(token==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(token);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param token
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Token token) {
        return super.exportXls(request, token, Token.class, "access_tokens");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Token.class);
    }

	 /**
	  *  生成token，存储在redis中
	  * @return
	  */
    @AutoLog(value = "getAccessToken-获取生成token")
	@ApiOperation(value="getAccessToken-获取生成token", notes="getAccessToken-获取生成token")
	@GetMapping(value = "/getAccessToken")
	public Result<?> getAccessToken() {
    	Map<String,Object> token = new HashMap<>();
    	// uuid
		token.put("token", UUIDGenerator.generate());
		token.put("time",System.currentTimeMillis());
		redisUtil.hmset("token_time", token);
		return Result.ok(token);
	 }


}
