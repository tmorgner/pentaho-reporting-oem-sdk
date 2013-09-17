package org.pentaho.reporting.sdk.designtime.gui.ui;

import java.awt.Component;

import org.pentaho.reporting.sdk.designtime.gui.model.QueryDialogModel;

public class GlobalTemplateAction<T> extends ScriptTemplateAction
{
  private final Component parentComponent;
  private final QueryDialogModel<T> dialogModel;

  public GlobalTemplateAction(final Component parentComponent,
                       final QueryDialogModel<T> dialogModel)
  {
    super(true);
    this.parentComponent = parentComponent;
    this.dialogModel = dialogModel;
  }

  protected Component getParentComponent()
  {
    return parentComponent;
  }

  protected String getText()
  {
    return dialogModel.getGlobalScript();
  }

  protected void setText(final String text)
  {
    dialogModel.setGlobalScripting(dialogModel.getGlobalScriptLanguage(), text);
  }
}
