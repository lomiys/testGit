package com.demo.utils.baseTest;

import com.google.common.collect.Lists;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author yaoyu
 * @date 16/4/8
 * @说明:
 */
public class TestAB {
    public static void main(String[] args) {
      final Date date=new Date();
        System.out.println(date);
        test11(date);
        System.out.println(date);
    }


    public static void test11(Date date){
        date=new Date();
        System.out.println(date);
    }

}
