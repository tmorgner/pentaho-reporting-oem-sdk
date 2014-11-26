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
    return editor.performConfiguration(context, (SampleDataFactory) input, selectedQueryName);
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
