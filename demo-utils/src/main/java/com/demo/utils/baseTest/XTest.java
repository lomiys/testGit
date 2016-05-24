package com.demo.utils.baseTest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

/**
 * @author yaoyu@weidian.com
 * @date 16/5/12
 * @说明:
 */
public class XTest {
    public static void main(String[] args) {
        final int a[]={1,2,3};
        xiugai(a);
        System.out.println(a.toString());
    }

    public static void xiugai(int a[]){
        a=new int[]{2,2,2};
        System.out.println(a[0]+","+a[1]+","+a[2]);
    }
}
