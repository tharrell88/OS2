import java.util.ArrayList;

public class PCB {
    public int[] registers;
    public String jobNum;
    private int insRun, wait, jobSize, jobPri;
    public int pos;
    private ArrayList<String> ins;
    public boolean ioQflag, waitQFlag, stopFlag, termFlag;

    public PCB() {
        //empty constructor should never be accessed for data, so leaving this blank
        this.ins = new ArrayList<String>();
    }

    public PCB(String jobNum, int jobSize, int jobPri) {
        this.jobNum = jobNum;
        this.jobSize = jobSize;
        this.jobPri = jobPri;
        this.ioQflag = false;
        this.waitQFlag = false;
        this.stopFlag = false;
        this.termFlag = false;

        this.insRun = 0;
        this.pos = 0;
        this.wait = 0;
        this.insRun = 0;
        this.registers = new int[5];
        this.registers[0] = 1;
        this.registers[1] = 3;
        this.registers[2] = 5;
        this.registers[3] = 7;
        this.registers[4] = 9;

        this.ins = new ArrayList<String>();
    }

    public int insLeft(){
        return this.ins.size();
    }

    public void setWait(int w){
        this.wait = w;
    }

    public int getWait(){
        return this.wait;
    }

    public void decWait(){
        this.wait--;
    }

    public void updateInsRun(){
        this.insRun++;
    }

    public void addIns(String ins){
        this.ins.add(ins);
    }

    public String next(){
        insRun++;
        return ins.remove(0);
    }

    public void reset(){
        this.registers[0] = 1;
        this.registers[1] = 3;
        this.registers[2] = 5;
        this.registers[3] = 7;
        this.registers[4] = 9;
    }

    public int size(){return this.jobSize;}

    public String toString(){
        String insOut = "PCB Data for " + this.jobNum + ": \n"
                + "Size: " + this.jobSize + " Job Priority: " + this.jobPri + "\n"
                + "Register A: " + this.registers[0] + "\n"
                + "Register B: " + this.registers[1] + "\n"
                + "Register C: " + this.registers[2] + "\n"
                + "Register D: " + this.registers[3] + "\n"
                + "Accumulator: " + this.registers[4];

        return insOut;
    }
}
