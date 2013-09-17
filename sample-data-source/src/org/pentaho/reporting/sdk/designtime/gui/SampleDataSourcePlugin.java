package org.pentaho.reporting.sdk.designtime.gui;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;

import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.designtime.DataFactoryChangeRecorder;
import org.pentaho.reporting.engine.classic.core.designtime.DataSourcePlugin;
import org.pentaho.reporting.engine.classic.core.designtime.DefaultDesignTimeContext;
import org.pentaho.reporting.engine.classic.core.designtime.DesignTimeContext;
import org.pentaho.reporting.engine.classic.core.metadata.DataFactoryMetaData;
import org.pentaho.reporting.engine.classic.core.metadata.DataFactoryRegistry;
import org.pentaho.reporting.engine.classic.core.modules.misc.datafactory.sequence.SequenceDataFactory;
import org.pentaho.reporting.sdk.datasource.SampleDataFactory;

public class SampleDataSourcePlugin implements DataSourcePlugin
{
  public SampleDataSourcePlugin()
  {
  }

  public boolean canHandle(final DataFactory dataFactory)
  {
    return dataFactory instanceof SampleDataFactory;
  }

  public DataFactory performEdit(final DesignTimeContext context,
                                 final DataFactory input,
                                 final String selectedQueryName,
                                 final DataFactoryChangeRecorder dataFactoryChangeRecorder)
  {
    final SampleDataSourceEditorDialog editor;
    final Window parentWindow = context.getParentWindow();

    if (parentWindow instanceof Dialog)
    {
      editor = new SampleDataSourceEditorDialog((Dialog) parentWindow);
    }
    else if (parentWindow instanceof Frame)
    {
      editor = new SampleDataSourceEditorDialog((Frame) parentWindow);
    }
    else
    {
      editor = new SampleDataSourceEditorDialog();
    }
    return editor.performConfiguration(context, (SequenceDataFactory) input, selectedQueryName);
  }

  public DataFactoryMetaData getMetaData()
  {
    return DataFactoryRegistry.getInstance().getMetaData(SampleDataFactory.class.getName());
  }

  public static void main(String[] args)
  {
    ClassicEngineBoot.getInstance().start();

    SampleDataSourcePlugin p = new SampleDataSourcePlugin();
    p.performEdit(new DefaultDesignTimeContext(new MasterReport()), null, null, null);
  }
}
