package com.sheng.heritagenexus.exception;

import com.sheng.heritagenexus.common.enums.ResultCodeEnum;
import lombok.*;

/**
 * @author 张福生
 * @version 1.0
 * @data 2025/12/30 15:23
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomException extends RuntimeException{

    private Integer code;
    private String msg;

    public CustomException(ResultCodeEnum resultCodeEnum) {
        this.code = resultCodeEnum.getCode();
        this.msg = resultCodeEnum.getMsg();
    }
}
