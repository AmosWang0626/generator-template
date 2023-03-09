package com.amos.generator.custom;

import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 自定义 Velocity 模板引擎
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2022/4/9
 */
public class CustomVelocityTemplateEngine extends VelocityTemplateEngine {

    private final Map<String, CustomTemplateInfo> customFileInfoMap;

    public CustomVelocityTemplateEngine(Map<String, CustomTemplateInfo> customFileInfoMap) {
        this.customFileInfoMap = customFileInfoMap;
    }

    @Override
    protected void outputCustomFile(Map<String, String> customFile, TableInfo tableInfo, Map<String, Object> objectMap) {
        String entityName = tableInfo.getEntityName().replace("DO", "");
        customFile.forEach((key, value) -> {
            CustomTemplateInfo customFileInfo = customFileInfoMap.get(key);
            String selfPath = getCustomBasePath() + customFileInfo.getPkgName();
            String fileName = String.format((selfPath + File.separator + entityName + "%s"), key);
            boolean fileOverride = false;
            if (Objects.nonNull(getConfigBuilder().getInjectionConfig())) {
                fileOverride = getConfigBuilder().getInjectionConfig().isFileOverride();
            }
            Set<String> importDTOPackages = getImportDTOPackages(tableInfo);
            objectMap.put("importDTOPackages", importDTOPackages);
            outputFile(new File(fileName), objectMap, value, fileOverride);
        });
    }

    private Set<String> getImportDTOPackages(TableInfo tableInfo) {
        Set<String> importDTOPackages = new HashSet<>(tableInfo.getImportPackages());
        importDTOPackages.remove(com.amos.generator.common.model.BaseDO.class.getName());
        importDTOPackages.remove(com.baomidou.mybatisplus.annotation.TableName.class.getName());
        importDTOPackages.remove(com.baomidou.mybatisplus.annotation.TableLogic.class.getName());
        importDTOPackages.remove(com.baomidou.mybatisplus.annotation.TableField.class.getName());
        return importDTOPackages;
    }

    private String getCustomBasePath() {
        String otherPath = getPathInfo(OutputFile.other);
        assert otherPath != null;
        return otherPath.substring(0, otherPath.lastIndexOf(File.separator) + 1);
    }
}
