package com.example.experiments;/*
 * Created by Sudhanshu Kumar on 09/03/24.
 */

public class Test {

    private static Test single_instance = null;

    private Test() {

    }

    public static synchronized Test getInstance() {
        if(single_instance == null) {
            single_instance = new Test();
        }
        return single_instance;
    }

}
