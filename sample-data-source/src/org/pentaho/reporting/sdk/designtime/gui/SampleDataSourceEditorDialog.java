package org.pentaho.reporting.sdk.designtime.gui;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;

import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.designtime.DesignTimeContext;
import org.pentaho.reporting.engine.classic.core.modules.misc.datafactory.sequence.SequenceDataFactory;
import org.pentaho.reporting.libraries.designtime.swing.CommonDialog;
import org.pentaho.reporting.sdk.designtime.gui.model.DefaultQueryDialogModel;
import org.pentaho.reporting.sdk.designtime.gui.model.QueryDialogModel;

public class SampleDataSourceEditorDialog extends CommonDialog
{
  private SampleDataSourceQueryEditorPanel editorPanel;
  private QueryDialogModel<String> dialogModel;

  public SampleDataSourceEditorDialog()
  {
    init();
  }

  public SampleDataSourceEditorDialog(final Dialog owner) throws HeadlessException
  {
    super(owner);
    init();
  }

  public SampleDataSourceEditorDialog(final Frame owner) throws HeadlessException
  {
    super(owner);
    init();
  }

  protected void init()
  {
    setTitle("Title");

    dialogModel = new DefaultQueryDialogModel<>();
    editorPanel = new SampleDataSourceQueryEditorPanel(dialogModel);
    super.init();
  }

  protected Component createContentPane()
  {
    return editorPanel;
  }

  protected String getDialogId()
  {
    return "SampleDataSourceEditorDialog";
  }

  public DataFactory performConfiguration(final DesignTimeContext context,
                                          final SequenceDataFactory input,
                                          final String selectedQueryName)
  {
    performEdit();

    return null;
  }
}
