/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.hi;

/**
 *
 * @author kmartin5
 */
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author keithmartin
 */
public class AdminThread implements Runnable{

    public static ConcurrentHashMap<String, Integer> catalog = new ConcurrentHashMap<String, Integer>();
    Course c = new Course();

    @Override
    public void run() {
        catalog.put(c.courseK, c.courseV);
    }

}