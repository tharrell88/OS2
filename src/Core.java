import java.util.ArrayList;

public class Core {
    public PCB currentJob;

    public Core() {
        this.currentJob = null;
    }

    public void setPCB(PCB p){
        this.currentJob = p;
    }

    public void decodeExecute(ArrayList<PCB> ioQ, ArrayList<PCB> waitQ, ArrayList<PCB> rdyQ,int ram){
        String[] explodeIns = currentJob.next().split(", ");

        String action = explodeIns[1];
        String jobNum = explodeIns[0];
        int reg1 = getRegID(explodeIns[2]);
        int reg2 = getRegID(explodeIns[3]);
        int value = Integer.parseInt(explodeIns[4]);

        execute(action, reg1, reg2, value, ram);
    }

    private int getRegID(String reg){
        int i = -1;
        switch (reg){
            case "A":
            case "a":
                i = 0;
                break;
            case "B":
            case "b":
                i = 1;
                break;
            case "C":
            case "c":
                i = 2;
                break;
            case "D":
            case "d":
                i = 3;
                break;
            default:
                break;
        }
        return i;
    }

    private void execute(String action, int reg1, int reg2, int val, int ram){
        switch(action){
            case "add":
                ram--;
                add(reg1, reg2);
                break;
            case "sub":
                ram--;
                sub(reg1, reg2);
                break;
            case "mul":
                ram--;
                mul(reg1, reg2);
                break;
            case "div":
                ram--;
                div(reg1, reg2);
                break;
            case "_rd":
                ram--;
                ioMove(val);
                break;
            case "_wr":
                ram--;
                ioMove(val);
                break;
            case "_wt":
                ram--;
                wtMove(val);
                break;
            case "sto":
                ram--;
                store(val);
                break;
            case "rcl":
                ram--;
                copyTo(reg1);
                break;
            case "nul":
                ram--;
                currentJob.reset();
                break;
            case "stp":
                ram--;
                stop();
                break;
            case "err":
                err(ram);
                break;
            default:
                System.out.println("ERROR IN ACTION");
                break;
        }
    }

    private void add(int reg1, int reg2){
        currentJob.registers[4] += (currentJob.registers[reg1] + currentJob.registers[reg2]);
    }

    private void sub(int reg1, int reg2){
        currentJob.registers[4] += (currentJob.registers[reg1] - currentJob.registers[reg2]);
    }

    private void mul(int reg1, int reg2){
        currentJob.registers[4] += (currentJob.registers[reg1] * currentJob.registers[reg2]);
    }

    private void div(int reg1, int reg2){
        currentJob.registers[4] += (currentJob.registers[reg2] / currentJob.registers[reg1]);
    }

    private void ioMove(int val){
        currentJob.ioQflag = true;
        currentJob.setWait(val);
    }

    private void wtMove(int val){
        currentJob.waitQFlag = true;
        currentJob.setWait(val);
    }

    private void store(int val){
        currentJob.registers[4] = val;
    }

    private void copyTo(int reg){
        currentJob.registers[reg] = currentJob.registers[4];
    }

    private void stop(){
        currentJob.stopFlag = true;
    }

    public void err(int ram){
        ram -= currentJob.insLeft();
        currentJob.termFlag = true;
    }
}
