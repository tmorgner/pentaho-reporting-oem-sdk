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
