package com.zhf.tkmapperstudy.help.util;

public interface CastUtils {

    @SuppressWarnings("unchecked")
    static <T> T cast(Object object) {
        return (T) object;
    }

}
