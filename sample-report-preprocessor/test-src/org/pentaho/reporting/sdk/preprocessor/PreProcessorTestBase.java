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

package org.pentaho.reporting.sdk.preprocessor;

import org.junit.Before;
import org.junit.Test;
import org.pentaho.reporting.engine.classic.core.AttributeNames;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.ReportPreProcessor;
import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.pentaho.reporting.engine.classic.core.cache.CachingDataFactory;
import org.pentaho.reporting.engine.classic.core.designtime.DesignTimeUtil;
import org.pentaho.reporting.engine.classic.core.function.ProcessingDataFactoryContext;
import org.pentaho.reporting.engine.classic.core.layout.output.DefaultProcessingContext;
import org.pentaho.reporting.engine.classic.core.states.NoOpPerformanceMonitorContext;
import org.pentaho.reporting.engine.classic.core.states.PerformanceMonitorContext;
import org.pentaho.reporting.engine.classic.core.states.StateUtilities;
import org.pentaho.reporting.engine.classic.core.states.datarow.DefaultFlowController;
import org.pentaho.reporting.engine.classic.core.wizard.DataSchemaDefinition;

public abstract class PreProcessorTestBase
{
  @Before
  public void setUp() throws Exception
  {
    ClassicEngineBoot.getInstance().start();
  }

  @Test
  public void testMetaData() throws Exception
  {
    ReportPreProcessor prc = create();
    PreProcessorTestHelper.validateElementMetaData(prc.getClass());
  }

  protected abstract ReportPreProcessor create();


  /**
   * Helper method to invoke a pre-processor outside of the report processing. This is strictly for
   * unit-testing only.
   *
   * @param report
   * @param processor
   * @return
   * @throws org.pentaho.reporting.engine.classic.core.ReportProcessingException
   */
  protected static MasterReport materialize(final MasterReport report,
                                          final ReportPreProcessor processor) throws ReportProcessingException
  {
    final PerformanceMonitorContext pmc = new NoOpPerformanceMonitorContext();
    final DefaultProcessingContext processingContext = new DefaultProcessingContext(report);
    final DataSchemaDefinition definition = report.getDataSchemaDefinition();
    final DefaultFlowController flowController = new DefaultFlowController(processingContext,
        definition, StateUtilities.computeParameterValueSet(report), pmc);
    final CachingDataFactory dataFactory = new CachingDataFactory(report.getDataFactory(), false);
    dataFactory.initialize(new ProcessingDataFactoryContext(processingContext, dataFactory));

    try
    {
      final DefaultFlowController postQueryFlowController = flowController.performQuery
          (dataFactory, report.getQuery(), report.getQueryLimit(),
              report.getQueryTimeout(), flowController.getMasterRow().getResourceBundleFactory());

      final Object originalEnable =
          report.getAttribute(AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.ENABLE);
      report.setAttribute(AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.ENABLE, Boolean.TRUE);
      final MasterReport masterReport = processor.performPreProcessing(report, postQueryFlowController);
      masterReport.setAttribute(AttributeNames.Wizard.NAMESPACE, AttributeNames.Wizard.ENABLE, originalEnable);

      masterReport.setName(null);
      DesignTimeUtil.resetDocumentMetaData(masterReport);
      return masterReport;
    }
    finally
    {
      dataFactory.close();
    }
  }
}
