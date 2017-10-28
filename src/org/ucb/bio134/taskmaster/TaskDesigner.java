package org.ucb.bio134.taskmaster;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.ucb.c5.constructionfile.ParseConstructionFile;
import org.ucb.c5.constructionfile.model.*;
import org.ucb.c5.semiprotocol.model.*;
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
 *
 * @author Bryant Luong
 * @author J. Christopher Anderson
 */
public class TaskDesigner {

    private List<Task> pcrSteps;
    private List<Task> digSteps;
    private List<Task> ligSteps;

    private List<Ligation> ligs;
    private List<Digestion> digs;
    private List<PCR> pcrs;

    public void initiate() throws Exception {
       pcrSteps = new ArrayList<>();
       digSteps = new ArrayList<>();
       ligSteps = new ArrayList<>();

       ligs = new ArrayList<>();
       digs = new ArrayList<>();
       pcrs = new ArrayList<>();

    }

    private void addPCR(PCR pcr){
        String olig1T = pcr.getOligo1()+"_tube/A1";
        String olig2T = pcr.getOligo2()+"_tube/A1";
        String templateT = pcr.getTemplate()+"_tube/A1";
        String primer1 = pcr.getOligo1();
        String primer2 = pcr.getOligo2();
        String template = pcr.getTemplate();

        pcrSteps.add(new AddContainer(Container.eppendorf_1p5mL, primer1, olig1T, false));
        pcrSteps.add(new AddContainer(Container.eppendorf_1p5mL, primer2, olig2T, false));
        pcrSteps.add(new AddContainer(Container.eppendorf_1p5mL, primer1, olig1T, false));
        pcrSteps.add(new AddContainer(Container.eppendorf_1p5mL, template, templateT, false));

        pcrSteps.add(new AddContainer(Container.pcr_tube, "10uM_"+primer1, "10uM_"+olig1T, true ));
        pcrSteps.add(new AddContainer(Container.pcr_tube, "10uM_"+primer2, "10uM_"+olig2T, true ));
        pcrSteps.add(new AddContainer(Container.pcr_tube, "20xdil_"+template, "10uM_"+templateT, true ));

        pcrSteps.add(new Dispense(Reagent.water, olig1T, 324.0));
        pcrSteps.add(new Dispense(Reagent.water, olig2T, 298.0));
        pcrSteps.add(new Dispense(Reagent.water, "20xdil_"+template, 9.5));
        pcrSteps.add(new Dispense(Reagent.water, "10uM_"+primer1, 90));
        pcrSteps.add(new Dispense(Reagent.water ,"10uM_"+primer2,90));

        pcrSteps.add(new Transfer(olig1T, "10uM"+olig1T, 10.0));
        pcrSteps.add(new Transfer(olig2T, "10uM"+olig2T, 10.0));
        pcrSteps.add(new Transfer(templateT, "20xdil_"+templateT, 0.5));

        String pcr_name = "pcr/"+primer1+"/"+primer2;
        pcrSteps.add(new AddContainer(Container.pcr_tube, pcr_name, "pcr/A1", true));
        pcrSteps.add(new Dispense(Reagent.water, "pcr/A1", 28.5));
        pcrSteps.add(new Dispense(Reagent.Q5_Polymerase_Buffer_5x, "pcr/A1", 10.0));
        pcrSteps.add(new Dispense(Reagent.dNTPs_2mM, "pcr/A1", 5.0));
        pcrSteps.add(new Dispense(Reagent.Q5_polymerase, "pcr/A1", 0.5));
        pcrSteps.add(new Transfer("10uM"+olig1T, "pcr/A1", 2.5));
        pcrSteps.add(new Transfer("10uM"+olig2T, "pcr/A1", 2.5));
        pcrSteps.add(new Transfer("20xdil_"+templateT, "pcr/A1", 1.0));
    };

    private void addDig(Digestion dig) {
        String substrate = dig.getSubstrate();
        digSteps.add(new Transfer(substrate, "tube/A6", 5.0));
    };

    private void addLig(Ligation lig){

        for (String s: lig.getFragments()){
            ligSteps.add(new Transfer(s, "tube/A7", 1.0));
        }
    };


