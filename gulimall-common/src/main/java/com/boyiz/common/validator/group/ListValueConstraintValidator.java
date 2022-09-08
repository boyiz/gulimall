package com.boyiz.common.validator.group;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

public class ListValueConstraintValidator implements ConstraintValidator<ListValue, Integer> {

    private Set<Integer> set = new HashSet<>();

    //初始化方法
    @Override
    public void initialize(ListValue constraintAnnotation) {
        //ConstraintValidator.super.initialize(constraintAnnotation);
        int[] vals = constraintAnnotation.vals();

        for (int val : vals) {
            set.add(val);

        }
    }

    //判断是否教研成功

    /**
     * @param integer   需要校验的值
     * @param constraintValidatorContext
     * @return
     */

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return set.contains(integer);
    }
}
