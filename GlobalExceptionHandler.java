package com.sheng.heritagenexus.exception;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.sheng.heritagenexus.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 张福生
 */
@ControllerAdvice("com.sheng.heritagenexus.controller")
public class GlobalExceptionHandler {

    private static final Log log = LogFactory.get();

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<Object> error(Exception e){
        log.error("异常信息:",e);
        return Result.error();
    }

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public Result<Object> error(CustomException e){
//        log.error("异常信息:",e);
        return Result.error(e.getCode(),e.getMsg());
    }

}
