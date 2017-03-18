package com.patrickzhong.aplux;



/**
 * Created by patrickzhong on 3/18/17.
 */

public class Report implements Comparable {

    String id;
    String desc;
    Double dist;

    public Report(String id, String desc, Double dist){
        this.id = id;
        this.desc = desc;
        this.dist = dist;
    }


    @Override
    public int compareTo(Object o) {
        return dist.compareTo(((Report) o).dist);
    }
}
