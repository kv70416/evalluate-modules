package filefetching;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mainapp.moduleInterfaces.IFileFetchingModule;

public class LocalFileFetchingModule implements IFileFetchingModule {
    
    private String selectedPath = null;
    
    @Override
    public Node moduleGUI(Stage mainWindow, Runnable mainSceneRefresh) {
        if (mainWindow == null) {
            return null;
        }
        
        FXMLLoader loader = new FXMLLoader();
        loader.setController(new SubMenuGUIController(this, mainWindow, mainSceneRefresh));
        loader.setLocation(getClass().getResource("/filefetching/SubMenuGUI.fxml"));
        
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
    
    @Override
    public boolean isConfigured() {
        if (selectedPath == null) {
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
    
    
    
    private int maxBytes = 102400;
    
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
}
