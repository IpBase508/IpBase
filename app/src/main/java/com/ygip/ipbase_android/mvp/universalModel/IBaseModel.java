package com.ygip.ipbase_android.mvp.universalModel;

import com.orhanobut.logger.Logger;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


/**
 * Created by LockyLuo on 2017/11/22.
 */

public abstract class IBaseModel<T> {
    private Class<T> entityClass = null;

    /**
     * 获取泛型T的Class
     * @return
     */
    public Class<T> getEntityClass() {
        Type t = getClass().getGenericSuperclass();
        if(t instanceof ParameterizedType){
            Type[] p = ((ParameterizedType)t).getActualTypeArguments();
            entityClass = (Class<T>)p[0];
            Logger.d(entityClass.getName());
        }

        return  entityClass;
    }
}
