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

package sdk.generate.patch;

import org.pentaho.reporting.engine.classic.core.AbstractReportDefinition;
import org.pentaho.reporting.engine.classic.core.CompoundDataFactory;
import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.modules.misc.datafactory.sql.ConnectionProvider;
import org.pentaho.reporting.engine.classic.core.modules.misc.datafactory.sql.DriverConnectionProvider;
import org.pentaho.reporting.engine.classic.core.modules.misc.datafactory.sql.JndiConnectionProvider;
import org.pentaho.reporting.engine.classic.core.modules.misc.datafactory.sql.SimpleSQLReportDataFactory;
import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.writer.BundleWriter;
import org.pentaho.reporting.engine.classic.core.modules.parser.bundle.writer.BundleWriterException;
import org.pentaho.reporting.engine.classic.core.util.AbstractStructureVisitor;
import org.pentaho.reporting.libraries.repository.ContentIOException;
import org.pentaho.reporting.libraries.resourceloader.ResourceException;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;

import java.io.File;
import java.io.IOException;

public class PatchAReportSample {

  public static void processReport(String sourceText, String targetText)
          throws ResourceException, IOException, BundleWriterException, ContentIOException {

    // load a report ..
    File sourceFile = new File(sourceText);
    MasterReport report = (MasterReport) new ResourceManager().createDirectly(sourceFile, MasterReport.class).getResource();

    // .. then do something with it ..
    MasterReport processedReport = manipulateReport(report);

    // .. then write the report as PRPT file
    BundleWriter.writeReportToZipFile(processedReport, new File(targetText));
  }

  private static MasterReport manipulateReport(MasterReport report) {
    MasterReport derived = (MasterReport) report.derive();
    new DataSourceStructureVisitor().inspect(derived);
    return derived;
  }

  private static class DataSourceStructureVisitor extends AbstractStructureVisitor {
    @Override
    protected void inspect(AbstractReportDefinition reportDefinition) {

      processAllDataSources(reportDefinition);
      processSingleDataSource(reportDefinition, "query");
      super.inspect(reportDefinition);
    }

    private void processSingleDataSource(AbstractReportDefinition reportDefinition, String query) {
      CompoundDataFactory dataFactory = CompoundDataFactory.normalize(reportDefinition.getDataFactory());
      DataFactory dataFactoryForQuery = dataFactory.getDataFactoryForQuery(query);
      if (dataFactoryForQuery != null) {
        int idx = dataFactory.indexOfByReference(dataFactory);
        dataFactory.set(idx, handleDataSource(reportDefinition, dataFactory));
      }
      reportDefinition.setDataFactory(dataFactory);
    }


    private void processAllDataSources(AbstractReportDefinition reportDefinition) {
      CompoundDataFactory dataFactory = CompoundDataFactory.normalize(reportDefinition.getDataFactory());
      final int size = dataFactory.size();
      for (int i = 0; i < size; i++) {
        dataFactory.set(i, handleDataSource(reportDefinition, dataFactory.getReference(i)));
      }
      reportDefinition.setDataFactory(dataFactory);
    }

    private DataFactory handleDataSource(AbstractReportDefinition reportDefinition, DataFactory dataFactory) {
      PatchAReportSample.handleDataSource(reportDefinition, dataFactory);
      return dataFactory;
    }
  }


  private static DataFactory handleDataSource(AbstractReportDefinition reportDefinition, DataFactory dataFactory) {
    // do whatever you want here.
    if (dataFactory instanceof SimpleSQLReportDataFactory) {
      SimpleSQLReportDataFactory sdf = (SimpleSQLReportDataFactory) dataFactory;
      if (isLocalSampleData(sdf)) {
        JndiConnectionProvider connectionProvider = new JndiConnectionProvider();
        connectionProvider.setConnectionPath("SampleData");
        sdf.setConnectionProvider(connectionProvider);
      }
    }
    return dataFactory;
  }

  private static boolean isLocalSampleData(SimpleSQLReportDataFactory sdf) {
    ConnectionProvider cp = sdf.getConnectionProvider();
    if (cp instanceof DriverConnectionProvider) {
      DriverConnectionProvider jcp = (DriverConnectionProvider) cp;
      if ("org.hsqldb.jdbcDriver".equals(jcp.getDriver()) &&
              "jdbc:hsqldb:file:./sql/sampledata".equalsIgnoreCase(jcp.getUrl())) {
        return true;
      }
    }

    return false;
  }

}
