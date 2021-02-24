package gitlabmodule;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mainapp.modules.interfaces.IFileFetchingModule;

public class GitLabFetchingModule implements IFileFetchingModule {

    private String repoID = null;
    private String token = null;
    private int maxBytesPerStudent = 1048576;

    @Override
    public Node moduleGUI(Stage mainWindow, Runnable mainSceneRefresh) {
        if (mainWindow == null) {
            return null;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setController(new SubMenuGUIController(this, mainWindow, mainSceneRefresh));
        loader.setLocation(getClass().getResource("/gitlabmodulegui/SubMenuGUI.fxml"));

        try {
            VBox submenuGUI = loader.<VBox>load();
            return submenuGUI;
        } catch (IOException e) {
            // TODO
            System.out.println("Module GUI load error:" + e.getMessage());
            return null;
        }
    }

    public void setRepoID(String id) {
        repoID = id;
    }

    public void setToken(String t) {
        token = t;
    }

    public void setMaxMBPerStudent(double mb) {
        maxBytesPerStudent = (int) Math.round(Math.ceil(mb * 1024 * 1024));
    }

    @Override
    public boolean isConfigured() {
        return repoID != null && repoID != "" && maxBytesPerStudent > 0;
    }


    private Map<String, Long> forkIDs = null;
    private Map<String, Long> forkSizes = null;

    @Override
    public List<String> getAllStudents() {
        forkIDs = new HashMap<>();
        forkSizes = new HashMap<>();
        List<String> ret = new ArrayList<>();
        try {
            URL url = new URL("https://gitlab.com/api/v4/projects/" + repoID + "/forks?statistics=true");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            if (token != null && token != "") {
                conn.setRequestProperty("PRIVATE-TOKEN", token);
            }

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String out = sb.toString();
            reader.close();

            JSONArray forks = new JSONArray(out);
            for (int i = 0; i < forks.length(); i++) {
                long forkID = forks.getJSONObject(i).getLong("id");
                String owner = forks.getJSONObject(i).getJSONObject("owner").getString("username");
                long size = forks.getJSONObject(i).getJSONObject("statistics").getLong("repository_size");

                forkIDs.put(owner, forkID);
                forkSizes.put(owner, size);
                ret.add(owner);
            }

            return ret;
        } catch (IOException e) {
            return null;
        }
    }


    private int fetchCounter = 0;

    @Override
    public boolean fetchSourceFiles(String student, String targetSourcePath) {
        if (forkSizes.get(student) > maxBytesPerStudent) {
            return false;
        }

        if (fetchCounter >= 5) {
            try {
                TimeUnit.SECONDS.sleep(90);
            } catch (InterruptedException e) {
                return false;
            }
            fetchCounter = 0;
        }

        try {
            fetchCounter++;

            URL url = new URL("https://gitlab.com/api/v4/projects/" + Long.toString(forkIDs.get(student)) + "/repository/archive");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("accept", "*/*");
            if (token != null && token != "") {
                conn.setRequestProperty("PRIVATE-TOKEN", token);
            }

            InputStream is = conn.getInputStream();
            TarArchiveInputStream tarIn = new TarArchiveInputStream(new GzipCompressorInputStream(is));

            TarArchiveEntry entry;
            while((entry = tarIn.getNextTarEntry()) != null) {
                Path ep = Paths.get(entry.getName());
                if (ep.getNameCount() < 2) {
                    continue;
                }
                Path p = ep.subpath(1, ep.getNameCount());
                File f = Paths.get(targetSourcePath, p.toString()).toFile();
                if (entry.isDirectory()) {
                    if (!f.isDirectory() && !f.mkdirs()) {
                        return false;
                    }
                }
                else {
                    File pf = f.getParentFile();
                    if (!pf.isDirectory() && !pf.mkdirs()) {
                        return false;
                    }
                    OutputStream o = Files.newOutputStream(f.toPath());
                    IOUtils.copy(tarIn, o);
                    o.close();
                }
            }

            tarIn.close();

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public String exportConfiguration() {
        JSONObject obj = new JSONObject();
        obj.put("id", repoID);
        obj.put("sz", maxBytesPerStudent);
        return obj.toString();
    }

    @Override
    public boolean importConfiguration(String configString) {
        try {
            JSONObject configObj = new JSONObject(configString);
            setRepoID(configObj.getString("id"));
            maxBytesPerStudent = configObj.getInt("sz");
        }
        catch (JSONException e) {
            return false;
        }

        Stage dialog = new Stage();
        dialog.setTitle("GitLab Fork Fetch Configuration");
        //dialog.initModality(Modality.WINDOW_MODAL);
        //dialog.initOwner(mainWindow);

        FXMLLoader resLoader = new FXMLLoader();
        resLoader.setController(new TokenConfigWindowController(this));
        resLoader.setLocation(getClass().getResource("/gitlabmodulegui/TokenConfigWindowGUI.fxml"));

        try {
            Pane pane = resLoader.<Pane>load();
            dialog.setScene(new Scene(pane));
            dialog.sizeToScene();
            dialog.showAndWait();
        }
        catch (IOException e) {
            return false;
        }

        return isConfigured();
    }

}
