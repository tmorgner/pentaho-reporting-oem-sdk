package sdk.sample;

import java.io.IOException;
import java.net.URL;

import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.pentaho.reporting.engine.classic.core.modules.output.pageable.pdf.PdfReportUtil;
import org.pentaho.reporting.libraries.resourceloader.ResourceException;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;

public class UseReportingExport
{
  public static void main(String[] args)
  {
    try
    {
      ClassicEngineBoot.getInstance().start();

      final URL url = UseReportingExport.class.getResource("sdk-sample-report.prpt");

      final ResourceManager mgr = new ResourceManager();
      final MasterReport report = (MasterReport) mgr.createDirectly(url, MasterReport.class).getResource();

      PdfReportUtil.createPDF(report, "report.pdf");
      System.out.println ("Successfully created 'report.pdf' in the current directory.");
    }
    catch (ResourceException e)
    {
      System.err.println("Failed to load the report");
      e.printStackTrace(System.err);
    }
    catch (ReportProcessingException e)
    {
      System.err.println("Failed to process the report");
      e.printStackTrace(System.err);
    }
    catch (IOException e)
    {
      System.err.println("Failed to write the report output");
      e.printStackTrace(System.err);
    }

  }
}
