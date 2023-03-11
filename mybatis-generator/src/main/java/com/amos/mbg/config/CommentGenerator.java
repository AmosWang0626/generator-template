package com.amos.mbg.config;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * 自定义生成配置--备注优化
 *
 * @author TSN
 * @date 2019/12/26
 */
public class CommentGenerator extends EmptyCommentGenerator {

    private String author;

    private final String currentDateStr;


    public CommentGenerator() {
        currentDateStr = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).format(new Date());
    }


    @Override
    public void addConfigurationProperties(Properties properties) {
        author = properties.getProperty("author");
    }

    @Override
    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        String remarks = introspectedTable.getRemarks();
        if (StringUtils.isBlank(remarks)) {
            remarks = introspectedTable.getTableConfiguration().getTableName();
        }
        topLevelClass.addJavaDocLine("/**");
        topLevelClass.addJavaDocLine(" * " + remarks);
        topLevelClass.addJavaDocLine(" *");
        topLevelClass.addJavaDocLine(" * @author " + author);
        topLevelClass.addJavaDocLine(" * @date " + currentDateStr);
        topLevelClass.addJavaDocLine(" */");
    }

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        String remarks = introspectedColumn.getRemarks();
        field.addJavaDocLine("/**");
        field.addJavaDocLine(" * " + remarks);
        field.addJavaDocLine(" */");
    }

}

