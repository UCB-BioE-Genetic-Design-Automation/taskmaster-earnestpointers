package org.ucb.bio134.taskmaster;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.ucb.c5.constructionfile.ParseConstructionFile;
import org.ucb.c5.constructionfile.model.ConstructionFile;
import org.ucb.c5.semiprotocol.model.Semiprotocol;
import org.ucb.c5.utils.FileUtils;

/**
 * This is the executable that would run the TaskDesigner and then
 * instantiate the PipetteAid and update the Inventory
 * 
 * It is not needed for the project at hand, so it is not fully implemented
 * 
 * You do not need to work on this class
 * 
 * @author J. Christopher Anderson
 */
public class TaskMaster {
    
    public static final String path = "Insert path to Dropbox WorkList directory";
    
    public static void main(String[] args) throws Exception {
        ParseConstructionFile parser = new ParseConstructionFile();
        parser.initiate();
        
        //Parse the construction files in the "worklist" directory containing Experiments
        File worklist = new File(path);
        List<ConstructionFile> cfiles = new ArrayList<>();
        for(File afile : worklist.listFiles()) {
            File constDir = new File(afile.getAbsoluteFile() + "/construction");
            for(File cfile : constDir.listFiles()) {
                String data = FileUtils.readFile(cfile.getAbsolutePath());
                ConstructionFile constf = parser.run(data);
                cfiles.add(constf);
            }
        }
        
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
