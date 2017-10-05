package org.ucb.c5.constructionfile;

import java.util.ArrayList;
import java.util.List;
import org.ucb.c5.constructionfile.model.*;

/**
 *
 * @author J. Christopher Anderson
 */
public class ParseConstructionFile {

    public void initiate() throws Exception {
    }

    public ConstructionFile run(String rawText) throws Exception {
        //Replace common unnessary words
        String text = rawText.replace("to ", "");
        text = text.replace("from ", "");
        text = text.replace("on ", "");
        text = text.replace("with ", "");
        text = text.replace("the ", "");
        text = text.replace(" bp", "");
        text = text.replace("bp", "");
        text = text.replaceAll("\\(", "");
        text = text.replaceAll("\\)", "");

        //Break it into lines
        String[] lines = text.split("\\r|\\r?\\n");
        List<Step> steps = new ArrayList<>();

        //Parse out the name of the plasmid
        String firstLine = lines[0];
        String[] spaced = firstLine.split("\\s+");
        String plasmidName = spaced[1];

        //Process each good line
        for (int i = 1; i < lines.length; i++) {
            String aline = lines[i];

            //Ignore blank lines
            if (aline.trim().isEmpty()) {
                continue;
            }

            //Ignore commented-out lines
            if (aline.startsWith("//")) {
                continue;
            }

            //Try to parse the operation, if fails will throw Exception
            String[] spaces = aline.split("\\s+");
            Operation op = Operation.valueOf(spaces[0]);

            //If past the gauntlet, keep the line
            Step parsedStep = parseLine(op, spaces, plasmidName);
            steps.add(parsedStep);
        }

        return new ConstructionFile(steps);
    }

    private Step parseLine(Operation op, String[] spaces, String plasmidName) throws Exception {
        switch (op) {
            case pcr:
                return createPCR(
                        spaces[1].split(","),
                        spaces[2],
                        spaces[3],
                        spaces[4]);
            case pca:
                return createPCA(
                        spaces[1].split(","),
                        spaces[3]);
            case cleanup:
                return createCleanup(
                        spaces[1],
                        spaces[2]);
            case digest:
                return createDigest(
                        spaces[1],
                        spaces[2].split(","),
                        spaces[3]);
            case ligate:
                return createLigation(
                        spaces[1].split(","),
                        spaces[2]);
            case transform:
                return createTransform(
                        spaces[1],
                        spaces[2].replace(",", ""),
                        spaces[3],
                        plasmidName);
            case acquire:
                return createAcquire(
                        spaces[1].split(","));
            default:
                throw new RuntimeException("Not implemented " + op);
        }

        
    }

    private Step createPCR(String[] oligos, String template, String size, String product) {
        return new PCR(oligos[0], oligos[1], template, product);
    }
    
    private Step createPCA(String[] oligos, String product) {
        List<String> frags = new ArrayList<>();
        for (String frag : oligos) {
            frags.add(frag);
        }
        return new PCA(frags, product);
    }
    
    private Step createCleanup(String substrate, String product) {
        return new Cleanup(substrate, product);
    }

    private Step createDigest(String substrate, String[] enzymes, String product) {
        List<Enzyme> enzList = new ArrayList<>();
        for (String enz : enzymes) {
            Enzyme enzyme = Enzyme.valueOf(enz);
            enzList.add(enzyme);
        }
        return new Digestion(substrate, enzList, product);
    }

    private Step createLigation(String[] fragments, String product) {
        List<String> frags = new ArrayList<>();
        for (String frag : fragments) {
            frags.add(frag);
        }
        return new Ligation(frags, product);
    }

    private Step createTransform(String substrate, String strain, String antibiotic, String product) {
        Antibiotic ab = Antibiotic.valueOf(antibiotic);
        return new Transformation(substrate, strain, ab, product);
    }

    private Step createAcquire(String[] dnas) {
        return new Acquisition(dnas[0]);
    }

    public static void main(String[] args) throws Exception {
        CRISPRExample();
        PCAExample();
    }

    public static void CRISPRExample() throws Exception {
        //Initializze the Function
        ParseConstructionFile parser = new ParseConstructionFile();
        parser.initiate();
        SerializeConstructionFile serilaizer = new SerializeConstructionFile();
        serilaizer.initiate();

        String data = ">Construction of pTarg-amilGFP1\n"
                + "acquire ca4238\n"
                + "acquire ca4239\n"
                + "acquire pTargetF\n"
                + "pcr ca4238,ca4239 on pTargetF	(3927 bp, ipcr)\n"
                + "cleanup ipcr	(pcr)\n"
                + "digest pcr with SpeI,DpnI	(spedig)\n"
                + "cleanup spedig	(dig)\n"
                + "ligate dig	(lig)\n"
                + "transform lig	(DH10B, Spec)";

        //Serialize it back
        ConstructionFile constf = parser.run(data);
        String serial = serilaizer.run(constf);
        System.out.println(serial);
    }

    public static void PCAExample() throws Exception {
        //Initializze the Function
        ParseConstructionFile parser = new ParseConstructionFile();
        parser.initiate();
        SerializeConstructionFile serilaizer = new SerializeConstructionFile();
        serilaizer.initiate();

        String data = ">Construction of synthon1\n"
                + "acquire ca4240\n"
                + "acquire ca4241\n"
                + "acquire ca4263\n"
                + "acquire ca4264\n"
                + "acquire pBca9145\n"
                + "\n"
                + "//Synthesize the gene and cut\n"
                + "pca ca4240,ca4241	(1423 bp, pca)\n"
                + "pcr ca4240,ca4241 on pca	(1445 bp, ipcr)\n"
                + "cleanup ipcr	(ipcrc)\n"
                + "digest ipcrc with EcoRI,BamHI	(iDig)\n"
                + "cleanup iDig	(ins)\n"
                + "\n"
                + "//Amplify the plasmid backbone and cut\n"
                + "pcr ca4263,ca4264 on pBca9145	(2532 bp, vpcr)\n"
                + "cleanup vpcr	(vpcrc)\n"
                + "digest vpcrc with EcoRI,BamHI,DpnI	(vDig)\n"
                + "cleanup vDig	(vec)\n"
                + "\n"
                + "//Ligate and transform\n"
                + "ligate ins,vec	(lig)\n"
                + "transform lig	(DH10B, Spec)";

        //Serialize it back
        ConstructionFile constf = parser.run(data);
        String serial = serilaizer.run(constf);
        System.out.println(serial);
    }

}
