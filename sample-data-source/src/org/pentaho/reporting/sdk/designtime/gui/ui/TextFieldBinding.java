package org.pentaho.reporting.sdk.designtime.gui.ui;

import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;

import org.pentaho.reporting.libraries.designtime.swing.event.DocumentChangeHandler;

public abstract class TextFieldBinding extends DocumentChangeHandler implements Runnable
{
  private boolean executed;

  protected void handleChange(final DocumentEvent documentEvent)
  {
    executed = false;
    SwingUtilities.invokeLater(this);
  }

  public final void run()
  {
    if (executed)
    {
      return;
    }

    performUpdate();
    executed = true;
  }

  protected abstract void performUpdate();
}
