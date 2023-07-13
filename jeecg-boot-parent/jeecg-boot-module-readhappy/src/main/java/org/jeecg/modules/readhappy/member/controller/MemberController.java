package org.jeecg.modules.readhappy.member.controller;

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

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.readhappy.article.entity.Article;
import org.jeecg.modules.readhappy.article.service.IArticleService;
import org.jeecg.modules.readhappy.article.service.impl.ArticleServiceImpl;
import org.jeecg.modules.readhappy.member.entity.Member;
import org.jeecg.modules.readhappy.member.service.IMemberService;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: member
 * @Author: jeecg-boot
 * @Date:   2020-06-10
 * @Version: V1.0
 */
@Api(tags="member")
@RestController
@RequestMapping("/readHappy/member")
@Slf4j
public class MemberController extends JeecgController<Member, IMemberService> {
	@Autowired
	private IMemberService memberService;

	@Autowired
	private IArticleService articleService;

	@Autowired
	private RedisUtil redisUtil;

	@Value("${wx.appId}")
	private String appId;

	@Value("${wx.appSecret}")
	private String appSecret;

	@Value("${wx.grantType}")
	private String grantType;

	@Value("${wx.requestUrl}")
	private String requestUrl;


	/**
	 * 分页列表查询
	 *
	 * @param member
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "member-分页列表查询")
	@ApiOperation(value="member-分页列表查询", notes="member-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Member member,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Member> queryWrapper = QueryGenerator.initQueryWrapper(member, req.getParameterMap());
		Page<Member> page = new Page<Member>(pageNo, pageSize);
		IPage<Member> pageList = memberService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param member
	 * @return
	 */
	@AutoLog(value = "member-添加")
	@ApiOperation(value="member-添加", notes="member-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Member member) {
		memberService.save(member);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param member
	 * @return
	 */
	@AutoLog(value = "member-编辑")
	@ApiOperation(value="member-编辑", notes="member-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Member member) {
		memberService.updateById(member);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "member-通过id删除")
	@ApiOperation(value="member-通过id删除", notes="member-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		memberService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "member-批量删除")
	@ApiOperation(value="member-批量删除", notes="member-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.memberService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "member-通过id查询")
	@ApiOperation(value="member-通过id查询", notes="member-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Member member = memberService.getById(id);
		if(member==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(member);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param member
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Member member) {
        return super.exportXls(request, member, Member.class, "member");
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
        return super.importExcel(request, response, Member.class);
    }

	 /**
	  * member-会员登录
	  * @param openid
	  * @param name
	  * @param face
	  * @return
	  */
	 @AutoLog(value = "member-会员登录")
	 @ApiOperation(value="member-会员登录", notes="member-会员登录")
	 @PostMapping(value = "/login")
	 public Result<?> login(@RequestParam(name = "openid", required = true) String openid,
							@RequestParam(name = "name", required = true) String name,
							@RequestParam(name = "face", required = true) String face,
	 						@RequestParam(name = "sign", required = true) String sign) {

	 	//验证签名
		final Map<Object, Object> token_time = redisUtil.hmget("token_time");
		String tokenTime = token_time.get("token").toString() + token_time.get("time").toString();
		String[] signArray = StrUtil.split(sign, "-");

		if (StrUtil.isEmpty(sign)) {
			return Result.error(10001 + "验签失败！");
		}
		 if (signArray.length != 2) {
			 return Result.error(10002 + "验签失败！");
		 }
		 if (!signArray[1].equals(token_time.get("token").toString())) {
			 return Result.error(10003 + "验签失败！");
		 }
		 String signMd5 = DigestUtil.md5Hex(tokenTime);
		 if (!signArray[0].equals(signMd5)) {
			 return Result.error(10004 + "验签失败！");
		 }

	 	// 根据openid检验是否存在用户，若不存在则注册
		QueryWrapper<Member> wrapper = new QueryWrapper<>();
		wrapper.eq("u_openid",openid);
		Member member = memberService.getOne(wrapper);
		if(member == null) {
		 	// 注册新用户
			Member memberReg = new Member();
			memberReg.setUOpenid(openid).setUName(name).setUFace(face).setURegtime(System.currentTimeMillis()/1000);
			memberService.save(memberReg);
			return Result.ok(memberReg);
		}
		return Result.ok(member);
	 }


	 @AutoLog(value = "member-code换取openid")
	 @ApiOperation(value="member-code换取openid", notes="member-code换取openid")
	 @PostMapping(value = "/codeToSession")
	 public Result<?> codeToSession(@RequestParam(name = "code",required = true) String code) {

	 	String url = this.requestUrl + "?appid=" + appId + "&secret=" + appSecret + "&js_code=" + code + "&grant_type=" + grantType;

	 	// String url = "https://api.weixin.qq.com/sns/jscode2session?appId=wxb5ff3fefc1044a9f&secret=e29913a5593078c165c7afb8c71a0ff8&js_code=08148FtP1CrSk71Wf5uP1bWttP148FtL&grant_type=authorization_code"
		final String data = HttpUtil.get(url);
		Map<String,Object> json = JSONUtil.toBean(data, HashMap.class);
		// 形如{"session_key":"oHEf70GDT07QwFLNLr3vuQ==","openId":"ongS65SyBWyWtMkDM-ZaiXTpuusE"}的字符串
		 if(json.isEmpty()){
		 	return Result.error("未找到数据");
		 }
		 return Result.ok(json);
	 }

	 /**
	  *
	  * @param uid
	  * @return
	  */
	 @AutoLog(value = "member-会员信息统计")
	 @ApiOperation(value="member-会员信息统计", notes="member-会员信息统计")
	 @PostMapping(value = "/info")
	 public Result<?> info(@RequestParam(name = "uid",required = true) String uid) {

		Member member = memberService.getById(uid);
		if (member == null){
			return Result.error("未找到用户数据");
		}
		QueryWrapper<Article> query = new QueryWrapper<>();
		query.eq("art_uid",uid);
		int count = articleService.count(query);
		member.setArtCount(count);
	 	return Result.ok(member);
	 }
}
