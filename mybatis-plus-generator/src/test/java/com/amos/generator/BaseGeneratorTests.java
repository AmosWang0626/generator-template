package com.amos.generator;

import com.amos.generator.config.GeneratorConfig;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 基于简单配置生成相关模板代码
 *
 * @author amoswang
 */
public class BaseGeneratorTests {

    /**
     * 初始化
     */
    private GeneratorConfig init() {
        GeneratorConfig generatorConfig = new GeneratorConfig();

        String currentProjectDir = System.getProperty("user.dir");
        generatorConfig.setJavaOutputDir(currentProjectDir + "/src/main/java");
        generatorConfig.setMapperXmlOutputDir(currentProjectDir + "/src/main/resources/mapper");

        generatorConfig.setBasePackage("com.amos.generator.biz");
        generatorConfig.setCommonPackage("com.amos.generator.common");
        generatorConfig.setRemoveClassPrefix("auth");

        return generatorConfig;
    }

    @Test
    public void generate() {
        GeneratorConfig generatorConfig = init();

        FastAutoGenerator.create(generatorConfig.getDbConfigBuilder())
                // 全局配置
                .globalConfig(builder -> getGlobalBuilder(builder, generatorConfig))
                // 包配置
                .packageConfig(builder -> getPackageBuilder(builder, generatorConfig))
                // 策略配置
                .strategyConfig(builder -> getStrategyConfigBuilder(builder, generatorConfig))
                .execute();
    }

    private void getGlobalBuilder(GlobalConfig.Builder builder, GeneratorConfig generatorConfig) {
        builder.author(generatorConfig.getAuthor())
                .outputDir(generatorConfig.getJavaOutputDir())
                .disableOpenDir();
    }

    private void getPackageBuilder(PackageConfig.Builder builder, GeneratorConfig generatorConfig) {
        Map<OutputFile, String> fileOutputDirMap =
                Collections.singletonMap(OutputFile.xml, generatorConfig.getMapperXmlOutputDir());

        builder.parent(generatorConfig.getBasePackage()).pathInfo(fileOutputDirMap);
    }

    private void getStrategyConfigBuilder(StrategyConfig.Builder builder, GeneratorConfig generatorConfig) {
        List<String> tablePrefixList = new ArrayList<>();
        if (StringUtils.isNotBlank(generatorConfig.getRemoveClassPrefix())) {
            tablePrefixList.add(generatorConfig.getRemoveClassPrefix());
        }

        builder
                .addTablePrefix(tablePrefixList)
                .controllerBuilder().enableRestStyle().enableHyphenStyle().fileOverride()
                .mapperBuilder().enableBaseColumnList().enableBaseResultMap().fileOverride()
                .entityBuilder().enableLombok().fileOverride()
                .build();
    }

}
