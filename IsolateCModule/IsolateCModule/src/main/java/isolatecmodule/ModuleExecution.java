package isolatecmodule;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleExecution {
    
    private Map<String, Integer> studentIDs = null;
    private Map<String, String> studentBoxPaths = null;

    public boolean initialize(List<String> students, ModuleConfiguration config) {
        studentIDs = new HashMap<>();
        studentBoxPaths = new HashMap<>();

        boolean success = true;

        for (int i = 0; i < students.size(); i++) {
            ProcessBuilder isolateInit = new ProcessBuilder(
                config.getIsolateExePath(),
                "--cg", "-p",
                "-b", Integer.toString(i),
                "--init"
            );

            ProcOutcomes initOutcomes = runProcessWithOutput(isolateInit, "ISOLATE INIT FAILED");

            if (initOutcomes.returnValue == 0) {
                try {
                    String boxPath = initOutcomes.outputReader.readLine();

                    studentIDs.put(students.get(i), i);
                    studentBoxPaths.put(students.get(i), boxPath + "/box");
                    
                    File studentSrcDir = Paths.get(boxPath, "box", students.get(i)).toFile();
                    success = success && studentSrcDir.mkdir();    
                }
                catch (IOException e) {
                    outputExceptionError("ISOLATE BOX PATH UNKNOWN", e);
                    success = false;
                }
            }
            else {
                success = false;
            }

        }

        return success;
    }
    
    public boolean compile(String student, ModuleConfiguration config) {
        File srcDir = new File(studentBoxPaths.get(student));
        List<String> allSrcFiles = findSrcFilesInDir(srcDir, srcDir);
        
        List<String> processSegments = new ArrayList<>(Arrays.asList(
            config.getIsolateExePath(),
            "--cg", "-p",
            "-b", studentIDs.get(student).toString(),
            "-d", config.getGccPath(),
            "-d", config.getLdPath(),
            "--env=PATH=" + Paths.get(config.getLdPath()).getParent().toAbsolutePath().toString(),
            "--run", "--", config.getGccPath(), "-o", "exe"
        ));
        
        processSegments.addAll(allSrcFiles);
        
        ProcessBuilder isolateCompileStudentSource = new ProcessBuilder(processSegments);
        
        int ret = runProcess(isolateCompileStudentSource, "ISOLATE COMPILATION FAILED");
        
        return ret == 0;
    }
    
    private List<String> findSrcFilesInDir(File dir, File base) {
        if (dir.isDirectory()) {
            List<String> ret = new ArrayList<>();
            for (File f : dir.listFiles()) {
                if (f.isDirectory()) {
                    ret.addAll(findSrcFilesInDir(f, base));
                }
                else {
                    List<String> splitName = Arrays.asList(f.getName().split("\\."));
                    String extension = splitName.get(splitName.size() - 1);
                    if (extension.equals("c") || extension.equals("C") ||
                            extension.equals("h") || extension.equals("H")) {
                        Path baseDirPath = Paths.get(base.getAbsolutePath());
                        Path srcFilePath = Paths.get(f.getAbsolutePath());
                        String srcFileRelPath = baseDirPath.relativize(srcFilePath).toString();
                        ret.add(srcFileRelPath);
                    }
                }
            }
            return ret;
        }
        else {
            return null;
        }
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
                "-i", "in",
                "-o", "out",
                "--cg-mem=" + Long.toString(config.getMemLimit() * 1024),
                "-t", Long.toString(config.getTimeLimit()),
                "-w", Long.toString(3 * config.getTimeLimit()),
                "--run", "--", "exe"
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
