package org.jeecg.modules.readhappy.article.controller;

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

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.readhappy.article.entity.Article;
import org.jeecg.modules.readhappy.article.service.IArticleService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.readhappy.member.entity.Member;
import org.jeecg.modules.readhappy.member.service.IMemberService;
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
 * @Description: article
 * @Author: jeecg-boot
 * @Date:   2020-06-11
 * @Version: V1.0
 */
@Api(tags="article")
@RestController
@RequestMapping("/readHappy/article")
@Slf4j
public class ArticleController extends JeecgController<Article, IArticleService> {
	 @Autowired
	 private IArticleService articleService;

	 @Autowired
	 private IMemberService memberService;

	 /**
	  * 分页列表查询
	  *
	  * @param article
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "article-分页列表查询")
	 @ApiOperation(value = "article-分页列表查询", notes = "article-分页列表查询")
	 @GetMapping(value = "/list")
	 public Result<?> queryPageList(Article article,
									@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
									@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
									HttpServletRequest req) {
		 QueryWrapper<Article> queryWrapper = QueryGenerator.initQueryWrapper(article, req.getParameterMap());
		 Page<Article> page = new Page<Article>(pageNo, pageSize);
		 IPage<Article> pageList = articleService.page(page, queryWrapper);
		 return Result.ok(pageList);
	 }

	 /**
	  * 添加
	  *
	  * @param article
	  * @return
	  */
	 @AutoLog(value = "article-添加")
	 @ApiOperation(value = "article-添加", notes = "article-添加")
	 @PostMapping(value = "/add")
	 public Result<?> add(@RequestBody Article article) {
		 articleService.save(article);
		 return Result.ok("添加成功！");
	 }

	 /**
	  * 编辑
	  *
	  * @param article
	  * @return
	  */
	 @AutoLog(value = "article-编辑")
	 @ApiOperation(value = "article-编辑", notes = "article-编辑")
	 @PutMapping(value = "/edit")
	 public Result<?> edit(@RequestBody Article article) {
		 articleService.updateById(article);
		 return Result.ok("编辑成功!");
	 }

