package com.ziyun.basic.mapper;

import java.util.List;
import java.util.Map;

public interface UserOptionMapper {
    List<Map<String, Object>> getUserLabel(String username);

    int insertUserLabel(Map<String, Object> label);

    int deleteUserLabel(Map<String, Object> map);

    Map<String, Object> getLabelById(Long id);
}
