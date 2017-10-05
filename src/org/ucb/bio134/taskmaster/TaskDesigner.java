package org.ucb.bio134.taskmaster;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.ucb.c5.constructionfile.ParseConstructionFile;
import org.ucb.c5.constructionfile.model.ConstructionFile;
import org.ucb.c5.semiprotocol.model.Semiprotocol;
import org.ucb.c5.utils.FileUtils;

/**
 * Inputs a bag of Construction Files and calculates the Semiprotocols
 * to implement them efficiently
 * 
 * Run's output:
 * 
 * Semiprotocol[0] =  the PCR Semiprotocol
 * Semiprotocol[1] =  the Digestion Semiprotocol
 * Semiprotocol[2] =  the Ligation Semiprotocol
 * 
 * @author J. Christopher Anderson
 */
public class TaskDesigner {

    public void initiate() throws Exception {
        
        //TODO:  write me
        
    }

    public Semiprotocol[] run(List<ConstructionFile> cfiles) throws Exception {
        
        //TODO:  write me
        
        return null;
    }
    
    public static void main(String[] args) throws Exception {        
        ParseConstructionFile parser = new ParseConstructionFile();
        parser.initiate();
        
        //Read in the bag of construction files
        File dir = new File("insert the path to the protocols");
        List<ConstructionFile> cfiles = new ArrayList<>();
        for(File afile : dir.listFiles()) {
            String data = FileUtils.readFile(afile.getAbsolutePath());
            ConstructionFile constf = parser.run(data);
            cfiles.add(constf);
        }
        
        //Run the designer
        TaskDesigner designer = new TaskDesigner();
        designer.initiate();
        
        Semiprotocol[] semis = designer.run(cfiles);
        
        //Price out the designed protocols
        double totalCost = 0.0;
        SemiprotocolPriceSimulator sim = new SemiprotocolPriceSimulator();
        sim.initiate();
        
        for(Semiprotocol protocol : semis) {
            double price = sim.run(protocol);
            totalCost += price;
        }
        
        System.out.println("$" + totalCost);
    }
}
