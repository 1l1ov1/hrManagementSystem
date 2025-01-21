package com.wan.commonservice.untils;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.function.Function;
import java.util.function.Supplier;

public class CheckFieldsUtil {


    /**
     * 检查提供的字段值是否为空
     *
     * @param isAll 是否需要所有字段都不为空（true）或至少一个字段不为空（false）
     * @param suppliers 提供字段值的 Supplier 数组
     * @return 如果isAll为true且所有字段都不为空，或isAll为false且至少一个字段不为空，返回true；否则返回false
     */
    public static boolean checkFieldsIsNull(boolean isAll, Supplier<?>... suppliers) {

        for (Supplier<?> supplier : suppliers) {
            Object fieldValue = supplier.get();

            if (fieldValue == null) {
                if (isAll) {
                    return false; // 如果isAll为true且发现一个字段为空，返回false
                }
            } else {
                if (!isAll) {
                    return true; // 如果isAll为false且发现一个字段不为空，返回true
                }
            }
        }

        return isAll; // 返回最终结果
    }

}
