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
public class PCR implements Step {
    //Loose-coupled references by name
    private final String oligo1;
    private final String oligo2;
    private final String template;
    private final String product;

    public PCR(String oligo1, String oligo2, String template, String product) {
        this.oligo1 = oligo1;
        this.oligo2 = oligo2;
        this.template = template;
        this.product = product;
    }

    public String getOligo1() {
        return oligo1;
    }

    public String getOligo2() {
        return oligo2;
    }

    public String getTemplate() {
        return template;
    }
    
    @Override
    public String getProduct() {
        return product;
    }
    
    @Override
    public Operation getOperation() {
        return Operation.pcr;
    }
}
