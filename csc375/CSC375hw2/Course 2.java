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
public class Course {
    
    public volatile String courseK;
    public volatile int courseV;

    private static volatile int courseNum = 0;

    public Course () {
        this.courseK = "csc " + (++courseNum);
        this.courseV = courseNum;
    }
    
}
