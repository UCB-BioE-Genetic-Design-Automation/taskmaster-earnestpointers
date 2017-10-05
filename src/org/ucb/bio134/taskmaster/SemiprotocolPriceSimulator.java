package org.ucb.bio134.taskmaster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.ucb.c5.constructionfile.model.ConstructionFile;
import org.ucb.c5.semiprotocol.ParseSemiprotocol;
import org.ucb.c5.semiprotocol.model.Semiprotocol;
import org.ucb.c5.utils.FileUtils;

/**
 *
 * @author J. Christopher Anderson
 */
public class SemiprotocolPriceSimulator {

    public void initiate() throws Exception {
        
        //TODO: write me
        
    }

    public double run(Semiprotocol protocol) throws Exception {
        
        //TODO: write me
        
        return 3;
    }
    
    public static void main(String[] args) throws Exception {
        //Read in the example semiprotocol
        String text = FileUtils.readResourceFile("semiprotocol/data/alibaba_semiprotocol.txt");
        ParseSemiprotocol parser = new ParseSemiprotocol();
        parser.initiate();
        Semiprotocol protocol = parser.run(text);
        
        SemiprotocolPriceSimulator sim = new SemiprotocolPriceSimulator();
        sim.initiate();
        double price = sim.run(protocol);
        System.out.println("$" + price);
    }
}
