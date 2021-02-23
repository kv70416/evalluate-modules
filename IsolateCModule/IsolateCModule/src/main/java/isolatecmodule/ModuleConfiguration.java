package isolatecmodule;

import java.io.File;

public class ModuleConfiguration {
    
    private String isolateExePath = null;
    private String gccPath = null;
    private String ldPath = null;

    private long memLimit = 512;
    private long timeLimit = 1;
    
    public boolean setIsolateExePath(String path) {
        File isoExe = new File(path);
        if (isoExe.exists() && isoExe.isFile() && isoExe.canExecute()) {
            isolateExePath = path;
            return true;
        }
        else {
            // TODO
            System.out.println("Invalid iso exe choice.");
            return false;
        }
    }
    
    public String getIsolateExePath() {
        return isolateExePath;
    }
    
    
    public boolean setGccPath(String path) {
        File f = new File(path);
        if (f.exists() && f.canExecute()) {
            gccPath = path;
            return true;
        }
        return false;
    }
    
    public String getGccPath() {
        return gccPath;
    }
    
    
    public boolean setLdPath(String path) {
        File f = new File(path);
        if (f.exists() && f.canExecute()) {
            ldPath = path;
            return true;
        }
        return false;
    }
    
    public String getLdPath() {
        return ldPath;
    }


    public long getMemLimit() {
        return memLimit;
    }

    public void setMemLimit(long memLimit) {
        this.memLimit = memLimit;
    }

    public long getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(long timeLimit) {
        this.timeLimit = timeLimit;
    }


    public boolean validate() {
        return isolateExePath != null
            && gccPath != null 
            && ldPath != null 
            && memLimit > 0 
            && timeLimit > 0;
    }
    
}
