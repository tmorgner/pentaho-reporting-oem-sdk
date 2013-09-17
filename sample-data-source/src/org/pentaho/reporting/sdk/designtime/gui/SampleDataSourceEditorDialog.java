package org.pentaho.reporting.sdk.designtime.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.ReportDataFactoryException;
import org.pentaho.reporting.engine.classic.core.designtime.DesignTimeContext;
import org.pentaho.reporting.engine.classic.core.modules.gui.commonswing.ExceptionDialog;
import org.pentaho.reporting.libraries.base.util.StringUtils;
import org.pentaho.reporting.libraries.designtime.swing.CommonDialog;
import org.pentaho.reporting.libraries.designtime.swing.background.DataPreviewDialog;
import org.pentaho.reporting.sdk.datasource.SampleDataFactory;
import org.pentaho.reporting.sdk.designtime.gui.model.DefaultQueryDialogModel;
import org.pentaho.reporting.sdk.designtime.gui.model.Query;
import org.pentaho.reporting.sdk.designtime.gui.model.QueryDialogModel;
import org.pentaho.reporting.sdk.designtime.gui.ui.Messages;

public class SampleDataSourceEditorDialog extends CommonDialog
{
  public class LimitRowsCheckBoxAction extends AbstractAction
  {
    private JSpinner maxPreviewRowsSpinner;

    public LimitRowsCheckBoxAction(final JSpinner maxPreviewRowsSpinner)
    {
      this.maxPreviewRowsSpinner = maxPreviewRowsSpinner;
      putValue(Action.NAME, Messages.getString("SampleDataSourceEditorDialog.LimitRowsCheckBox"));
      putValue(Action.MNEMONIC_KEY, Messages.getMnemonic("SampleDataSourceEditorDialog.LimitRowsCheckBox.Mnemonic"));
      maxPreviewRowsSpinner.setEnabled(false);
    }

    public void actionPerformed(final ActionEvent e)
    {
      final Object source = e.getSource();
      if (source instanceof AbstractButton)
      {
        final AbstractButton b = (AbstractButton) source;
        maxPreviewRowsSpinner.setEnabled(b.isSelected());
      }
    }
  }

  private class PreviewAction extends AbstractAction
  {
    private PreviewAction()
    {
      putValue(Action.NAME, Messages.getString("SampleDataSourceEditorDialog.Preview.Name"));
    }

    public void actionPerformed(final ActionEvent aEvt)
    {
      try
      {
        final Query<String> query = dialogModel.getSelectedQuery();
        if (query == null)
        {
          return;
        }

        final DataPreviewDialog previewDialog = new DataPreviewDialog(SampleDataSourceEditorDialog.this);
        final DataFactoryPreviewWorker worker = new DataFactoryPreviewWorker
            (createDataFactory(), query.getQuery(), designTimeContext);
        previewDialog.showData(worker);

        final ReportDataFactoryException factoryException = worker.getException();
        if (factoryException != null)
        {
          ExceptionDialog.showExceptionDialog(SampleDataSourceEditorDialog.this,
              Messages.getString("SampleDataSourceEditorDialog.PreviewError.Title"),
              Messages.getString("SampleDataSourceEditorDialog.PreviewError.Message"), factoryException);
        }

      }
      catch (Exception e)
      {
        ExceptionDialog.showExceptionDialog(SampleDataSourceEditorDialog.this,
            Messages.getString("SampleDataSourceEditorDialog.PreviewError.Title"),
            Messages.getString("SampleDataSourceEditorDialog.PreviewError.Message"), e);
      }
    }
  }

  private SampleDataSourceQueryEditorPanel editorPanel;
  private QueryDialogModel<String> dialogModel;
  private DesignTimeContext designTimeContext;
  private JSpinner maxPreviewRowsSpinner;
  private JComboBox<String> connectUrlBox;
  private DefaultComboBoxModel<String> connectUrlModel;

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

    maxPreviewRowsSpinner = new JSpinner(new SpinnerNumberModel(10000, 1, Integer.MAX_VALUE, 1));

    connectUrlModel = new DefaultComboBoxModel<>();

    connectUrlBox = new JComboBox<>(connectUrlModel);
    connectUrlBox.setEditable(true);

    dialogModel = new DefaultQueryDialogModel<>();
    editorPanel = new SampleDataSourceQueryEditorPanel(dialogModel);
    super.init();
  }

  protected Component createContentPane()
  {
    final JPanel urlPanel = new JPanel();
    urlPanel.setLayout(new BorderLayout());
    urlPanel.add(new JLabel(Messages.getString("SampleDataSourceEditorDialog.ConnectURL")));
    urlPanel.add(connectUrlBox);

    final JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    panel.add(editorPanel, BorderLayout.CENTER);
    panel.add(editorPanel, BorderLayout.CENTER);
    panel.add(createPreviewButtonsPanel(), BorderLayout.SOUTH);
    return panel;
  }

  private JPanel createPreviewButtonsPanel()
  {
    final JPanel previewButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    previewButtonsPanel.add(new JCheckBox(new LimitRowsCheckBoxAction(maxPreviewRowsSpinner)));
    previewButtonsPanel.add(maxPreviewRowsSpinner);
    previewButtonsPanel.add(new JButton(new PreviewAction()));
    previewButtonsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    return previewButtonsPanel;
  }

  protected String getDialogId()
  {
    return "SampleDataSourceEditorDialog"; // NON-NLS
  }

  public DataFactory performConfiguration(final DesignTimeContext context,
                                          final SampleDataFactory input,
                                          final String selectedQueryName)
  {
    this.designTimeContext = context;
    this.dialogModel.clear();
    this.connectUrlModel.removeAllElements();
    this.connectUrlModel.addElement(SampleDataFactory.YAHOO_QUERY_DEFAULT);
    this.connectUrlModel.setSelectedItem(SampleDataFactory.YAHOO_QUERY_DEFAULT);

    if (input != null)
    {
      if (StringUtils.isEmpty(input.getUrlPattern()) == false &&
          SampleDataFactory.YAHOO_QUERY_DEFAULT.equals(input.getUrlPattern()) == false)
      {
        this.connectUrlModel.insertElementAt(input.getUrlPattern(), 0);
        this.connectUrlModel.setSelectedItem(input.getUrlPattern());
      }

      Query<String> selectedQuery = null;
      for (String queryName : input.getQueryNames())
      {
        Query<String> query = new Query<>(queryName, input.getQuery(queryName),
            input.getScriptingLanguage(queryName), input.getScript(queryName));
        if (queryName.equals(selectedQueryName))
        {
          selectedQuery = query;
        }
        this.dialogModel.addQuery(query);
      }
      this.dialogModel.setSelectedQuery(selectedQuery);
    }

    if (performEdit() == false)
    {
      return null;
    }

    return createDataFactory();
  }

  private SampleDataFactory createDataFactory()
  {
    SampleDataFactory dataFactory = new SampleDataFactory();
    String selectedItem = (String) connectUrlModel.getSelectedItem();
    if (StringUtils.isEmpty(selectedItem) == false &&
        SampleDataFactory.YAHOO_QUERY_DEFAULT.equals(selectedItem) == false)
    {
      dataFactory.setUrlPattern(selectedItem);
    }
    dataFactory.setGlobalScriptLanguage(dialogModel.getGlobalScriptLanguage());
    dataFactory.setGlobalScript(dialogModel.getGlobalScript());
    for (Query<String> q : dialogModel)
    {
      dataFactory.setQuery(q.getName(), q.getQuery(), q.getQueryLanguage(), q.getQueryScript());
    }
    return dataFactory;
  }
}
