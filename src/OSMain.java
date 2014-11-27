import java.util.ArrayList;
import java.util.Scanner;

public class OSMain {
    public static void main(String[] args){
        //Hardware
        ArrayList<PCB> hd = new ArrayList<PCB>();
        int ram = 0;
        Core core1 = new Core();
        int maxRamSize = 100;

        //Queues
        ArrayList<PCB> rdyQ = new ArrayList<PCB>();
        ArrayList<PCB> waitQ = new ArrayList<PCB>();
        ArrayList<PCB> termQ = new ArrayList<PCB>();
        ArrayList<PCB> ioQ = new ArrayList<PCB>();

        //Flags
        boolean done = false;
        boolean ready = false;

        //method strings
        String lts_method = "SJF";
        String sts_method = "SJF";

        //Filepaths
        //CHANGE THIS TO LOCAL FILE. HAVE TO EXPLICITLY TELL JAVA WHERE FILE IS
        String path = "I:\\Users\\swema_000\\Desktop\\OS\\src\\ugradPart2.txt";

        Scanner s = new Scanner(System.in);

        System.out.print("Operating System Emulator\nBy: Travis Harrell\n");

        while(!ready){
            System.out.println("Please select an option from the following list (Enter number):");
            System.out.println("1) Run Emulator"
                    + "\n2) Set LTS method"
                    + "\n3) Set RAM size"
                    + "\n4) Set STS method");

            switch(s.nextInt()){
                //Run emulator
                case 1:
                    System.out.println("Ready. Running Emulator.\n");
                    ready = true;
                    break;
                //select method to use for the Long Term Scheduler
                case 2:
                    System.out.println("Which LTS method would you like to use? (Priority, SJF, and FCFS available)");
                    String t = s.next();
                    if(!t.equalsIgnoreCase("sjf") && !t.equalsIgnoreCase("fcfs") && !t.equalsIgnoreCase("priority")){
                        System.out.println("Invalid method");}
                    else{ lts_method = t; }
                    break;
                //Set max size for RAM
                case 3:
                    System.out.println("What size do you wish to use for the RAM?");
                    maxRamSize = s.nextInt();
                    break;
                case 4:
                    System.out.println("What algorithm should be used for STS? (SJF, Priority, FCFS");
                    String o = s.next();
                    if(!o.equalsIgnoreCase("sjf") && !o.equalsIgnoreCase("fcfs") && !o.equalsIgnoreCase("priority")){
                        System.out.println("Invalid method");}
                    else{ sts_method = o; }
                    break;
                default:
                    System.out.println("Sorry, invalid choice.\n");
                    break;
            }
        }

        //Begin Program
        OSFuncs.BootLoader(hd, path);

        //Enter main loop
        while(!done){
                if(hd.isEmpty() && rdyQ.isEmpty() && waitQ.isEmpty() && ioQ.isEmpty() && core1.currentJob ==null){done = true;}

                if(hd.size() > 0){
                    //System.out.println("derp >> " + ram);
                    OSFuncs.lts(lts_method,hd,ram,rdyQ);
                }

                if(core1.currentJob == null && !rdyQ.isEmpty()){
                    OSFuncs.sts(sts_method, rdyQ, core1);
                }

                if(core1.currentJob != null){
                    core1.decodeExecute(ioQ,waitQ,rdyQ,ram);
                }

                //decrement wait and ioQ
                OSFuncs.decWaitQs(ioQ, waitQ, rdyQ);

                if(core1.currentJob != null){
                    //check to see if current job has anymore instructions left
                    if(core1.currentJob.insLeft() == 0){
                        System.out.println("Moving job to termQ JOB IS DONE");
                        termQ.add(core1.currentJob);
                        core1.currentJob = null;
                    }
                    //check to see if the job needs to be moved to rdyQ, termQ, waitQ, or ioQ
                    else if(core1.currentJob.stopFlag){
                        System.out.println("Moving job to rdyQ");
                        core1.currentJob.stopFlag = false;
                        rdyQ.add(core1.currentJob);
                        core1.currentJob = null;
                    }else if(core1.currentJob.termFlag){
                        System.out.println("Moving job to termQ");
                        termQ.add(core1.currentJob);
                        core1.currentJob = null;
                    }else if(core1.currentJob.waitQFlag){
                        System.out.println("Moving job to waitQ");
                        core1.currentJob.waitQFlag = false;
                        waitQ.add(core1.currentJob);
                        core1.currentJob = null;
                    }else if(core1.currentJob.ioQflag){
                        System.out.println("Moving job to ioQ");
                        core1.currentJob.ioQflag = false;
                        ioQ.add(core1.currentJob);
                        core1.currentJob = null;
                    }
                }else{}
        }

        //while loop is over, dump results
        System.out.print("\nDONE\n");

       /* System.out.println("Fucking stupid core");
        System.out.println(core1.currentJob == null ? "NULL" : core1.currentJob);
        System.out.print("\nRDYQ\n");
        while(!rdyQ.isEmpty()){
            System.out.println(rdyQ.remove(0));
        }

        System.out.print("\nWAITQ\n");
        while(!waitQ.isEmpty()){
            System.out.println(waitQ.remove(0));
        }

        System.out.print("\nIOQ\n");
        while(!ioQ.isEmpty()){
            System.out.println(ioQ.remove(0));
        }

        System.out.print("\nHDD CONTENTS\n");
        System.out.println(">> " + hd.size() + " <<");
        System.out.println(">> " + ram + " <<");*/

        System.out.print("\n\nTERM\n");
        OSFuncs.dumpResults(termQ);
    }
}
