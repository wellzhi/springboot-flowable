package com.dapeng.flow.common.utils;


import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuxz
 */
public class BeanUtil {


    public static List copyList(List<? extends Object> sourceList, @NotNull Class targetClass) {
        List targetList = new ArrayList();
        Object target = null;
        for (Object source : sourceList) {
            try {
                target = targetClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            BeanUtils.copyProperties(source, target);
            targetList.add(target);
        }
        return targetList;
    }

    public static <T> T copyBean(T entity, @NotNull Class targetClass) {
        Object target = null, source = null;
        try {
            source = entity.getClass().newInstance();
            target = targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        BeanUtils.copyProperties(source, target);
        return (T) target;
    }
}
