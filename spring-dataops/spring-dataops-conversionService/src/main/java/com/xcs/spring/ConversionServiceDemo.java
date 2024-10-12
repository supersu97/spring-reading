package com.xcs.spring;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.DefaultConversionService;

/**
 * @author xcs
 * @date 2023年12月08日 16时30分
 **/
public class ConversionServiceDemo {

    public static void main(String[] args) {
        // 创建 DefaultConversionService 实例
        ConversionService conversionService = new DefaultConversionService();

        //1.GenericConversionService里面有两个内部类，ConverterAdapter和ConverterFactoryAdapter，两个都是继承ConditionalGenericConverter的
        //2.内部有一个Map<ConvertiblePair, ConvertersForPair> converters的map，用来存储所有注册的转换器，所以需要将ConverterFactory和Converter对象转成GenericConverter
        //3.ConditionalGenericConverter 继承 GenericConverter和ConditionalConverter
        //4.conversionService的add方法，当对象是ConverterFactory的时候，转换成ConverterFactoryAdapter对象，当对象是Converter时，转换成ConverterAdapter
        //5.将原转换器存在converter字段里，内部类重写的convert方法，都是直接调用原对象的convert方法，然后getConvertibleTypes方法，就是将原对象的转换类返回
        //6.至于ConditionalConverter的matches方法，如果对象没有实现ConditionalConverter接口，有自定义的matches方法，那么就直接返回成功，如果实现了，就调用自定义的方法校验
        // 所以converterFactory和converter的对象，在getConvert方法时，会判断有没有实现ConditionalGenericConverter接口，没实现直接返回true，也就不会走matches方法
        // 如果实现了，就走上一步，判断具体的对象有没有实现ConditionalConverter接口，有就调用重写的matches，没有就直接返回true

        // 使用 canConvert 检查转换是否可能
        if (conversionService.canConvert(Integer.class, String.class)) {
            System.out.println("Can convert from Integer to String");

            // 执行转换操作，将 Integer 转换为 String
            String converted = conversionService.convert(666, String.class);
            System.out.println("Converted: " + converted);
        }

        // 使用 TypeDescriptor 检查转换是否可能
        if (conversionService.canConvert(
                TypeDescriptor.valueOf(Integer.class),
                TypeDescriptor.valueOf(String.class))) {
            System.out.println("Can convert from Integer to String using TypeDescriptors");

            // 使用 TypeDescriptor 执行转换
            Object convertedWithTypeDescriptor = conversionService.convert(
                    888,
                    TypeDescriptor.valueOf(Integer.class),
                    TypeDescriptor.valueOf(String.class));
            System.out.println("Converted with TypeDescriptors: " + convertedWithTypeDescriptor);
        }
    }
}
