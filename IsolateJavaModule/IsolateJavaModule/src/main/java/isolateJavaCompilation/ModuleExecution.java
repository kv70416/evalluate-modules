package isolateJavaCompilation;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleExecution {
    
    private Map<String, Integer> studentIDs = null;
    private Map<String, String> studentBoxPaths = null;

    public boolean initialize(List<String> students, ModuleConfiguration config) {
        studentIDs = new HashMap<>();
        studentBoxPaths = new HashMap<>();

        for (int i = 0; i < students.size(); i++) {
            ProcessBuilder isolateInit = new ProcessBuilder(
                config.getIsolateExePath(),
                "--cg", "-p",
                "-b", Integer.toString(i),
                "--init"
            );

            ProcOutcomes initOutcomes = runProcessWithOutput(isolateInit, "ISOLATE INIT FAILED");

            if (initOutcomes.returnValue == 0) {
                String boxPath = null;
                try {
                    boxPath = initOutcomes.outputReader.readLine();
                }
                catch (IOException e) {
                    outputExceptionError("ISOLATE BOX PATH UNKNOWN", e);
                }
                studentIDs.put(students.get(i), i);
                studentBoxPaths.put(students.get(i), boxPath + "/box");
                
                File studentSrcDir = Paths.get(boxPath, "box", students.get(i)).toFile();
                studentSrcDir.mkdir();
            }

        }

        return true;
    }
    
    public boolean compile(String student, ModuleConfiguration config) {
        File mainF = new File(studentBoxPaths.get(student), student + "/" + config.getMainFile());
        if (!mainF.exists()) {
            // TODO
            System.out.println("MAIN FILE NOT FOUND");
            return false;
        }
        
        ProcessBuilder isolateCompileStudentSource =
            new ProcessBuilder(
                config.getIsolateExePath(),
                "--cg", "-p",
                "-b", studentIDs.get(student).toString(),
                "-d", config.getJavaJdkPath(),
                "--run", "--", config.getJavaJdkPath() + "/bin/javac", "-cp", student, student + "/" + config.getMainFile()
            );

        int ret = runProcess(isolateCompileStudentSource, "ISOLATE COMPILATION FAILED");
        
        return ret == 0;
    }
    
    public boolean execute(String student, ModuleConfiguration config) {
        if (!createEmptyFile(studentBoxPaths.get(student) + "/out")) {
            return false;
        }

        ProcessBuilder isolateRunStudentSolution =
            new ProcessBuilder(
                config.getIsolateExePath(),
                "--cg", "-p",
                "-b", studentIDs.get(student).toString(),
                "-d", config.getJavaJdkPath(),
                "-i", "in",
                "-o", "out",
                "--run", "--", config.getJavaJdkPath() + "/bin/java", "-cp", student, config.getMainClass()
            );

        int ret = runProcess(isolateRunStudentSolution, "ISOLATE EXECUTION FAILED");

        return ret == 0;
    }
    
    public boolean cleanUp(List<String> students, ModuleConfiguration config) {
        boolean success = true;

        for (int i = 0; i < students.size(); i++) {
            ProcessBuilder isolateCleanUp = new ProcessBuilder(
                config.getIsolateExePath(),
                "--cg", "-p",
                "-b", Integer.toString(i),
                "--cleanup"
            );

            int ret = runProcess(isolateCleanUp, "ISOLATE CLEANUP FAILED");

            success = success && (ret == 0);
        }

        return success;
    }
    
    
    public String getStudentBoxSrcPath(String student) {
        return Paths.get(studentBoxPaths.get(student), student).toString();
    }
    
    public String getStudentBoxInPath(String student) {
        return Paths.get(studentBoxPaths.get(student), "in").toString();
    }
    
    public String getStudentBoxOutPath(String student) {
        return Paths.get(studentBoxPaths.get(student), "out").toString();
    }
    
    
    
    private int runProcess(ProcessBuilder pb, String errorMsg) {
        int ret = -1;
        try {
            Process proc = pb.start();
            ret = proc.waitFor();
        }
        catch (IOException | InterruptedException e) {
            outputExceptionError(errorMsg, e);
        }
        return ret;
    }

    private ProcOutcomes runProcessWithOutput(ProcessBuilder pb, String errorMsg) {
        int ret = -1;
        BufferedReader output = null;
        try {
            Process proc = pb.start();
            output = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            ret = proc.waitFor();
        }
        catch (IOException | InterruptedException e) {
            outputExceptionError(errorMsg, e);
        }
        return new ProcOutcomes(ret, output);
    }

    private void outputExceptionError(String errorMsg, Exception e) {
        System.out.println(errorMsg + ": " + e.getMessage());
    }
    
    
    private boolean createEmptyFile(String path) {
        File outf = new File(path);
        try {
            outf.createNewFile();
        }
        catch (IOException e) {
            outputExceptionError("OUTPUT FILE CREATION FAILED", e);
            return false;
        }
        return true;
    }

    
    private class ProcOutcomes {
        int returnValue = -1;
        BufferedReader outputReader = null;

        private ProcOutcomes(int ret, BufferedReader br) {
            returnValue = ret;
            outputReader = br;
        }
    }

}
