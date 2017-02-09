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
import java.util.Random;

/**
 * Created by on 10/19/15.
 */
public class StudentThread implements Runnable{


    Random random = new Random();
    int randKey;
    int test = 0;

    public StudentThread() {
    }

    @Override
    public void run() {
        getRandKey();
        AdminThread.catalog.get("csc " + randKey);
    }

    private void getRandKey() {
        randKey = random.nextInt(20) + 1;
    }
}
