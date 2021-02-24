package pdfresmodule;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.concurrent.Callable;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import javafx.scene.Node;
import javafx.stage.Stage;
import mainapp.modules.interfaces.IResultExportModule;

public class PdfResultExportModule implements IResultExportModule {

    private String exportPath = null;
    private IResultAccess resultAccessInterface = null;

    @Override
    public boolean isConfigured() {
        return true;
    }

    @Override
    public Node moduleGUI(Stage arg0, Runnable arg1) {
        return null;
    }

    @Override
    public void setExportPath(String path) {
        exportPath = path;
    }

    @Override
    public void setAccessInterface(IResultAccess access) {
        resultAccessInterface = access;
    }

    @Override
    public boolean export() {
        try (
            PDDocument doc = new PDDocument();
        ) {
            PDDocumentInformation info = doc.getDocumentInformation();
            info.setAuthor("EvAlluate");
            info.setTitle("Evaluation Results");
            info.setCreationDate(GregorianCalendar.getInstance());
    
            addData(doc);
            doc.save(exportPath);
        }
        catch (IOException e) {
            return false;
        }

        return true;
    }


    private PDPage page;
    private float currHeight;

    private void addData(PDDocument doc) throws IOException {
        page = new PDPage(PDRectangle.A4);
        currHeight = page.getMediaBox().getHeight() - 70;

        appendContent(doc, () -> PdfContentDrawer.writeSubtitle(doc, page, currHeight, "Scores:"));
        for (String student : resultAccessInterface.getScoredStudents()) {
            double score = resultAccessInterface.getStudentScore(student);
            if (!Double.isNaN(score)) {
                appendContent(doc, () -> PdfContentDrawer.writeStudentScore(doc, page, currHeight, student, score));
            }
            else {
                String msg = resultAccessInterface.getStudentMessage(student);
                appendContent(doc, () -> PdfContentDrawer.writeStudentMessage(doc, page, currHeight, student, msg));
            }
            for (String module : resultAccessInterface.getScoringModules(student)) {
                double modScore = resultAccessInterface.getStudentModuleScore(student, module);
                if (modScore != Double.NaN) {
                    appendContent(doc, () -> PdfContentDrawer.writeModuleScore(doc, page, currHeight, module, modScore));
                }
                for (String test : resultAccessInterface.getScoringTests(student, module)) {
                    double testScore = resultAccessInterface.getStudentTestScore(student, module, test);
                    String testMsg = resultAccessInterface.getStudentTestMessage(student, module, test);
                    appendContent(doc, () -> PdfContentDrawer.writeTestCase(doc, page, currHeight, test, testScore, testMsg));
                }
            }
        }

        doc.addPage(page);
    }

    private void appendContent(PDDocument doc, Callable<Float> writeFunc) {
        try {
            currHeight = writeFunc.call();
            if (currHeight == 0) {
                doc.addPage(page);
                page = new PDPage(PDRectangle.A4);
                currHeight = page.getMediaBox().getHeight() - 70;
                currHeight = writeFunc.call();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    @Override
    public String exportConfiguration() {
        return "";
    }

    @Override
    public boolean importConfiguration(String arg0) {
        return isConfigured();
    }

}
