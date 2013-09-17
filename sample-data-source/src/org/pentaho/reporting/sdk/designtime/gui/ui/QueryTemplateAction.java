package org.pentaho.reporting.sdk.designtime.gui.ui;

import java.awt.Component;

import org.pentaho.reporting.sdk.designtime.gui.model.Query;
import org.pentaho.reporting.sdk.designtime.gui.model.QueryDialogModel;

public class QueryTemplateAction<T> extends ScriptTemplateAction
{
  private Component parentComponent;
  private QueryDialogModel<T> dialogModel;

  public QueryTemplateAction(final Component parentComponent,
                      final QueryDialogModel<T> dialogModel)
  {
    super(false);
    this.parentComponent = parentComponent;
    this.dialogModel = dialogModel;
  }

  protected Component getParentComponent()
  {
    return parentComponent;
  }

  protected void setText(final String text)
  {
    Query<T> selectedQuery = dialogModel.getSelectedQuery();
    if (selectedQuery == null)
    {
      throw new IllegalStateException();
    }

    dialogModel.updateSelectedQuery(selectedQuery.updateQueryScript(selectedQuery.getQueryLanguage(), text));
  }

  protected String getText()
  {
    Query<T> selectedQuery = dialogModel.getSelectedQuery();
    if (selectedQuery == null)
    {
      throw new IllegalStateException();
    }
    return selectedQuery.getQueryScript();
  }
}
