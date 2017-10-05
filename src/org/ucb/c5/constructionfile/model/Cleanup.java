/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ucb.c5.constructionfile.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author J. Christopher Anderson
 */
public class Cleanup implements Step {
    private final String substrate;
    private final String product;
    
    public Cleanup(String dnaName, String product) {
        this.substrate = dnaName;
        this.product = product;
    }
    
    public String getSubstrate() {
        return substrate;
    }

    @Override
    public Operation getOperation() {
        return Operation.cleanup;
    }

    @Override
    public String getProduct() {
        return product;
    }
}
