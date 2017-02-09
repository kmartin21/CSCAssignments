/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.testinghashtable;

/**
 *
 * @author kmartin5
 */

/**
 *
 * @author keithmartin
 */
public class AdminThread implements Runnable{

    public static HashTable catalog = new HashTable();
    Course c = new Course();

    @Override
    public void run() {
        catalog.put(c.courseK, c.courseV);
    }

}
