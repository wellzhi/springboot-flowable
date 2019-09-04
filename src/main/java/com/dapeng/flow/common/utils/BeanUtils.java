package com.dapeng.flow.common.utils;


import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuxz
 */
public class BeanUtils {


    public static List copyList(List<? extends Object> sourceList, @NotNull Class targetClass) {
        List targetList = new ArrayList();
        Object target = null;
        for (Object source : sourceList) {
            try {
                target = targetClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            org.springframework.beans.BeanUtils.copyProperties(source, target);
            targetList.add(target);
        }
        return targetList;
    }

    public static <T> T copyBean(Object source, @NotNull Class<T> targetClass, @Nullable String... ignoreProperties) {
        T target = null;
        try {
            target = targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        org.springframework.beans.BeanUtils.copyProperties(source, target, ignoreProperties);
        return target;
    }
}
