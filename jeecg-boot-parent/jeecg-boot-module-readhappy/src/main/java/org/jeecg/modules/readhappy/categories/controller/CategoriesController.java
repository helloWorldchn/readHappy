package org.jeecg.modules.readhappy.categories.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.readhappy.categories.entity.Categories;
import org.jeecg.modules.readhappy.categories.service.ICategoriesService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: categories
 * @Author: jeecg-boot
 * @Date:   2020-06-11
 * @Version: V1.0
 */
@Api(tags="categories")
@RestController
@RequestMapping("/readHappy/categories")
@Slf4j
public class CategoriesController extends JeecgController<Categories, ICategoriesService> {
	@Autowired
	private ICategoriesService categoriesService;
	
	/**
	 * 分页列表查询
	 *
	 * @param categories
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "categories-分页列表查询")
	@ApiOperation(value="categories-分页列表查询", notes="categories-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Categories categories,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Categories> queryWrapper = QueryGenerator.initQueryWrapper(categories, req.getParameterMap());
		Page<Categories> page = new Page<Categories>(pageNo, pageSize);
		IPage<Categories> pageList = categoriesService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param categories
	 * @return
	 */
	@AutoLog(value = "categories-添加")
	@ApiOperation(value="categories-添加", notes="categories-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Categories categories) {
		categoriesService.save(categories);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param categories
	 * @return
	 */
	@AutoLog(value = "categories-编辑")
	@ApiOperation(value="categories-编辑", notes="categories-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Categories categories) {
		categoriesService.updateById(categories);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "categories-通过id删除")
	@ApiOperation(value="categories-通过id删除", notes="categories-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		categoriesService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "categories-批量删除")
	@ApiOperation(value="categories-批量删除", notes="categories-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.categoriesService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "categories-通过id查询")
	@ApiOperation(value="categories-通过id查询", notes="categories-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		Categories categories = categoriesService.getById(id);
		if(categories==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(categories);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param categories
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Categories categories) {
        return super.exportXls(request, categories, Categories.class, "categories");
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
        return super.importExcel(request, response, Categories.class);
    }

    @AutoLog(value = "categories-查询文章分类")
	@ApiOperation(value="categories-查询文章分类", notes="categories-查询文章分类")
	@GetMapping(value = "/cateType")
    public Result<?> cateType(@RequestParam(name = "pid",defaultValue = "0") int pid){
		QueryWrapper<Categories> query = new QueryWrapper<>();
		query.eq("cate_pid",pid);
		List<Categories> list = categoriesService.list(query);
		if(list == null){
			return Result.error("没有找到文章分类数据");
		}
		return Result.ok(list);
	 }


}
