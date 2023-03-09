package com.amos.generator.custom;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 自定义模板文件信息
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2022/4/9
 */
@Getter
@Setter
@Builder
public class CustomTemplateInfo {

    /**
     * 文件后缀
     * 例如: DTO.java
     */
    private String suffixKey;

    /**
     * 文件模板
     * 例如: /templates/model.dto.java.vm
     */
    private String template;

    /**
     * 包名
     * 例如:
     * - vo 专门放 com.amos.lc.model.vo
     * - dto 专门放 com.amos.lc.model.dto
     */
    private String pkgName;

    /**
     * 模板文件中的自定义参数
     */
    private Map<String, Object> customMap;

}
