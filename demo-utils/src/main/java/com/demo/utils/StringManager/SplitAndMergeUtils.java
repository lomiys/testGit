package com.demo.utils.StringManager;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yaoyu on 16/2/22.
 */
public class SplitAndMergeUtils {

    public static void SplitterTest(){
        List<String> qq=Splitter.on(",").limit(3).trimResults().splitToList("a,b,c,d");
        System.out.printf(qq.toString()+qq.size());
    }
    public static void MapJoinerTest(){
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("a","aa");
        map.put("b","bb");
        map.put(null,null);
        String s2=Joiner.on(",").withKeyValueSeparator("=").useForNull("yy").join(map);
        System.out.printf(s2);
    }
    public static void JoinerTest(){
        List<String> list=new ArrayList<String>();
        list.add("aa");
        list.add(null);
        list.add("bb");
        String s1=Joiner.on(",").skipNulls().join(list);
        System.out.printf(s1);
    }


    public static void main(String[] args) {
        JoinerTest();
    }
}
