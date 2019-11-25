/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ml.pf.test;

import org.ml.pf.output.IOutput;

/**
 * @author osboxes
 */
public class StringOutput implements IOutput<String> {

    private String s;

    /**
     * @param s
     */
    public StringOutput(String s) {
        if (s == null) {
            throw new NullPointerException("s may not be null");
        }
        this.s = s;

    }

    /**
     * @param data
     */
    @Override
    public void put(String data) {
        if (data == null) {
            throw new NullPointerException("data may not be null");
        }
        System.out.println(s + " | " + data);
    }

}