    private void optpcrprep() throws Exception{

        String oligo1, oligo2, template;
        Map<String, Integer> uniqueOligos = new HashMap<>();
        Map<String, Integer> uniqueTemplates= new HashMap<>();

        // find all unique oligos
        for (PCR pcr: pcrs){
            oligo1 = pcr.getOligo1();
            oligo2 = pcr.getOligo2();
            template = pcr.getTemplate();

            if (uniqueOligos.containsKey(oligo1)){
                uniqueOligos.replace(oligo1, uniqueOligos.get(oligo1) + 1);
            } else {
                uniqueOligos.put(oligo1, 1);
            }

            if (uniqueOligos.containsKey(oligo2)){
                uniqueOligos.replace(oligo2, uniqueOligos.get(oligo2) + 1);
            } else {
                uniqueOligos.put(oligo2, 1);
            }

            if (uniqueTemplates.containsKey(template)){
                uniqueTemplates.replace(template, uniqueTemplates.get(template) + 1);
            } else {
                uniqueTemplates.put(template, 1);
            }
        }
        int numOfUseOligos = 440;
        int counterOligo = 1;
        Map<String, String> oligo2Tube = new HashMap<>();
        Map<String, String> template2Tube = new HashMap<>();

        //prepare mastermix for each unique oligo
        for (Map.Entry<String, Integer> pair: uniqueOligos.entrySet()){
            if (pair.getValue() >= numOfUseOligos){
                throw new Exception("Edge case");
            }

            String oligo = pair.getKey();
            String oTube = oligo + "/A" + counterOligo;

            //acquire step so it tube doesn't cost anything
            pcrSteps.add(new AddContainer(Container.eppendorf_1p5mL, oligo, "100uM"+oTube, false));

            //dilute to 100 uM
            pcrSteps.add(new Dispense(Reagent.water, "100uM"+oTube, 300.0));

            //new tube for current unique oligo
            pcrSteps.add(new AddContainer(Container.eppendorf_1p5mL, "10uM"+oligo, "10uM"+oTube, true));

            //add water to new tube
            pcrSteps.add(new Dispense(Reagent.water, "10uM"+oTube, 990));

            //transfer some volume of 100 uM oligo to new 10 uM tube for to finish preparation for current oligo
            pcrSteps.add(new Transfer(oTube, "10uM"+oTube, 110));

            oligo2Tube.put(oligo, oTube);
            //increase counter for next oligo
            counterOligo++;
        }

        // prepare mastermix for all templates
        int numOfUseTemplates = 440;
        int counterTemplate = 1;
        for (Map.Entry<String, Integer> pair: uniqueTemplates.entrySet()){
            if (pair.getValue() >= numOfUseTemplates){
                throw new Exception("Edge case");
            }

            String curTemplate = pair.getKey();
            String templateT = curTemplate + "/A" + counterTemplate;
            pcrSteps.add(new AddContainer(Container.eppendorf_1p5mL, curTemplate, templateT, false));
            pcrSteps.add(new AddContainer(Container.eppendorf_1p5mL, curTemplate, "20xdil_"+templateT, true));
            pcrSteps.add(new Dispense(Reagent.water, "20xdil_"+templateT, 459.8));
            pcrSteps.add(new Transfer(templateT, "20xdil_"+templateT, 24.2));
            template2Tube.put(curTemplate, "20xdil_"+templateT);
            counterTemplate++;
        }

        // execute PCR
        String source = "pcrMM";
        String destination = "Exp";
        double volPerPCR = 44.0;
        int numOfPCR = pcrs.size();
        double totalBufferVol = volPerPCR * numOfPCR* 1.1;

        if (totalBufferVol < 1400.0) {
            pcrSteps.add(new AddContainer(Container.eppendorf_1p5mL, source, source + "/E1.5mL", true));
        } else {
            pcrSteps.add(new AddContainer(Container.eppendorf_2mL, source, source+"/E2mL", true));
        }

        pcrSteps.add(new Dispense(Reagent.water, source, 1.1 * 28.5 * numOfPCR));
        pcrSteps.add(new Dispense(Reagent.Q5_Polymerase_Buffer_5x, source, 1.1 * 10.0 * numOfPCR));
        pcrSteps.add(new Dispense(Reagent.Q5_polymerase, source, 1.1 * 0.5 * numOfPCR));
        pcrSteps.add(new Dispense(Reagent.dNTPs_2mM, source, 1.1 * 5.0 * numOfPCR));

        pcrSteps.add(new Transfer(source, destination, volPerPCR));
        int numPCR = 1;

        for (PCR pcr: pcrs){
            pcrSteps.add(new AddContainer(Container.pcr_tube, pcr.getProduct(), "/B"+numPCR, true));
            //primer 1
            pcrSteps.add(new Transfer(oligo2Tube.get(pcr.getOligo1()), "/B"+numPCR, 2.5));
            //primer 2
            pcrSteps.add(new Transfer(oligo2Tube.get(pcr.getOligo2()), "/B"+numPCR, 2.5));
            //template
            pcrSteps.add(new Transfer(template2Tube.get(pcr.getTemplate()), "/B"+numPCR, 1.0));
            numPCR++;
        }
    };

