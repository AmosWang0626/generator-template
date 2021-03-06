package com.amos.generator.config;

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import lombok.Getter;
import lombok.Setter;

/**
 * 基础配置
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2022/4/9
 */
@Getter
@Setter
public class GeneratorConfig {

    private static final String DEFAULT_DB_URL = "jdbc:mysql://localhost:3306/generator" +
            "?serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=UTF-8";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    /**
     * 数据库链接地址
     */
    private DataSourceConfig.Builder dbConfigBuilder = new DataSourceConfig.Builder(DEFAULT_DB_URL, USERNAME, PASSWORD);

    /**
     * 作者信息
     */
    private String author = "<a href=\"mailto:daoyuan0626@gmail.com\">amos.wang</a>";

    /**
     * 基础包信息
     */
    private String basePackage;


    /**
     * common包信息
     */
    private String commonPackage;

    /**
     * Java文件输出路径
     */
    private String javaOutputDir;

    /**
     * Mapper.xml文件输出路径
     */
    private String mapperXmlOutputDir;

    /**
     * 要去掉的实体类的前缀
     */
    private String removeClassPrefix;

}
