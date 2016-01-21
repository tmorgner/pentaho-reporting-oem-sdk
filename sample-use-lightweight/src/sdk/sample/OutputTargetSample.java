package sdk.sample;

import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.ReportHeader;
import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.pentaho.reporting.engine.classic.core.modules.output.pageable.base.PageableReportProcessor;
import org.pentaho.reporting.engine.classic.core.modules.output.pageable.pdf.PdfOutputProcessor;
import org.pentaho.reporting.engine.classic.core.modules.output.table.base.FlowReportProcessor;
import org.pentaho.reporting.engine.classic.core.modules.output.table.html.AllItemsHtmlPrinter;
import org.pentaho.reporting.engine.classic.core.modules.output.table.html.FileSystemURLRewriter;
import org.pentaho.reporting.engine.classic.core.modules.output.table.html.FlowHtmlOutputProcessor;
import org.pentaho.reporting.engine.classic.core.modules.output.table.html.HtmlPrinter;
import org.pentaho.reporting.libraries.base.util.IOUtils;
import org.pentaho.reporting.libraries.repository.ContentIOException;
import org.pentaho.reporting.libraries.repository.ContentLocation;
import org.pentaho.reporting.libraries.repository.DefaultNameGenerator;
import org.pentaho.reporting.libraries.repository.file.FileRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class OutputTargetSample {
  public static void main(String[] args) {
    ClassicEngineBoot.getInstance().start();

    MasterReport report = new MasterReport();
    ReportHeader reportHeader = report.getReportHeader();
    reportHeader.addElement(HelloWorld.createContainer());

    try (OutputStream out = new FileOutputStream("./report.pdf")) {
      createPDF(report, out);
    } catch (IOException | ReportProcessingException e) {
      throw new IllegalStateException("That was unexpected!", e);
    }

    try {
      File target = new File("./html-export");
      target.mkdirs();
      createDirectoryHTML(report, target, "data");
    } catch (IOException | ReportProcessingException e) {
      throw new IllegalStateException("That was unexpected!", e);
    }


  }

  /**
   * Saves a report to HTML. The HTML file is stored in a directory; all other content goes into the same directory as
   * the specified html file. The parent directories for both the TargetFilename and the DataDirectoryName will be
   * created if necessary.
   * <p/>
   * When exporting a report with manual pagebreaks, the directory of the target-filename will contain more than one
   * result-HTML files after the export is complete.
   *
   * @param report         the report.
   * @param targetFile     file object of the target directory.
   * @throws ReportProcessingException if the report processing failed.
   * @throws IOException               if there was an IOerror while processing the report.
   */
  public static void createDirectoryHTML(final MasterReport report,
                                         final File targetFile,
                                         final String dataDirectoryName) throws IOException, ReportProcessingException {
    if (report == null) {
      throw new NullPointerException();
    }
    if (dataDirectoryName == null) {
      throw new NullPointerException();
    }
    try {

      final File targetDirectory = targetFile.getCanonicalFile();
      if (targetDirectory.exists() == false) {
        if (targetDirectory.mkdirs() == false) {
          throw new IOException("Unable to create the target-directory.");
        }
      }

      final File tempDataDir = new File(targetFile, dataDirectoryName).getCanonicalFile();
      File dataDirectory;
      if (tempDataDir.isAbsolute()) {
        dataDirectory = tempDataDir;
      }
      else {
        dataDirectory = new File(targetDirectory, dataDirectoryName).getCanonicalFile();
      }
      if (dataDirectory.exists() && dataDirectory.isDirectory() == false) {
        dataDirectory = dataDirectory.getParentFile();
        if (dataDirectory.isDirectory() == false) {
          throw new ReportProcessingException("DataDirectory is invalid: " + dataDirectory);
        }
      }
      else if (dataDirectory.exists() == false) {
        if (dataDirectory.mkdirs() == false) {
          throw new IOException("Unable to create the data-directory.");
        }
      }

      final FileRepository targetRepository = new FileRepository(targetDirectory);
      final ContentLocation targetRoot = targetRepository.getRoot();

      final FileRepository dataRepository = new FileRepository(dataDirectory);
      final ContentLocation dataRoot = dataRepository.getRoot();

      final FlowHtmlOutputProcessor outputProcessor = new FlowHtmlOutputProcessor();

      final HtmlPrinter printer = new AllItemsHtmlPrinter(report.getResourceManager());
      printer.setContentWriter(targetRoot, new DefaultNameGenerator(targetRoot, "index", "html"));
      printer.setDataWriter(dataRoot, new DefaultNameGenerator(dataRoot, "content"));
      printer.setUrlRewriter(new FileSystemURLRewriter());
      outputProcessor.setPrinter(printer);

      final FlowReportProcessor sp = new FlowReportProcessor(report, outputProcessor);
      sp.processReport();
      sp.close();
    } catch (ContentIOException e) {
      throw new IOException("Failed to get repository-root.");
    }
  }


  public static boolean createPDF(final MasterReport report, final OutputStream out) throws ReportProcessingException {
    if (report == null) {
      throw new NullPointerException();
    }
    if (out == null) {
      throw new NullPointerException();
    }

    PageableReportProcessor proc = null;
    try {

      final PdfOutputProcessor outputProcessor =
              new PdfOutputProcessor(report.getConfiguration(), out, report.getResourceManager());
      proc = new PageableReportProcessor(report, outputProcessor);
      proc.processReport();
      return true;
    } catch (ReportProcessingException rpe) {
      throw rpe;
    } catch (Exception e) {
      throw new ReportProcessingException("Writing PDF failed", e);
    } catch (Error e) {
      throw new ReportProcessingException("Writing PDF failed", e);
    } finally {
      if (proc != null) {
        proc.close();
      }
    }
  }

}
