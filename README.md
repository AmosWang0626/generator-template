# Java代码生成器-模板项目

## [mybatis-plus-generator](mybatis-plus-generator)

基于 mybatis-plus-generator 实现。

1. 生成controller层代码（生成了，用不到的类，例如中间表，还是要删掉的，删总比写快哈）
2. 生成dto、vo、converter（用来处理dto --> do, do --> dto, dto --> vo）
3. 根据表名前缀批量生成分包后的业务代码 @see `BatchGeneratorTests`


## [mybatis-generator](mybatis-generator)

基于 mybatis-generator 实现。

1. 字段注释自动取表里的 COMMENT
2. 集成 lombok，默认生成带 lombok @Getter @Setter 注解的类