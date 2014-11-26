/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Thomas Morgner
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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
