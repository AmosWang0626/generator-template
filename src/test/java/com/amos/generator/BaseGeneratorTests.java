package com.amos.generator;

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
     * 数据库链接地址
     */
    String datasourceUrl = "jdbc:mysql://localhost:3306/generator?serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=UTF-8";
    /**
     * 作者信息
     */
    String author = "<a href=\"mailto:daoyuan0626@gmail.com\">amos.wang</a>";
    /**
     * 基础包信息
     */
    String parentPackage = "com.amos.generator";
    /**
     * Java文件输出路径
     */
    String javaOutputDir = null;
    /**
     * Mapper.xml文件输出路径
     */
    String mapperXmlOutputDir = null;


    /**
     * 初始化
     */
    public void init() {
        String currentProjectDir = System.getProperty("user.dir");

        javaOutputDir = currentProjectDir + "/src/main/java";
        mapperXmlOutputDir = currentProjectDir + "/src/main/resources/mapper";
    }

    @Test
    public void generate() {
        init();

        FastAutoGenerator.create(datasourceUrl, "root", "root")
                // 全局配置
                .globalConfig(this::getGlobalBuilder)
                // 包配置
                .packageConfig(this::getPackageBuilder)
                // 策略配置
                .strategyConfig(this::getStrategyConfigBuilder)
                .execute();
    }

    private void getGlobalBuilder(GlobalConfig.Builder builder) {
        builder.author(author).outputDir(javaOutputDir).disableOpenDir();
    }

    private void getPackageBuilder(PackageConfig.Builder builder) {
        Map<OutputFile, String> fileOutputDirMap = Collections.singletonMap(OutputFile.xml, mapperXmlOutputDir);
        builder.parent(parentPackage).pathInfo(fileOutputDirMap);
    }

    private void getStrategyConfigBuilder(StrategyConfig.Builder builder) {
        builder
                // Controller配置
                .controllerBuilder().enableRestStyle().enableHyphenStyle().fileOverride()
                // Mapper配置
                .mapperBuilder().enableBaseColumnList().enableBaseResultMap().fileOverride()
                // Entity配置
                .entityBuilder().enableLombok().fileOverride()
                // end
                .build();
    }

}
