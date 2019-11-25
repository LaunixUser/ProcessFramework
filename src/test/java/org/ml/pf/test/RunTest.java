/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ml.pf.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author osboxes
 */
public class RunTest {

    public static void main(String[] args) {

        AddHelloStep step1 = new AddHelloStep("1");
        AddHelloStep step2 = new AddHelloStep("2");
        AddHelloStep step3 = new AddHelloStep("3");
        AddHelloStep step4 = new AddHelloStep("4");
        step1.addNext(step2);
        step1.addNext(step3);
        step3.addNext(step4);

        step1.addOutput(new StringOutput("step1-output1: "), new StringOutput("step1-output2: "));
        step2.addOutput(new StringOutput("step2-output1: "));

        List<String> list = new ArrayList<>();
        Map<String, Object> result = step1.run(list);

        int i = 0;
        for (String stepID : result.keySet()) {
            List<String> l = (List<String>) result.get(stepID);
            System.out.println("Step: " + stepID);
            for (String s : l) {
                System.out.println("run-result: " + "(" + i + ") : " + s);
            }
            i++;
        }

        for (String s : step1.getResult()) {
            System.out.println("step1: " + s);
        }
        for (String s : step2.getResult()) {
            System.out.println("step2: " + s);
        }
        for (String s : step3.getResult()) {
            System.out.println("step3: " + s);
        }
        for (String s : step4.getResult()) {
            System.out.println("step4: " + s);
        }
    }
}