	 /**
	  * 通过id删除
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "article-通过id删除")
	 @ApiOperation(value = "article-通过id删除", notes = "article-通过id删除")
	 @DeleteMapping(value = "/delete")
	 public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
		 articleService.removeById(id);
		 return Result.ok("删除成功!");
	 }

	 /**
	  * 批量删除
	  *
	  * @param ids
	  * @return
	  */
	 @AutoLog(value = "article-批量删除")
	 @ApiOperation(value = "article-批量删除", notes = "article-批量删除")
	 @DeleteMapping(value = "/deleteBatch")
	 public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		 this.articleService.removeByIds(Arrays.asList(ids.split(",")));
		 return Result.ok("批量删除成功!");
	 }

	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "article-通过id查询")
	 @ApiOperation(value = "article-通过id查询", notes = "article-通过id查询")
	 @GetMapping(value = "/queryById")
	 public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
		 Article article = articleService.getById(id);
		 if (article == null) {
			 return Result.error("未找到对应数据");
		 }
		 return Result.ok(article);
	 }

	 /**
	  * 导出excel
	  *
	  * @param request
	  * @param article
	  */
	 @RequestMapping(value = "/exportXls")
	 public ModelAndView exportXls(HttpServletRequest request, Article article) {
		 return super.exportXls(request, article, Article.class, "article");
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
		 return super.importExcel(request, response, Article.class);
	 }

	 @AutoLog(value = "article-添加文章")
	 @ApiOperation(value = "article-添加文章", notes = "article-添加文章")
	 @PostMapping(value = "/addArt")
	 public Result<?> addArt(@RequestParam(name = "title", required = true) String title,
							 @RequestParam(name = "content", required = true) String content,
							 @RequestParam(name = "uid", required = true) Integer uid,
							 @RequestParam(name = "cate", required = true) Integer cate,
							 @RequestParam(name = "sign", required = true) String sign) {
		 // 验证签名

		 //会员加积分
		 final Member member = memberService.getById(uid);
		 member.setUIntegral(member.getUIntegral() + 10);
		 memberService.saveOrUpdate(member);
		 //保存文章
		 Article article = new Article();
		 article.setArtTitle(title).setArtContent(content).setArtUid(uid).setArtCate(cate)
				 .setArtCreatetime((int) (System.currentTimeMillis()/1000));
		 articleService.save(article);
		 return Result.ok("添加成功！");
	 }

	 @AutoLog(value = "article-编辑文章")
	 @ApiOperation(value = "article-编辑文章", notes = "article-编辑文章")
	 @PutMapping(value = "/editArt")
	 public Result<?> editArt(@RequestParam(name = "artId", required = true) Integer artid,
							  @RequestParam(name = "title", required = true) String title,
							  @RequestParam(name = "content", required = true) String content,
							  @RequestParam(name = "uid", required = true) Integer uid,
							  @RequestParam(name = "cate", required = true) Integer cate,
							  @RequestParam(name = "sign", required = true) String sign) {
		 System.out.println("进入了editArt方法");
		 //保存文章
		 Article article = new Article();
		 article.setId(artid).setArtTitle(title).setArtContent(content).setArtUid(uid).setArtCate(cate);
		 System.out.println("article:"+article);
		 articleService.updateById(article);
		 return Result.ok("编辑成功！");
	 }

	 @AutoLog(value = "article-分页列表查询")
	 @ApiOperation(value = "article-分页列表查询", notes = "article-分页列表查询")
	 @GetMapping(value = "/artList")
	 public Result<?> artList(@RequestParam(name = "uid", required = true) Integer uid,
							  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
							  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
		 QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq("art_uid", uid);
		 queryWrapper.orderByDesc("id");
		 Page<Article> page = new Page<Article>(pageNo, pageSize);
		 IPage<Article> pageList = articleService.page(page, queryWrapper);
		 return Result.ok(pageList);
	 }

	 /**
	  * 根据用户id和文章id删除文章
	  *
	  * @param uid
	  * @param artId
	  * @param
	  * @return
	  */
	 @AutoLog(value = "article-删除文章")
	 @ApiOperation(value = "article-删除文章", notes = "article-删除文章")
	 @PostMapping(value = "/removeArt")
	 public Result<?> removeArt(@RequestParam(name = "uid", required = true) int uid,
								@RequestParam(name = "artId", required = true) int artId
								//	   @RequestParam(name="sign", required = true) String sign
	 ) {
		 //验签

		 //uid验证用户

		 articleService.removeById(artId);
		 return Result.ok("删除成功");
	 }

	 /**
	  * 根据文章id获取文章列表
	  *
	  * @param cate
	  * @param pageNo
	  * @param pageSize
	  * @return
	  */
	 @AutoLog(value = "article-根据文章类别查询列表")
	 @ApiOperation(value = "article-根据文章类别查询列表", notes = "article-根据文章类别查询列表")
	 @GetMapping(value = "/artListByCate")
	 public Result<?> artListByCate(@RequestParam(name = "cate", required = true) Integer cate,
									@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
									@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
		 QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
		 if (cate != 0) {
			 queryWrapper.eq("art_cate", cate);
		 }
		 queryWrapper.orderByDesc("id");
		 Page<Article> page = new Page<Article>(pageNo, pageSize);
		 IPage<Article> pageList = articleService.page(page, queryWrapper);
		 if (pageList.getRecords().size() == 0) {
			 return Result.error("未找到数据");
		 }
		 return Result.ok(pageList);
	 }

	 @AutoLog(value = "article-根据文章id获取文章信息")
	 @ApiOperation(value = "article-根据文章id获取文章信息", notes = "article-根据文章id获取文章信息")
	 @GetMapping(value = "/artInfo")
	 public Result<?> artInfo(@RequestParam(name = "artId", required = true) Integer artid) {
		 final Article article = articleService.getById(artid);
		 if (article == null) {
			 return Result.error("未找到数据");
		 }
		 //根据文章的会员id获取会员信息
		 Member member = memberService.getById(article.getArtUid());

		 Map<String, Object> map = new HashMap<>();
//		 map.put("article",article);
//		 map.put("member",member);
		 map.put("artTitle",article.getArtTitle());
		 map.put("artContent",article.getArtContent());
		 map.put("artCreatetimes",article.getArtCreatetime());
		 map.put("uface",member.getUFace());
		 map.put("uname",member.getUName());
		 return Result.ok(map);
	 }

}
