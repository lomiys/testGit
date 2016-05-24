package com.demo.utils.proxy;

/**
 * Created by yaoyu on 16/2/29.
 */
public class BookFacadeImpl implements BookFacade {
    @Override
    public void addBook() {
        System.out.println("增加图书的的方法....");
    }
}
