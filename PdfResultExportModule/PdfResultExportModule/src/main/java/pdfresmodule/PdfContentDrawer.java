package pdfresmodule;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class PdfContentDrawer {

    public static float writeSubtitle(PDDocument doc, PDPage page, float startHeight, String subtitle) throws IOException {
        if (subtitle == null) return startHeight;
        if (startHeight - 26 < 70) return 0;

        try (
            PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);
        ) {
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
            contentStream.newLineAtOffset(70, startHeight - 26);
            contentStream.showText(subtitle);
            contentStream.endText();
        }

        return startHeight - 26;
    }
    
    public static float writeStudentScore(PDDocument doc, PDPage page, float startHeight, String studentName, double studentScore) throws IOException {
        if (studentName == null) return startHeight;
        if (startHeight - 28 < 70) return 0;

        String scoreText = Double.isNaN(studentScore) ? "---" : String.format("%.2f", studentScore);
        if (scoreText == null) return startHeight;

        try (
            PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);
        ) {
            float pWidth = page.getMediaBox().getWidth();

            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.newLineAtOffset(70, startHeight - 24);
            contentStream.showText("Student " + studentName + ": " + scoreText);
            contentStream.endText();

            contentStream.moveTo(70, startHeight - 28);
            contentStream.lineTo(pWidth - 70, startHeight - 28);
            contentStream.stroke();
        }

        return startHeight - 28;
    }

    public static float writeStudentMessage(PDDocument doc, PDPage page, float startHeight, String studentName, String studentMsg) throws IOException {
        if (studentName == null) return startHeight;
        if (startHeight - 28 < 70) return 0;

        String msgText = (studentMsg == null) ? "---" : studentMsg;

        try (
            PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);
        ) {
            float pWidth = page.getMediaBox().getWidth();

            contentStream.beginText();
            contentStream.newLineAtOffset(70, startHeight - 24);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.showText("Student " + studentName + ": ");
            contentStream.setFont(PDType1Font.HELVETICA_BOLD_OBLIQUE, 14);
            contentStream.showText(msgText);
            contentStream.endText();

            contentStream.moveTo(70, startHeight - 28);
            contentStream.lineTo(pWidth - 70, startHeight - 28);
            contentStream.stroke();
        }

        return startHeight - 28;
    }

    public static float writeModuleScore(PDDocument doc, PDPage page, float startHeight, String moduleName, double moduleScore) throws IOException {
        if (moduleName == null) return startHeight;
        if (startHeight - 24 < 70) return 0;

        String scoreText = Double.isNaN(moduleScore) ? "---" : String.format("%.2f", moduleScore);
        if (scoreText == null) return startHeight;

        try (
            PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);
        ) {
            float pWidth = page.getMediaBox().getWidth();

            contentStream.moveTo(82, startHeight);
            contentStream.lineTo(pWidth - 82, startHeight);
            contentStream.stroke();

            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 14);
            contentStream.newLineAtOffset(82, startHeight - 20);
            contentStream.showText("Module " + moduleName + ": " + scoreText);
            contentStream.endText();

            contentStream.moveTo(82, startHeight - 24);
            contentStream.lineTo(pWidth - 82, startHeight - 24);
            contentStream.stroke();
        }

        return startHeight - 24;
    }

    public static float writeTestCase(PDDocument doc, PDPage page, float startHeight, String testName, double testScore, String testMsg) throws IOException {
        if (testName == null) return startHeight;
        if (startHeight - 66 < 70) return 0;

        String scoreText = Double.isNaN(testScore) ? "---" : String.format("%.2f", testScore);
        if (scoreText == null) return startHeight;

        String msgText = (testMsg == null) ? "---" : testMsg;

        try (
            PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true)
        ) {
            float pWidth = page.getMediaBox().getWidth();

            contentStream.moveTo(94, startHeight);
            contentStream.lineTo(pWidth - 94, startHeight);
            contentStream.stroke();

            contentStream.beginText();
            contentStream.newLineAtOffset(94, startHeight - 18);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.showText("Test: ");
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.showText(testName);
            contentStream.newLineAtOffset(0, -22);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.showText("Score: ");
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.showText(scoreText);
            contentStream.newLineAtOffset(0, -22);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.showText("Information: ");
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.showText(msgText);
            contentStream.endText();

            contentStream.moveTo(94, startHeight - 66);
            contentStream.lineTo(pWidth - 94, startHeight - 66);
            contentStream.stroke();
        }

        return startHeight - 66;
    }
}
