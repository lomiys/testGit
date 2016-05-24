package com.demo.utils.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by yaoyu on 16/2/29.
 */
public class BookFacadeProxy implements InvocationHandler{
    private Object target;
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result=null;
        System.out.println("事务开始");
        result=method.invoke(target,args);
        System.out.println("事务结束");
        return result;
    }

    public Object bind(Object target){
        this.target = target;
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),target.getClass().getInterfaces(),this);
    }

    public static void main(String[] args) {
        BookFacadeProxy proxy=new BookFacadeProxy();
        BookFacade bookFacade =(BookFacade) proxy.bind(new BookFacadeImpl());
        bookFacade.addBook();
    }
}
