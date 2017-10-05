package org.ucb.bio134.taskmaster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.ucb.c5.constructionfile.model.ConstructionFile;
import org.ucb.c5.semiprotocol.model.Semiprotocol;

/**
 * This is the executable that would run the TaskDesigner and than
 * instantiate the PipetteAid and update the Inventory
 * 
 * It is not needed for the project at hand, so it is not implemented
 * 
 * @author J. Christopher Anderson
 */
public class TaskMaster {
    public static void main(String[] args) throws Exception {
        //Parse the construction files in the "worklist" directory
        List<ConstructionFile> cfiles = new ArrayList<>();
        
        //Run TaskDesigner
        TaskDesigner designer = new TaskDesigner();
        designer.initiate();
        Semiprotocol[] tasks = designer.run(cfiles);
        
        //Simulate the final State of plates after completion
        
        //Run the pipette aid, ask the user in the end to confirm completion
        
        //Serialize the Semiprotocols to the filesystem as TSV for records
        
        //Serialize the new state of modified plates to the Inventory 
    }
}
