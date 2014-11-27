import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class OSFuncs {

    public static void BootLoader(ArrayList<PCB> hd, String path){
        try{
            //Standard buffered reader format, will support larger files than standard reader streams
            //Credit: Java official documents, StackExchange

            String line;
            ArrayList<String> input = new ArrayList<String>();
            File file = new File(path);
            BufferedReader br = new BufferedReader(new FileReader(file));

            while((line = br.readLine()) != null){
                input.add(line);
            }
            br.close();

            if(!input.isEmpty()){
                String[] jobChars = input.get(0).split(", ");
                PCB tempPCB = new PCB(jobChars[0], Integer.parseInt(jobChars[1]), Integer.parseInt(jobChars[2]));

                for(int x = 1; x < input.size(); x++){
                    if(input.get(x).startsWith("Job")){
                        hd.add(tempPCB);
                        jobChars = input.get(x).split(", ");

                        tempPCB = new PCB(jobChars[0], Integer.parseInt(jobChars[1]), Integer.parseInt(jobChars[2]));
                    }else{tempPCB.addIns(input.get(x));}
                }
                hd.add(tempPCB);
            }
        }catch(IOException e){System.out.print("Bootloader IO Error: " + e.getMessage()+"\n");}

    }

    public static void lts(String method, ArrayList<PCB> hd, int currRamSize, ArrayList<PCB> rdyQ){
        PCB toAdd = hd.get(0);
        //System.out.println(hd.get(0));
        int index = 0;
        switch(method){
            case "SJF":
                boolean foundJob;
                boolean sjfDone = false;

                while(!sjfDone){
                    foundJob = false;
                    for(int i = 0; i < hd.size(); i++){
                        if((hd.get(i).size() + currRamSize) <= 100 && toAdd.size() > hd.get(i).size()){
                            toAdd = hd.get(i);
                            index = i;
                            foundJob = true;
                        }
                    }

                    if(foundJob){
                        currRamSize += hd.get(index).size();
                        rdyQ.add(hd.remove(index));
                        toAdd = hd.get(0);
                    }else{
                        rdyQ.add(hd.remove(0));
                        sjfDone = true;}
                }

                break;
            case "FCFS":
                break;
            case "Priority":
                break;
            default:
                System.out.println("Error in lts");
                break;
        }
    }

    public static void sts(String method, ArrayList<PCB> rdyQ, Core c){
        PCB toAssign = rdyQ.get(0);
        int index = 0;
        switch(method){
            case "SJF":
                for(int i=0;i< rdyQ.size(); i++){
                    if(rdyQ.get(i).size() < toAssign.size()){
                        toAssign = rdyQ.get(i);
                        index = i;
                    }
                }
                c.currentJob = rdyQ.remove(index);
                break;
            case "Priority":
                break;
            case "FCFS":
                break;
            default:
                System.out.println("Error: Invalid STS Method");
                break;
        }
    }

    public static void updatePositions(ArrayList<PCB> rdyQ, ArrayList<PCB> ioQ, ArrayList<PCB> waitQ,int index){
        for(int x = 0; x < rdyQ.size(); x++){
            if(rdyQ.get(x).pos > index){
                rdyQ.get(x).pos--;
            }
        }

        for(int x = 0; x < ioQ.size(); x++){
            if(ioQ.get(x).pos > index){
                ioQ.get(x).pos--;
            }
        }

        for(int x = 0; x < waitQ.size(); x++){
            if(waitQ.get(x).pos > index){
                waitQ.get(x).pos--;
            }
        }
    }

    public static void decWaitQs(ArrayList<PCB> ioQ, ArrayList<PCB> waitQ, ArrayList<PCB> rdyQ){
        for(int x = 0; x < ioQ.size();x++){
            ioQ.get(x).decWait();

            if(ioQ.get(x).getWait() < 1){
                rdyQ.add(ioQ.remove(x));
            }
        }

        for(int i = 0; i < waitQ.size();i++){
            waitQ.get(i).decWait();

            if(waitQ.get(i).getWait() < 1){
                rdyQ.add(waitQ.remove(i));
            }
        }
    }

    public static int getRamSize(ArrayList<PCB> ram){
        int size = 0;
        for(int i = 0; i < ram.size(); i++){
            size += ram.get(i).size();
        }

        return size;
    }

    public static void dumpResults(ArrayList<PCB> termQ){
        while(!termQ.isEmpty()){
            System.out.println(termQ.remove(0));
        }
    }
}
