package com.amos.generator.custom;

import com.amos.generator.common.model.BaseDO;
import com.amos.generator.custom.batch.BatchGeneratorInfo;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import org.apache.commons.collections.MapUtils;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * 根据表前缀批量生成分包后的业务代码
 *
 * @author amoswang
 */
public class BatchGeneratorTests {

    /**
     * 初始化
     */
    private CustomConfig init() {
        CustomConfig customConfig = new CustomConfig();

        String currentProjectDir = System.getProperty("user.dir");

        customConfig.setJavaOutputDir(currentProjectDir + "/src/main/java");
        customConfig.setMapperXmlOutputDir(currentProjectDir + "/src/main/resources/mapper");

        return customConfig;
    }

    @Test
    public void generate() {
        CustomConfig customConfig = init();

        // 全局模板参数
        Map<String, String> globalCustomParam = getGlobalCustomParam(customConfig);

        // 自定义生成文件
        Map<String, CustomTemplateInfo> customTemplateInfoMap = customTemplateInfo();

        List<BatchGeneratorInfo> batchGeneratorInfoList = getBatchGeneratorInfoList();

        for (BatchGeneratorInfo batchGeneratorInfo : batchGeneratorInfoList) {
            FastAutoGenerator.create(customConfig.getDbConfigBuilder())
                    // 全局配置
                    .globalConfig(builder -> getGlobalBuilder(builder, customConfig))
                    // 包配置
                    .packageConfig(builder -> getPackageBuilder(builder, customConfig, batchGeneratorInfo.getTargetPkg()))
                    // 策略配置
                    .strategyConfig(builder -> getStrategyConfigBuilder(builder, batchGeneratorInfo))
                    // 注入配置
                    .injectionConfig(builder -> getInjectionBuilder(builder, customTemplateInfoMap, globalCustomParam))
                    // 模板引擎配置，默认 VelocityTemplateEngine 可选模板引擎 BeetlTemplateEngine 或 FreemarkerTemplateEngine
                    .templateEngine(new CustomVelocityTemplateEngine(customTemplateInfoMap))
                    .execute();
        }
    }

    private List<BatchGeneratorInfo> getBatchGeneratorInfoList() {
        List<BatchGeneratorInfo> batchGeneratorInfoList = new ArrayList<>();
        batchGeneratorInfoList.add(BatchGeneratorInfo.builder().tablePrefix("auth_").targetPkg("auth").build());
        batchGeneratorInfoList.add(BatchGeneratorInfo.builder().tablePrefix("pay_").targetPkg("pay").build());
        batchGeneratorInfoList.add(BatchGeneratorInfo.builder().tablePrefix("promotion_").targetPkg("promotion").build());
        batchGeneratorInfoList.add(BatchGeneratorInfo.builder().tablePrefix("wms_").targetPkg("wms").removeClassPrefix(false).build());

        return batchGeneratorInfoList;
    }

    private Map<String, String> getGlobalCustomParam(CustomConfig customConfig) {
        Map<String, String> globalCustomMap = new HashMap<>();
        globalCustomMap.put("commonPkg", customConfig.getCommonPackage());
        return globalCustomMap;
    }

    private Map<String, CustomTemplateInfo> customTemplateInfo() {
        Map<String, CustomTemplateInfo> customTemplateInfoMap = new HashMap<>();

        CustomTemplateInfo dtoTemplate = CustomTemplateInfo.builder().suffixKey("DTO.java")
                .template("/templates/model.dto.java.vm").pkgName("model/dto")
                .customMap(Collections.singletonMap("relativeDTOPkg", ".model.dto"))
                .build();
        CustomTemplateInfo voTemplate = CustomTemplateInfo.builder().suffixKey("VO.java")
                .template("/templates/model.vo.java.vm").pkgName("model/vo")
                .customMap(Collections.singletonMap("relativeVOPkg", ".model.vo"))
                .build();
        CustomTemplateInfo converterTemplate = CustomTemplateInfo.builder().suffixKey("Converter.java")
                .template("/templates/model.converter.java.vm").pkgName("model/converter")
                .customMap(Collections.singletonMap("relativeConverterPkg", ".model.converter"))
                .build();

        customTemplateInfoMap.put(dtoTemplate.getSuffixKey(), dtoTemplate);
        customTemplateInfoMap.put(voTemplate.getSuffixKey(), voTemplate);
        customTemplateInfoMap.put(converterTemplate.getSuffixKey(), converterTemplate);

        return customTemplateInfoMap;
    }

    private void getInjectionBuilder(InjectionConfig.Builder builder,
                                     Map<String, CustomTemplateInfo> fileInfoMap,
                                     Map<String, String> globalCustomMap) {
        Map<String, Object> customMap = new HashMap<>();
        Map<String, String> customFile = new HashMap<>();

        // 全局模板变量
        if (MapUtils.isNotEmpty(globalCustomMap)) {
            customMap.putAll(globalCustomMap);
        }

        // 自定义配置文件与模板变量
        if (MapUtils.isNotEmpty(fileInfoMap)) {
            for (CustomTemplateInfo info : fileInfoMap.values()) {
                // 自定义配置文件
                customFile.put(info.getSuffixKey(), info.getTemplate());

                // 自定义配置文件的变量
                if (MapUtils.isNotEmpty(info.getCustomMap())) {
                    customMap.putAll(info.getCustomMap());
                }
            }
        }

        builder
                .beforeOutputFile((tableInfo, objectMap) -> {
                    String path = objectMap.get("controllerMappingHyphen").toString();
                    objectMap.put("controllerMappingHyphen", path.replace("-do", ""));
                })
                .customMap(customMap)
                .customFile(customFile)
                .fileOverride();
    }

    private void getGlobalBuilder(GlobalConfig.Builder builder, CustomConfig customConfig) {
        builder.author(customConfig.getAuthor())
                .outputDir(customConfig.getJavaOutputDir())
                .disableOpenDir();
    }

    private void getPackageBuilder(PackageConfig.Builder builder, CustomConfig customConfig, String moduleName) {
        Map<OutputFile, String> fileOutputDirMap =
                Collections.singletonMap(OutputFile.xml, customConfig.getMapperXmlOutputDir());

        builder.parent(customConfig.getBasePackage())
                .pathInfo(fileOutputDirMap)
                .moduleName(moduleName); // 具体包名
    }

    private void getStrategyConfigBuilder(StrategyConfig.Builder builder, BatchGeneratorInfo batchGeneratorInfo) {
        List<String> tablePrefixList = new ArrayList<>();
        if (batchGeneratorInfo.isRemoveClassPrefix()) {
            tablePrefixList.add(batchGeneratorInfo.getTablePrefix());
        }

        builder
                // 使用正则匹配(注意正则格式哦)，每个包下只生成指定前缀的表
                .disableSqlFilter()
                .addInclude("^" + batchGeneratorInfo.getTablePrefix() + "(.*)$")
                // 生成的类是否去掉表名前缀
                .addTablePrefix(tablePrefixList)
                .controllerBuilder().enableRestStyle().enableHyphenStyle().fileOverride()
                .mapperBuilder().enableBaseColumnList().enableBaseResultMap().fileOverride()
                // entity
                .entityBuilder().formatFileName("%sDO").superClass(BaseDO.class).enableRemoveIsPrefix()
                .addSuperEntityColumns("id", "gmt_create", "gmt_modified").logicDeleteColumnName("is_deleted")
                .disableSerialVersionUID().enableLombok().fileOverride()
                // end
                .build();
    }

}
