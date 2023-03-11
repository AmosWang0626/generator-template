# Java代码生成器-模板项目

> 基于 mybatis-generator 生成自定义模板代码。

入口方法：GeneratorApplicationTests

## 字段注释自动取表里的 COMMENT

主要通过 CommentGenerator.java 实现。

## 集成了 lombok

默认生成带 lombok @Getter @Setter 注解的类。

主要通过 LombokPlugin.java 实现。

其他的注解可配置：

```xml

<plugin type="com.amos.mbg.config.LombokPlugin">
    <!-- getter/setter -->
    <property name="getter" value="true"/>
    <property name="setter" value="true"/>

    <!-- builder -->
    <property name="builder" value="false"/>

    <!-- accessors -->
    <property name="accessors" value="false"/>
    <property name="accessors.chain" value="true"/>

    <!-- constructor -->
    <property name="allArgsConstructor" value="false"/>
</plugin>
```