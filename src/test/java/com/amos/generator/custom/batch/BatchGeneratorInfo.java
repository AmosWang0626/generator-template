package com.amos.generator.custom.batch;

import lombok.Getter;
import lombok.Setter;

/**
 * 批量生成类(entity, mapper, service, controller)
 * <p>
 * 目的：分包。我们有很多很多表，然后要根据表名的前缀进行分包
 * 1. 分包; 2. 去掉实体类的前缀(不只是实体类，还有service等等类的前缀)
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2022/4/9
 */
@Getter
@Setter
public class BatchGeneratorInfo {

    /**
     * 表名前缀。示例: auth_
     */
    private String tablePrefix;

    /**
     * 生成的代码放到指定的包里边。示例: auth
     */
    private String targetPkg;

    /**
     * 是否要去掉实体类的前缀。
     * true-去掉; false-不去掉
     */
    private boolean removeClassPrefix;

    public BatchGeneratorInfo(String tablePrefix, String targetPkg, boolean removeClassPrefix) {
        this.tablePrefix = tablePrefix;
        this.targetPkg = targetPkg;
        this.removeClassPrefix = removeClassPrefix;
    }

    public static BatchGeneratorInfo.BatchGeneratorInfoBuilder builder() {
        return new BatchGeneratorInfo.BatchGeneratorInfoBuilder();
    }

    public static class BatchGeneratorInfoBuilder {
        private String tablePrefix;
        private String targetPkg;
        private boolean removeClassPrefix = true;

        BatchGeneratorInfoBuilder() {
        }

        public BatchGeneratorInfo.BatchGeneratorInfoBuilder tablePrefix(final String tablePrefix) {
            this.tablePrefix = tablePrefix;
            return this;
        }

        public BatchGeneratorInfo.BatchGeneratorInfoBuilder targetPkg(final String targetPkg) {
            this.targetPkg = targetPkg;
            return this;
        }

        public BatchGeneratorInfo.BatchGeneratorInfoBuilder removeClassPrefix(final boolean removeClassPrefix) {
            this.removeClassPrefix = removeClassPrefix;
            return this;
        }

        public BatchGeneratorInfo build() {
            return new BatchGeneratorInfo(this.tablePrefix, this.targetPkg, this.removeClassPrefix);
        }

        public String toString() {
            return "BatchGeneratorInfo.BatchGeneratorInfoBuilder(tablePrefix=" + this.tablePrefix + ", targetPkg=" +
                    "" + this.targetPkg + ", removeClassPrefix=" + this.removeClassPrefix + ")";
        }
    }

}
