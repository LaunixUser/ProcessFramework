/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ml.pf.test;

import org.ml.pf.step.AbstractProcessStep;

import java.util.ArrayList;
import java.util.List;

/**
 * @author osboxes
 */
public class AddHelloStep extends AbstractProcessStep<List<String>, List<String>, String> {

    /**
     * @param s
     */
    public AddHelloStep(String s) {
        super(s);
    }

    /**
     * @param data
     * @return
     */
    @Override
    public List<String> convert(List<String> data) {
        if (data == null) {
            throw new NullPointerException("data may not be null");
        }
        List<String> myData = new ArrayList<>();
        myData.addAll(data);
        myData.add("Hello " + getID());
        return myData;
    }

    /**
     * @param data
     * @return
     */
    @Override
    public String createOutputData(List<String> data) {
        if (data == null) {
            throw new NullPointerException("data may not be null");
        }
        StringBuilder sb = new StringBuilder(200);
        for (String s : data) {
            sb.append(s);
            sb.append("-");
        }
        return sb.toString();
    }

}
