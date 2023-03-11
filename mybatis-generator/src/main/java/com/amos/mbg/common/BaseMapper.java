package com.amos.mbg.common;

import java.util.List;

public interface BaseMapper<T extends BaseDO> {

    int deleteByPrimaryKey(Long id);

    int insert(T row);

    T selectByPrimaryKey(Long id);

    List<T> selectAll();

    int updateByPrimaryKey(T row);

}
