import java.util.*;
import java.io.*;

class Main {

    public static void main(String [] args) throws IOException {
        //Reading the input from user
        Scanner x = null;
        int buffer, total;
        ArrayList<Integer> inputArray = new ArrayList<Integer>();
        x = new Scanner(System.in);
        total = Integer.parseInt(x.next());
        inputArray.add(total);
        int threshold = 0;
        while(x.hasNextLine()){
            threshold += 1;
            buffer = Integer.parseInt(x.next());
            inputArray.add(buffer);
            if(threshold == 2 * total * total){
                x.close();
                break;
            }
        }
        class Proposer{
            int wife, id;
            int numOfProposes = 0;
            Boolean engaged = false;
            ArrayList<Integer> preference = new ArrayList<Integer>();
            ArrayList<Integer> ranking = new ArrayList<Integer>();
            public Proposer(int id){
                this.id = id;
                this.wife = -1;
            }
        }
        class Respondent{
            int husband, id;
            Boolean engaged = false;
            ArrayList<Integer> preference = new ArrayList<Integer>();
            ArrayList<Integer> ranking = new ArrayList<Integer>();
            public Respondent(int id){
                this.id = id;
                this.husband = -1;
            }
        }

        //Storing input into data structures
        ArrayList<Proposer> men = new ArrayList<Proposer>();
        ArrayList<Respondent> women = new ArrayList<Respondent>();
        for(int i = 0; i < total; i++){
            men.add(new Proposer(i));
            women.add(new Respondent(i));
        }
        for(int i = 1; i < (2 * total * total + 1); i++){
            if(i < (total * total + 1)){
                int row = (i - 1) / total;
                int col = (i - 1) % total;
                men.get(row).preference.add(inputArray.get(i));
                women.get(inputArray.get(i)).ranking.add(col);
            }
            else if(i > (total * total)){
                int row = (i - 1) / total - total;
                int col = (i - 1) % total;
                women.get(row).preference.add(inputArray.get(i));
                men.get(inputArray.get(i)).ranking.add(col);
            }
        }
        ArrayList<Proposer> freeMen = new ArrayList<Proposer>();
        for(int i = 0; i < total; i++){
            freeMen.add(men.get(i));
        }

        //Stable Matching Algorithm
        int counter = total;
        while(counter > 0) {
            Proposer man = freeMen.get(0);
            Respondent woman = women.get(man.preference.get(man.numOfProposes));
            if (!woman.engaged) {
                man.engaged = true;
                woman.engaged = true;
                man.wife = man.preference.get(man.numOfProposes);
                woman.husband = man.id;
                freeMen.remove(0);
                counter -= 1;
            } else {
                int currentHusband = men.get(woman.husband).ranking.get(woman.id);
                int currentProposer = man.ranking.get(woman.id);
                if (currentHusband > currentProposer) {
                    Proposer oldHusband = men.get(woman.husband);
                    oldHusband.engaged = false;
                    oldHusband.wife = -1;
                    woman.husband = man.id;
                    man.engaged = true;
                    man.wife = man.preference.get(man.numOfProposes);
                    freeMen.remove(0);
                    freeMen.add(oldHusband);
                }
            }
            man.numOfProposes += 1;
        }

        //Storing the original results
        int originalHusband = women.get(men.get(0).preference.get(0)).husband;
        int originalResult = women.get(0).husband;

        //Modification of the Algorithm
        freeMen.clear();
        for(int i = 0; i < total; i++){
            men.get(i).engaged = false;
            men.get(i).wife = -1;
            men.get(i).numOfProposes = 0;
            women.get(i).engaged = false;
            women.get(i).husband = -1;
            freeMen.add(men.get(i));
        }
        counter = total;
        while(counter > 0) {
            Proposer man = freeMen.get(0);
            Respondent woman = new Respondent(-1);
            if(man.numOfProposes < total){
                woman = women.get(man.preference.get(man.numOfProposes));
            }
            else{
                break;
            }
            if (!woman.engaged) {
                if (man.id != 0 || (man.id == 0 && man.numOfProposes > 0)) {
                    man.engaged = true;
                    woman.engaged = true;
                    man.wife = man.preference.get(man.numOfProposes);
                    woman.husband = man.id;
                    freeMen.remove(0);
                    counter -= 1;
                }
            } else {
                int currentHusband = men.get(woman.husband).ranking.get(woman.id);
                int currentProposer = man.ranking.get(woman.id);
                if (currentHusband > currentProposer) {
                    if (man.id != 0 || (man.id == 0 && man.numOfProposes > 0)) {
                        Proposer oldHusband = men.get(woman.husband);
                        oldHusband.engaged = false;
                        oldHusband.wife = -1;
                        woman.husband = man.id;
                        man.engaged = true;
                        man.wife = man.preference.get(man.numOfProposes);
                        freeMen.remove(0);
                        freeMen.add(oldHusband);
                    }
                }
            }
            man.numOfProposes += 1;
        }
        Respondent w = women.get(men.get(0).preference.get(0));
        int secondResult;
        if(!w.engaged){
            secondResult = 1;
        }
        else{
            Proposer m = men.get(w.husband);
            int currentHusbandIdx = m.ranking.get(w.id);
            int originalHusbandIdx = men.get(originalHusband).ranking.get(w.id);
            if(currentHusbandIdx <= originalHusbandIdx){
                secondResult = 3;
            }
            else{
                secondResult = 2;
            }
        }

        //Returning the output to stdout
        System.out.print(originalResult + "\n" + secondResult + "\n");
    }
}