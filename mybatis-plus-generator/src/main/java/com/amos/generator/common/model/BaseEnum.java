package com.amos.generator.common.model;

/**
 * Base Enum
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2022/3/4
 */
public interface BaseEnum {

    /**
     * 数字编码
     *
     * @return code
     */
    Integer getCode();

    /**
     * 描述信息
     *
     * @return 描述
     */
    String getMessage();

}
