package com.android.flash.util;

import com.android.flash.SibOne;
import com.android.flash.SibTwo;

import java.util.ArrayList;

/**
 * User: johnwright
 * Date: 5/6/12
 * Time: 7:02 PM
 */
public class TestData {
    public static ArrayList<SibOne> createData() {
        ArrayList<SibOne> myItems = new ArrayList<SibOne>();
        SibOne tmpSibOne;
        SibTwo tmpSibTwo;
        boolean verbs = true;

        for (int i=0; i<10; i++) {
            tmpSibOne = new SibOne("sibone data " + i);
            tmpSibTwo = new SibTwo("sibtwo data " + i);
            tmpSibOne.updatePair(tmpSibTwo);

            if (verbs) {
                tmpSibTwo.addVerb("siboneverb data " + i, "sibtwoverb data " + i);
            }

            myItems.add(tmpSibOne);
        }
        return myItems;
    }
}
