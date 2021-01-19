package levenshteinModule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mainapp.moduleInterfaces.IDuplicateDetectionModule;

public class LevenshteinModule implements IDuplicateDetectionModule {

    @Override
    public Node moduleGUI(Stage mainWindow, Runnable mainSceneRefresh) {
        if (mainWindow == null) {
            return null;
        }
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/levenshteinGUI/SubMenuGUI.fxml"));
        
        try {
            VBox submenuGUI = loader.<VBox>load();
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
        return true;
    }
    
    
    Map<String, Map<String, Double>> detectionScores = new HashMap<>();

    @Override
    public boolean runDuplicateDetection(List<String> students, List<String> studentSourcePaths) {
        for (int i = 0; i < students.size(); i++) {
            for (int j = 0; j < students.size(); j++) {
                if (i >= j) continue;
                
                String student1 = students.get(i);
                String student2 = students.get(j);
                
                String student1Path = studentSourcePaths.get(i);
                String student2Path = studentSourcePaths.get(j);

                double score = calculateDistance(student1Path, student2Path);
                
                if (!detectionScores.containsKey(student1))
                    detectionScores.put(student1, new HashMap<>());
                if (!detectionScores.containsKey(student2))
                    detectionScores.put(student2, new HashMap<>());
                
                detectionScores.get(student1).put(student2, score);
                detectionScores.get(student2).put(student1, score);
            }
        }
        return true;
    }

    private double calculateDistance(String path1, String path2) {
        List<File> files1 = allFilesIn(new File(path1));
        List<File> files2 = allFilesIn(new File(path2));

        long size = Math.max(totalSize(files1), totalSize(files2));

        long compRate = Math.max(size / 200, 1);
        long epsilon = 7;

        String signature1 = compress(files1, compRate, epsilon);
        String signature2 = compress(files2, compRate, epsilon);

        System.out.println("SIGNATURE1: \n" + signature1);
        System.out.println("SIGNATURE2: \n" + signature2);

        double dist = dist(signature1, signature2);
        double len = Math.max(signature1.length(), signature2.length());

        return 1. - (dist / len);
    }

    private List<File> allFilesIn(File f) {
        if (!f.isDirectory()) {
            List<File> ret = new ArrayList<>();
            ret.add(f);
            return ret;
        }
        else {
            List<File> ret = new ArrayList<>();
            for (File subf : f.listFiles()) {
                ret.addAll(allFilesIn(subf));
            }
            return ret;
        }
    }

    private long totalSize(List<File> files) {
        long ret = 0;
        for (File f : files) {
            ret += f.length();
        }
        return ret;
    } 

    private String compress(List<File> files, long compRate, long epsilon) {
        Iterator<File> it = files.iterator();
        try {
            String curr = "";
            String sign = "";
            while (it.hasNext()) {
                File f = it.next();
                BufferedReader reader = new BufferedReader(new FileReader(f));
                
                int r;
                while ((r = reader.read()) != -1) {
                    char c = (char)r;
                    if (Character.isWhitespace(c)) {
                        continue;
                    }
                    if (curr.length() == epsilon) {
                        curr = curr.substring(1, (int)epsilon);
                    }
                    curr = curr + c;

                    int h = curr.hashCode();
                    if (curr.length() == epsilon && h % compRate == (compRate - 1)) {
                        int ch = '0' + (((h % 43) + 43) % 43);
                        sign = sign + (char)ch;
                    }
                }

                reader.close();
            }
            return sign;
        }
        catch (IOException e) {
            return null;
        }
    }


    private int dist(String a, String b) {
        int[][] memo = new int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) {
                    memo[i][j] = j;
                    continue;
                }
                if (j == 0) {
                    memo[i][j] = i;
                    continue;
                }

                memo[i][j] = Math.min(
                    Math.min(memo[i - 1][j] + 1, memo[i][j - 1] + 1),
                    memo[i - 1][j - 1] + (a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1)
                );
            }
        }
        return memo[a.length()][b.length()];
    }
    

    @Override
    public double getStudentTotalDuplicateRating(String student) {
        double ret = 0.;
        for (double score : detectionScores.get(student).values()) {
            ret = Math.max(score, ret);
        }
        return ret;
    }

    @Override
    public Map<String, Double> getStudentDuplicateComparisons(String student) {
        return detectionScores.get(student);
    }

}