    private void optdigs(){
        String source = "DigMM";
        String destination = "Exp";
        double volPerDig = 45.0;
        int numOfDigs = digs.size();
        double totalBufferVol = volPerDig * numOfDigs * 1.1;

        if (totalBufferVol < 1400.0) {
            digSteps.add(new AddContainer(Container.eppendorf_1p5mL, source, source + "/E1.5mL", true));
        } else {
            digSteps.add(new AddContainer(Container.eppendorf_2mL, source, source+"/E2mL", true));
        }

        digSteps.add(new Dispense(Reagent.water, source, 1.1 * 39.0 * numOfDigs));
        digSteps.add(new Dispense(Reagent.NEB_Buffer_2_10x, source, 1.1 * 5.0 * numOfDigs));
        digSteps.add(new Dispense(Reagent.SpeI, source, 1.1 * 0.5 * numOfDigs));
        digSteps.add(new Dispense(Reagent.DpnI, source, 1.1 * 0.5 * numOfDigs));

        digSteps.add(new Transfer(source, destination, volPerDig));
    }

    private void optligs () throws Exception{
        String source = "LigMM";
        String destination = "Exp";
        double volPerLig = 9.0;
        int numOfLigs = ligs.size();
        double totalBufferVol = volPerLig * numOfLigs * 1.1;

        // cost of tubes
        if (totalBufferVol < 1400.0) {
            ligSteps.add(new AddContainer(Container.eppendorf_1p5mL, source, source + "/E1.5mL", true));
        } else if (totalBufferVol < 2000.0){
            ligSteps.add(new AddContainer(Container.eppendorf_2mL, source, source+"/E2mL", true));
        } else {
            throw new IllegalArgumentException("You need a larger volume eppendorf tube.");
        }

        // cost of reagents
        ligSteps.add(new Dispense(Reagent.water, source, 1.1 * 7.5 * numOfLigs));
        ligSteps.add(new Dispense(Reagent.T4_DNA_Ligase_Buffer_10x, source, 1.1 * 1.0 * numOfLigs));
        ligSteps.add(new Dispense(Reagent.T4_DNA_ligase, source, 1.1 * 0.5 * numOfLigs));

        // cost of ligation transfer
        ligSteps.add(new Transfer(source, destination, volPerLig));

    }

    public Semiprotocol[] run(List<ConstructionFile> cfiles) throws Exception {
        Semiprotocol[] semiprotocols = new Semiprotocol[3];
        for (ConstructionFile cf: cfiles){
            for (Step s: cf.getSteps()){
                switch (s.getOperation()){
                    case pcr:
//                        addPCR((PCR) s);
                        pcrs.add((PCR)s);
                        break;
                    case digest:
//                        addDig((Digestion) s);
                        // mastermix
                        digs.add((Digestion) s);
                        break;
                    case ligate:
//                        addLig((Ligation) s);
                        // mastermix
                        ligs.add((Ligation) s);
                        break;
                    default:
                        break;
                }
            }
        }

        optpcrprep();
        optdigs();
        optligs();

        semiprotocols[0] = new Semiprotocol(pcrSteps);
        semiprotocols[1] = new Semiprotocol(digSteps);
        semiprotocols[2] = new Semiprotocol(ligSteps);

        return semiprotocols;
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
