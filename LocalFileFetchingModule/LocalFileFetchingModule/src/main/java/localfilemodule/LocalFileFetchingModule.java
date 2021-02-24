package localfilemodule;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mainapp.modules.interfaces.IFileFetchingModule;

public class LocalFileFetchingModule implements IFileFetchingModule {
    
    private String selectedPath = null;
    private int maxBytesPerStudent = 1048576;
    
    @Override
    public Node moduleGUI(Stage mainWindow, Runnable mainSceneRefresh) {
        if (mainWindow == null) {
            return null;
        }
        
        FXMLLoader loader = new FXMLLoader();
        loader.setController(new SubMenuGUIController(this, mainWindow, mainSceneRefresh));
        loader.setLocation(getClass().getResource("/localfilegui/SubMenuGUI.fxml"));
        
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
    
    public void setSelectedPath(String path) {
        selectedPath = path;
    }

    public void setMaxMBPerStudent(double mb) {
        maxBytesPerStudent = (int) Math.round(Math.ceil(mb * 1024 * 1024));
    }
    
    @Override
    public boolean isConfigured() {
        if (selectedPath == null || maxBytesPerStudent <= 0) {
            return false;
        }
        return Paths.get(selectedPath).toFile().isDirectory();
    }
    
    @Override
    public List<String> getAllStudents() {
        if (!isConfigured()) return new ArrayList<>();
        
        File selectedDir = Paths.get(selectedPath).toFile();
        
        File[] filesInSelectedDir = selectedDir.listFiles();
        
        List<String> studentList = new ArrayList<>();
        for (File file : filesInSelectedDir) {
            if (file.isDirectory()) {
                studentList.add(file.getName());
            }
        }
        return studentList;
    }
    
    
    @Override
    public boolean fetchSourceFiles(String student, String targetSourcePath) {
        System.out.println("Fetching source for: " + student + ".");
        
        File studentDir = Paths.get(selectedPath, student).toFile();
        
        if (studentDir.isDirectory()) {
            return copyFilesInDir(studentDir.toPath(), Paths.get(targetSourcePath));
        }
        
        return false;
    }
    
    
    private boolean copyFilesInDir(Path sourceDirPath, Path targetDirPath) {
        int maxBytes = maxBytesPerStudent;
        File[] sourceFiles = sourceDirPath.toFile().listFiles();
        for (File sourceFile : sourceFiles) {
            Path sourceFilePath = sourceFile.toPath();
            Path targetFilePath = Paths.get(targetDirPath.toString(), sourceFile.getName());
            
            System.out.println(sourceFile.getAbsolutePath() + " : " + sourceFile.length() + " / " + Integer.toString(maxBytes));
            
            if (sourceFile.length() > maxBytes) {
                // TODO
                return false;
            }
            
            try {
                maxBytes -= sourceFile.length();
                Files.copy(sourceFilePath, targetFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
            catch (Exception e) {
                // TODO
                System.out.println(e.getMessage());
                return false;
            }
            
            if (sourceFile.isDirectory()) {
                copyFilesInDir(sourceFilePath, targetFilePath);
            }

        }
        return true;
    }

    @Override
    public String exportConfiguration() {
        JSONObject obj = new JSONObject();
        obj.put("sp", selectedPath);
        obj.put("mb", maxBytesPerStudent);
		return obj.toString();
    }

    @Override
    public boolean importConfiguration(String configStr) {
        JSONObject obj = new JSONObject(configStr);
        setSelectedPath(obj.getString("sp"));
        maxBytesPerStudent = obj.getInt("mb");
        return isConfigured();
    }
}
