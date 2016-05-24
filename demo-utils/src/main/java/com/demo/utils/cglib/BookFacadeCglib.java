package com.demo.utils.cglib;


import com.demo.utils.proxy.BookFacadeImpl;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by yaoyu on 16/2/29.
 */
public class BookFacadeCglib implements MethodInterceptor {

    private Object target;
    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("开始");
        methodProxy.invokeSuper(o, args);
        return null;
    }

    public Object getInstance(Object target){
        this.target=target;
        Enhancer enhancer=new Enhancer();
        enhancer.setSuperclass(this.target.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    public static void main(String[] args) {
        BookFacadeCglib cglib=new BookFacadeCglib();
        BookFacadeImpl1 bookFacadeImpl1= (BookFacadeImpl1) cglib.getInstance(new BookFacadeImpl1());
        bookFacadeImpl1.addBook();
    }
}
