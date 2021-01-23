package isolatejavamodule;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ModuleConfiguration {
    
    private String isolateExePath = null;
    private String javaJdkPath = null;
    private String mainFile = "Main.java";
    private String mainClass = "Main";
    
    
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
                && !mainClass.equals("");
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

}
