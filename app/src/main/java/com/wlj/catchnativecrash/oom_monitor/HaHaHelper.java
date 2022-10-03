package com.wlj.catchnativecrash.oom_monitor;

import com.squareup.haha.perflib.ArrayInstance;
import com.squareup.haha.perflib.ClassInstance;
import com.squareup.haha.perflib.Instance;
import com.squareup.haha.perflib.Type;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 当前类的注释:
 * 作者：WangLiJian on 2022/10/3.
 * 邮箱：wanglijian1214@gmail.com
 */
class HaHaHelper {

    public static List<ClassInstance.FieldValue> classInstanceValues(Instance instance){
      ClassInstance classInstance=(ClassInstance) instance;
      return classInstance.getValues();
    }

    public static <T> T filedValue(List<ClassInstance.FieldValue> values, String filedName) {
        for (ClassInstance.FieldValue value : values) {
            if (value.getField().getName().equals(filedName)) {
                return (T) value.getValue();
            }
        }
        return null;
    }


    public static String asString(Object stringObj){

        Instance instance=(Instance) stringObj;
        List<ClassInstance.FieldValue> values=classInstanceValues(instance);
        Integer count = filedValue(values, "count");
        if (count == 0) {
            return "";
        }
        Object value = filedValue(values, "value");
        if (isCharArray(value)) {
            Integer offset = 0;
            ArrayInstance array = (ArrayInstance) value;
            if(hasFiled(values, "offset")){
                offset = filedValue(values, "offset");
            }
            char[] chars = array.asCharArray(offset, count);
            return new String(chars);
        }else if (isByteArray(value)) {
            ArrayInstance array = (ArrayInstance) value;
            try {
                Method asRawByteArrayMethod = ArrayInstance.class.getDeclaredMethod(
                        "asRawByteArray", int.class, int.class);
                asRawByteArrayMethod.setAccessible(true);
                byte[] rawByteArray = (byte[]) asRawByteArrayMethod.invoke(array,0, count);
                return new String(rawByteArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    private static boolean hasFiled(List<ClassInstance.FieldValue> values, String filedName) {
        return filedValue(values, filedName) != null;
    }

    private static boolean isByteArray(Object value) {
        return value instanceof ArrayInstance && ((ArrayInstance)value).getArrayType() == Type.BYTE;
    }
    private static boolean isCharArray(Object value) {
        return value instanceof ArrayInstance && ((ArrayInstance)value).getArrayType() == Type.CHAR;
    }
}
