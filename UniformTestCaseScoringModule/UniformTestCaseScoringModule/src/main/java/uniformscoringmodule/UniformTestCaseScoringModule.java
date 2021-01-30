package uniformscoringmodule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mainapp.modules.interfaces.ISolutionScoringModule;

public class UniformTestCaseScoringModule implements ISolutionScoringModule {

    private String testCaseDirectory = null;
    private List<String> testCaseNames = new ArrayList<>();
    private String inFileName = "in";
    private String outFileName = "out";
    
    private int scorePerCase = 1;
    
    private Map<String, Map<String, Integer>> scores = new HashMap<>();
    
    @Override
    public Node moduleGUI(Stage mainWindow, Runnable mainSceneRefresh) {
        if (mainWindow == null) {
            return null;
        }
        
        FXMLLoader loader = new FXMLLoader();
        loader.setController(new SubMenuGUIController(this, mainWindow, mainSceneRefresh));
        loader.setLocation(getClass().getResource("/uniformscoringgui/SubMenuGUI.fxml"));

        try {
            VBox submenuGUI = loader.<VBox>load();
            
            SubMenuGUIController ctrl = loader.<SubMenuGUIController>getController();
            ctrl.setInFileNameFieldValue(inFileName);
            ctrl.setOutFileNameFieldValue(outFileName);
            ctrl.setScorePerCaseFieldValue(scorePerCase);
            
            return submenuGUI;
        }
        catch (IOException e) {
            // TODO
            System.out.println("Module GUI load error:" + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean isConfigured() {
        return testCaseDirectory != null &&
                testCaseNames.size() > 0 &&
                inFileName != null &&
                outFileName != null &&
                !inFileName.equals("") &&
                !outFileName.equals("");
    }
    
    public boolean setTestCaseDir(String path) {
        testCaseDirectory = path;
        refreshTestCaseNames();
        return true;
    }
    
    public boolean setInFileName(String name) {
        inFileName = name;
        return true;
    }
    
    public boolean setOutFileName(String name) {
        outFileName = name;
        return true;
    }
    
    public boolean setScorePerCase(int score) {
        if (score < 1)
            return false;
        
        scorePerCase = score;
        return true;
    }

    public int getScorePerCase() {
        return scorePerCase;
    }
    
        
    @Override
    public List<String> inputSegments() {
        return testCaseNames;
    }


    @Override
    public boolean fetchInputFile(String segmentName, String inputFilePath) {
        // TODO remove
        System.out.println("FETCHING INPUT...");
        
        Path inPath = Paths.get(testCaseDirectory, segmentName, inFileName);
        Path targetInPath = Paths.get(inputFilePath);
        
        // TODO remove
        System.out.println("    " + inPath.toString());
        System.out.println("    " + targetInPath.toString());

        
        try {
            Files.copy(inPath, targetInPath, StandardCopyOption.REPLACE_EXISTING);
            return true;
        }
        catch (IOException e) {
            // TODO remove
            System.out.println("FAIL: " + e.getMessage());
            
            return false;
        }
    }
    
    @Override
    public void addSourceScore(String student, String sourcePath) {
        return;
    }

    @Override
    public void addOutputScore(String student, String segmentName, String actualOutputPath) {
        if (scores.get(student) == null) {
            scores.put(student, new HashMap<>());
        }
        
        File outFile = Paths.get(testCaseDirectory, segmentName, outFileName).toFile();
        File actOutFile = new File(actualOutputPath);
        
        if (!outFile.exists() || !actOutFile.exists()) {
            // TODO remove
            System.out.println("OUTPUT FILE MISSING");
            
            scores.get(student).put(segmentName, 0);
            return;
        }
        
        try {
            BufferedReader expectedOutFile = new BufferedReader(new FileReader(outFile));
            BufferedReader actualOutFile = new BufferedReader(new FileReader(actOutFile));
            
            String expLine, actLine;
            boolean same = true;
            while (true) {
                expLine = expectedOutFile.readLine();
                actLine = actualOutFile.readLine();
                
                if (expLine == null && actLine == null) {
                    break;
                }
                
                if (expLine == null || actLine == null) {
                    same = false;
                    break;
                }
                
                if (!expLine.equals(actLine)) {
                    same = false;
                    break;
                }
            }
            
            if (same) {
                scores.get(student).put(segmentName, scorePerCase);
            }
            else {
                scores.get(student).put(segmentName, 0);
            }

            expectedOutFile.close();
            actualOutFile.close();
            
            return;
        }
        catch (IOException e) {
            scores.get(student).put(segmentName, 0);
            return;
        }
        
    }

    @Override
    public void addFailScore(String student, String segmentName) {
        return;
    }


    @Override
    public double solutionScore(String student) {
        if (scores.get(student) == null) {
            return 0;
        }
        
        int totalScore = 0;
        for (String testName : testCaseNames) {
            if (scores.get(student).get(testName) != null) {
                totalScore += scores.get(student).get(testName);
            }
        }
        return totalScore;
    }
    
    @Override
    public Map<String, Double> segmentScores(String student) {
        if (scores.get(student) == null) {
            return null;
        }
        
        Map<String, Double> scoreList = new HashMap<>();
        for (String testName : testCaseNames) {
            if (scores.get(student).get(testName) != null) {
                scoreList.put(testName, scores.get(student).get(testName).doubleValue());
            }
        }
        return scoreList;
    }

    
    private void refreshTestCaseNames() {
        testCaseNames.clear();
        File tcDir = new File(testCaseDirectory);
        if (tcDir.isDirectory()) {
            for (File tcd : tcDir.listFiles()) {
                if (checkIfValidTestCaseDir(tcd)) {
                    testCaseNames.add(tcd.getName());
                }
            }
        }
    }
    
    private boolean checkIfValidTestCaseDir(File dir) {
        if (!dir.isDirectory())
            return false;
        
        File[] ins = dir.listFiles((d, n) -> {
            return n.equals(inFileName);
        });
        
        File[] outs = dir.listFiles((d, n) -> {
            return n.equals(outFileName);
        });

        return ins.length == 1 && outs.length == 1;
    }

}
