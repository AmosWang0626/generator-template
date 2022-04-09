package com.amos.generator;

import com.amos.generator.common.model.BaseDO;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class GeneratorApplicationTests2 {

    String url = "jdbc:mysql://localhost:3306/lc-demo?serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=UTF-8";
    String author = "<a href=\"mailto:daoyuan0626@gmail.com\">amos.wang</a>";
    String parentPackage = "com.amos.lc";
    String javaOutputDir = "/Users/amoswang/Workspace/local/lc-demo/src/main/java";
    String mapperXmlOutputDir = "/Users/amoswang/Workspace/local/lc-demo/src/main/resources/mapper";

    @Test
    public void generate() {
        // 自定义生成文件
        List<CustomFileInfo> customFileInfoList = new ArrayList<>();
        customFileInfoList.add(CustomFileInfo.of("DTO.java",
                "/templates/model.dto.java.vm", "model/dto"));
        customFileInfoList.add(CustomFileInfo.of("VO.java",
                "/templates/model.vo.java.vm", "model/vo"));
        customFileInfoList.add(CustomFileInfo.of("Converter.java",
                "/templates/model.converter.java.vm", "model/converter"));

        // 自定义生成文件构造为Map，供模板引擎使用
        Map<String, CustomFileInfo> customFileInfoMap = customFileInfoList.stream()
                .collect(Collectors.toMap(CustomFileInfo::getSuffixKey, info -> info));


        FastAutoGenerator.create(url, "root", "root")
                // 全局配置
                .globalConfig(this::getGlobalBuilder)
                // 包配置
                .packageConfig(this::getPackageBuilder)
                // 策略配置
                .strategyConfig(this::getStrategyConfigBuilder)
                // 注入配置
                .injectionConfig(builder -> getInjectionBuilder(builder, customFileInfoList))
                // 模板引擎配置，默认 VelocityTemplateEngine 可选模板引擎 BeetlTemplateEngine 或 FreemarkerTemplateEngine
                .templateEngine(new CustomVelocityTemplateEngine(customFileInfoMap))
                .execute();
    }

    private void getInjectionBuilder(InjectionConfig.Builder builder,
                                     List<CustomFileInfo> customFileInfoList) {
        Map<String, Object> customMap = new HashMap<>();
        customMap.put("customDTOPackage", parentPackage + ".model.dto");
        customMap.put("customVOPackage", parentPackage + ".model.vo");
        customMap.put("customConverterPackage", parentPackage + ".model.converter");
        Map<String, String> customFile = new HashMap<>();
        if (!CollectionUtils.isEmpty(customFileInfoList)) {
            customFileInfoList.forEach(info -> customFile.put(info.getSuffixKey(), info.getTemplate()));
        }
        builder.customMap(customMap).customFile(customFile).fileOverride();
    }

    private void getGlobalBuilder(GlobalConfig.Builder builder) {
        builder.author(author).outputDir(javaOutputDir).disableOpenDir();
    }

    private void getPackageBuilder(PackageConfig.Builder builder) {
        builder.parent(parentPackage)
                .moduleName("auth") // 独立目录
                .pathInfo(Collections.singletonMap(OutputFile.xml, mapperXmlOutputDir))
        ;
    }

    private void getStrategyConfigBuilder(StrategyConfig.Builder builder) {
        builder
                .addTablePrefix("auth_") // 不生成表前缀
                // Controller配置
                .controllerBuilder().enableRestStyle().enableHyphenStyle().fileOverride()
                // Mapper配置
                .mapperBuilder().enableBaseColumnList().enableBaseResultMap().fileOverride()
                // Entity配置
                .entityBuilder().formatFileName("%sDO").superClass(BaseDO.class)
                .addSuperEntityColumns("id", "gmt_create", "gmt_modified")
                .enableRemoveIsPrefix()
                .addTableFills(new Column("gmt_create", FieldFill.INSERT))
                .addTableFills(new Column("gmt_modified", FieldFill.INSERT_UPDATE))
                .logicDeleteColumnName("is_deleted")
                .disableSerialVersionUID().enableLombok().fileOverride()
                // end
                .build();
    }

    static class CustomVelocityTemplateEngine extends VelocityTemplateEngine {

        private final Map<String, CustomFileInfo> customFileInfoMap;

        CustomVelocityTemplateEngine(Map<String, CustomFileInfo> customFileInfoMap) {
            this.customFileInfoMap = customFileInfoMap;
        }

        @Override
        protected void outputCustomFile(Map<String, String> customFile, TableInfo tableInfo, Map<String, Object> objectMap) {
            String entityName = tableInfo.getEntityName().replace("DO", "");
            customFile.forEach((key, value) -> {
                CustomFileInfo customFileInfo = customFileInfoMap.get(key);
                String selfPath = getCustomBasePath() + customFileInfo.getPkgName();
                String fileName = String.format((selfPath + File.separator + entityName + "%s"), key);
                boolean fileOverride = false;
                if (Objects.nonNull(getConfigBuilder().getInjectionConfig())) {
                    fileOverride = getConfigBuilder().getInjectionConfig().isFileOverride();
                }
                outputFile(new File(fileName), objectMap, value, fileOverride);
            });
        }

        private String getCustomBasePath() {
            String otherPath = getPathInfo(OutputFile.other);
            assert otherPath != null;
            return otherPath.substring(0, otherPath.lastIndexOf(File.separator) + 1);
        }
    }

    @Getter
    @Setter
    static class CustomFileInfo {

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
         * 包名。默认 other
                 * 例如:
         * - VO 专门放 com.amos.lc.model.vo
         * - DTO 专门放 com.amos.lc.model.dto
         */
        private String pkgName = "other";

        public static CustomFileInfo of(String suffixKey, String template, String pkgName) {
            CustomFileInfo customFileInfo = new CustomFileInfo();
            customFileInfo.setSuffixKey(suffixKey);
            customFileInfo.setTemplate(template);
            customFileInfo.setPkgName(pkgName);
            return customFileInfo;
        }
    }

}
