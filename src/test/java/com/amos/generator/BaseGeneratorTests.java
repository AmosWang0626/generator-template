package com.amos.generator;

import com.amos.generator.custom.CustomConfig;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import org.junit.jupiter.api.Test;

import java.util.Collections;
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

        FastAutoGenerator.create(customConfig.getDbConfigBuilder())
                // 全局配置
                .globalConfig(builder -> getGlobalBuilder(builder, customConfig))
                // 包配置
                .packageConfig(builder -> getPackageBuilder(builder, customConfig))
                // 策略配置
                .strategyConfig(this::getStrategyConfigBuilder)
                .execute();
    }

    private void getGlobalBuilder(GlobalConfig.Builder builder, CustomConfig customConfig) {
        builder.author(customConfig.getAuthor())
                .outputDir(customConfig.getJavaOutputDir())
                .disableOpenDir();
    }

    private void getPackageBuilder(PackageConfig.Builder builder, CustomConfig customConfig) {
        Map<OutputFile, String> fileOutputDirMap =
                Collections.singletonMap(OutputFile.xml, customConfig.getMapperXmlOutputDir());

        builder.parent(customConfig.getBasePackage()).pathInfo(fileOutputDirMap);
    }

    private void getStrategyConfigBuilder(StrategyConfig.Builder builder) {
        builder
                .controllerBuilder().enableRestStyle().enableHyphenStyle().fileOverride()
                .mapperBuilder().enableBaseColumnList().enableBaseResultMap().fileOverride()
                .entityBuilder().enableLombok().fileOverride()
                .build();
    }

}
