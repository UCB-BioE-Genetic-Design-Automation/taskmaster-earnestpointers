package org.ucb.bio134.taskmaster;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ucb.bio134.taskmaster.model.Tip;
import org.ucb.c5.semiprotocol.model.Container;
import org.ucb.c5.semiprotocol.model.Reagent;
import org.ucb.c5.utils.FileUtils;

/**
 * @author Bryant Luong
 * @author J. Christopher Anderson
 */
public class PriceCalculator {

    private Map<String, Double> COG;

    public void initiate() throws Exception {

        // initiate new HashMap for COG
        COG = new HashMap<>();

        String goods = FileUtils.readResourceFile("semiprotocol/data/COG.txt");

        // Split data by line
        String[] goods_rows= goods.split("\\r|\\r?\\n");
        int goods_rows_length = goods_rows.length;


        // Populate COG with materials and their cost
        for (int i = 0; i < goods_rows_length; i++) {
            String goods_oneRow= goods_rows[i];
            String[] columns = goods_oneRow.split("\t");
            COG.put(columns[0],Double.parseDouble(columns[1]));
        }


    }

    public double run(Map<Reagent, Double> reagentCount,
                      Map<Container, Integer> tubeCount,
                      Map<Tip, Integer> tipCount) throws Exception {
        
        double cost = 0.0;

        for (Map.Entry<Reagent, Double> reagent: reagentCount.entrySet() ){
            cost = cost + COG.get(reagent.getKey().name())*reagent.getValue();
        }

        for (Map.Entry<Container, Integer> tube : tubeCount.entrySet() ){
            cost = cost + COG.get(tube.getKey().name())*tube.getValue();
        }

        for (Map.Entry<Tip, Integer> tip : tipCount.entrySet()) {
            cost = cost + COG.get(tip.getKey().name())*tip.getValue();
        }

        return cost;
    }
    
    public static void main(String[] args) throws Exception {
        PriceCalculator calc = new PriceCalculator();
        calc.initiate();
        
        //Construct some example data
        Map<Reagent, Double> reagentCount = new HashMap<>();
        reagentCount.put(Reagent.BsaI, 32.0);
        reagentCount.put(Reagent.T4_DNA_Ligase_Buffer_10x, 32.0);
        reagentCount.put(Reagent.T4_DNA_ligase, 16.0);
        
        Map<Container, Integer> tubeCount = new HashMap<>();
        tubeCount.put(Container.pcr_strip, 3);
        tubeCount.put(Container.pcr_tube, 8);
        tubeCount.put(Container.pcr_plate_96, 1);
        tubeCount.put(Container.eppendorf_1p5mL, 3);
        tubeCount.put(Container.eppendorf_2mL, 2);

        Map<Tip, Integer> tipCount = new HashMap<>();
        tipCount.put(Tip.P20, 86);
        tipCount.put(Tip.P200, 23);
        tipCount.put(Tip.P1000, 6);

        double price = calc.run(reagentCount, tubeCount, tipCount);
        System.out.println("$" + price);
    }
}
