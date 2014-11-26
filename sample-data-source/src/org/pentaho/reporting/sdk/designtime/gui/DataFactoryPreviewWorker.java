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

package org.pentaho.reporting.sdk.designtime.gui;

import javax.swing.table.TableModel;

import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.ParameterDataRow;
import org.pentaho.reporting.engine.classic.core.ReportDataFactoryException;
import org.pentaho.reporting.engine.classic.core.ReportProcessingException;
import org.pentaho.reporting.engine.classic.core.designtime.DesignTimeContext;
import org.pentaho.reporting.engine.classic.core.designtime.datafactory.DataFactoryEditorSupport;
import org.pentaho.reporting.libraries.base.util.ArgumentNullException;
import org.pentaho.reporting.libraries.designtime.swing.background.CancelEvent;
import org.pentaho.reporting.libraries.designtime.swing.background.PreviewWorker;

public class DataFactoryPreviewWorker implements PreviewWorker
{
  private DataFactory dataFactory;
  private String queryName;
  private TableModel resultTableModel;
  private ReportDataFactoryException exception;

  public DataFactoryPreviewWorker(final DataFactory dataFactory,
                                  final String queryName,
                                  final DesignTimeContext context) throws ReportProcessingException
  {
    ArgumentNullException.validate("queryName", queryName);
    ArgumentNullException.validate("dataFactory", dataFactory);

    this.dataFactory = dataFactory;
    this.queryName = queryName;
    DataFactoryEditorSupport.configureDataFactoryForPreview(dataFactory, context);
  }

  public ReportDataFactoryException getException()
  {
    return exception;
  }

  public TableModel getResultTableModel()
  {
    return resultTableModel;
  }

  public void close()
  {
    dataFactory.close();
  }

  /**
   * Requests that the thread stop processing as soon as possible.
   */
  public void cancelProcessing(final CancelEvent event)
  {
    dataFactory.cancelRunningQuery();
  }

  /**
   * When an object implementing interface <code>Runnable</code> is used
   * to create a thread, starting the thread causes the object's
   * <code>run</code> method to be called in that separately executing
   * thread.
   * <p/>
   * The general contract of the method <code>run</code> is that it may
   * take any action whatsoever.
   *
   * @see Thread#run()
   */
  public void run()
  {
    try
    {
      resultTableModel = dataFactory.queryData(queryName, new ParameterDataRow());
    }
    catch (ReportDataFactoryException e)
    {
      exception = e;
    }
    finally
    {
      dataFactory.close();
    }
  }
}
