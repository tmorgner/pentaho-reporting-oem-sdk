package sdk.sample;

import java.net.URL;

import javax.swing.SwingUtilities;

import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.modules.gui.base.PreviewDialog;
import org.pentaho.reporting.libraries.designtime.swing.LibSwingUtil;
import org.pentaho.reporting.libraries.resourceloader.ResourceException;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;

public class UseReportingGui
{
  public static void main(String[] args)
  {
    try
    {
      // Initialize the reporting engine. The reporting engine wont work without it.
      // You have to do that only once before you first use any functionality or class
      // of the reporting engine. If you don't, errors will happen.
      ClassicEngineBoot.getInstance().start();

      // Locate the report file
      final URL url = UseReportingGui.class.getResource("sdk-sample-report.prpt");
      // and use a resource-manager to load it. Pentaho Reporting loads all
      // its resources via the resource-manager. This manager will cache resources
      // for you and will invalidate the cache when the resources changed on disk.
      final ResourceManager mgr = new ResourceManager();

      // The master-report object is the actual report definition.
      final MasterReport report = (MasterReport) mgr.createDirectly(url, MasterReport.class).getResource();

      // Bring up the preview-dialog. We have to do this on the AWT-event-dispatcher thread
      // or all sorts of strange errors can happen.
      SwingUtilities.invokeLater(new Runnable()
      {
        public void run()
        {
          final PreviewDialog d = new PreviewDialog(report);
          d.pack();
          d.setModal(true);

          LibSwingUtil.centerFrameOnScreen(d);

          d.setVisible(true);
          System.exit(0);
        }
      });
    }
    catch (ResourceException e)
    {
      System.err.println("Failed to load the report");
      e.printStackTrace(System.err);
    }

  }
}
