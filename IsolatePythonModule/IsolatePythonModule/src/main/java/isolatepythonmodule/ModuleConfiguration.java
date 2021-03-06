package isolatepythonmodule;

import java.io.File;

import org.json.JSONObject;

public class ModuleConfiguration {
    
    private String isolateExePath = null;
    private String pythonPath = null;
    
    private String mainFile = "main.py";
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
    
    
    public boolean setPythonPath(String path) {
        File f = new File(path);
        if (f.exists() && f.canExecute()) {
            pythonPath = path;
            return true;
        }
        return false;
    }
    
    public String getPythonPath() {
        return pythonPath;
    }
    
    
    public boolean setMainFileName(String fileName) {
        mainFile = fileName;
        return true;
    }
    
    public String getMainFileName() {
        return mainFile;
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
            && pythonPath != null 
            && mainFile != null 
            && mainFile != ""
            && memLimit > 0
            && timeLimit > 0;
    }


	public String exportC() {
        JSONObject obj = new JSONObject();
        obj.put("ip", isolateExePath);
        obj.put("pp", pythonPath);
        obj.put("mf", mainFile);
        obj.put("ml", memLimit);
        obj.put("tl", timeLimit);
		return obj.toString();
	}

	public boolean importC(String configStr) {
        JSONObject obj = new JSONObject(configStr);
        setIsolateExePath(obj.getString("ip"));
        setPythonPath(obj.getString("pp"));
        setMainFileName(obj.getString("mf"));
        setMemLimit(obj.getLong("ml"));
        setTimeLimit(obj.getLong("tl"));
        return validate();
	}
}
