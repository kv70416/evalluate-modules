package isolatecmodule;

import java.io.File;

public class ModuleConfiguration {
    
    private String isolateExePath = null;
    private String gccPath = null;
    private String ldPath = null;
    
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
    
    
    public boolean validate() {
        return isolateExePath != null && gccPath != null && ldPath != null;
    }
    
}
