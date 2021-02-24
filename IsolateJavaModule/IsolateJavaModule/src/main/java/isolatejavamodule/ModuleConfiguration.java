package isolatejavamodule;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

public class ModuleConfiguration {
    
    private String isolateExePath = null;
    private String javaJdkPath = null;
    private String mainFile = "Main.java";
    private String mainClass = "Main";
    
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
    
    
    public boolean setJavaJdkPath(String path) {
        File jdkDir = new File(path);
        if (jdkDir.exists() && jdkDir.isDirectory() && javaJdkDirExtraCheck(jdkDir)) {
            javaJdkPath = path;
            return true;
        }
        else {
            // TODO
            System.out.println("Invalid java jdk choice.");
            return false;
        }
    }
    
    public String getJavaJdkPath() {
        return javaJdkPath;
    }
    
    
    public boolean setMainJavaFile(String path) {
        mainFile = path;
        mainClass = mainFile.replace(".java", "");
        return true;
    }
    
    public String getMainFile() {
        return mainFile;
    }
    
    public String getMainClass() {
        return mainClass;
    }
    
    
    public boolean validate() {
        return isolateExePath != null
            && javaJdkPath != null 
            && mainFile != null 
            && mainClass != null 
            && !mainFile.equals("") 
            && !mainClass.equals("")
            && memLimit > 0
            && timeLimit > 0;
    }
    
    

    private boolean javaJdkDirExtraCheck(File jdkDir) {
        List<File> jdkFiles = Arrays.asList(jdkDir.listFiles());
        for (File jf : jdkFiles) {
            if (jf.isDirectory() && jf.getName().equals("bin")) {
                List<File> binFiles = Arrays.asList(jf.listFiles());
                
                boolean javacFound = false, javaFound = false;
                for (File bf : binFiles ) {
                    if (bf.isFile() && bf.canExecute() && bf.getName().equals("java")) {
                        javaFound = true;
                    }
                    if (bf.isFile() && bf.canExecute() && bf.getName().equals("javac")) {
                        javacFound = true;
                    }
                    if (javaFound && javacFound) {
                        return true;
                    }
                }
                
            }
        }
        return false;
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


	public String exportC() {
        JSONObject obj = new JSONObject();
        obj.put("ip", isolateExePath);
        obj.put("jp", javaJdkPath);
        obj.put("mf", mainFile);
        obj.put("ml", memLimit);
        obj.put("tl", timeLimit);
		return obj.toString();
	}

	public boolean importC(String configStr) {
        JSONObject obj = new JSONObject(configStr);
        setIsolateExePath(obj.getString("ip"));
        setJavaJdkPath(obj.getString("jp"));
        setMainJavaFile(obj.getString("mf"));
        setMemLimit(obj.getLong("ml"));
        setTimeLimit(obj.getLong("tl"));
        return validate();
	}

}
