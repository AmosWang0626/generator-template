package com.amos.generator.common.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 分页查询对象
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2022/4/5
 */
public class PageQuery<T> extends Page<T> {

    private T param;

    public PageQuery() {
        super(1, 10);
    }

    public PageQuery(long current, long size) {
        super(current, size);
    }

    public PageQuery<T> setParam(T param) {
        this.param = param;
        return this;
    }

    public T getParam() {
        return param;
    }
}
