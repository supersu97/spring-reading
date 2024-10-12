package com.xcs.spring;

import com.xcs.spring.converter.StringToNumberConverterFactory;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;

public class CharacterToNumberFactoryDemo {

    public static void main(String[] args) {
        // 创建一个默认的转换服务
        // 这里使用 GenericConversionService，它是一个通用的类型转换服务
        GenericConversionService conversionService = new DefaultConversionService();

        // 向转换服务中添加一个字符串到数字的转换器工厂
        // StringToNumberConverterFactory 是一个工厂类，用于创建字符串到数字的转换器
        conversionService.addConverterFactory(new StringToNumberConverterFactory());

        // 使用转换服务将字符串 "8" 转换为 Integer 类型
        // 这里演示了如何将字符串转换为对应的整数
        if (conversionService.canConvert(String.class,Integer.class)) {
            //1.先去缓存中寻找有没有类型对应的转换器
            //2.如果没有，那么将查找所有父类的转换器，直到查询到后将新的类型组放入缓存
            //3.然后用对应的转换器进行转换
            //注册的是<String,Number>类型组放进缓存，第一次进去的时候，是找不到的，然后通过Integer父类找寻到<String,Number>的转换器，将<String,Integer>类型组放入缓存，指向当前查到的转换器
            Integer num = conversionService.convert("8", Integer.class);
            // 输出转换结果
            System.out.println("String to Integer: " + num);
        }
    }
}
