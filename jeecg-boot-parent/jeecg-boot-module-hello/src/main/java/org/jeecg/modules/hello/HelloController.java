package org.jeecg.modules.hello;

import io.swagger.annotations.ApiOperation;
import org.jeecg.common.api.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

/**
 * 测试新的module
 * @author chengtg
 *
 */
@Slf4j
@Api(tags="新建module--hello")
@RestController
@RequestMapping("/hello")
public class HelloController  {
    @GetMapping(value="/")
    @ApiOperation("测试hello方法")
    public Result<String> hello(){
        Result<String> result = new Result<String>();
        result.setResult("hello world!");
        result.setSuccess(true);
        return result;
    }
}
