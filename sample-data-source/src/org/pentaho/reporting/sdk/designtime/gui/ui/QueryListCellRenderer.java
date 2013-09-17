package org.pentaho.reporting.sdk.designtime.gui.ui;

import java.awt.Component;
import javax.swing.JList;

import org.pentaho.reporting.libraries.designtime.swing.FixDefaultListCellRenderer;
import org.pentaho.reporting.sdk.designtime.gui.model.Query;

public class QueryListCellRenderer extends FixDefaultListCellRenderer
{
  public QueryListCellRenderer()
  {
  }

  public Component getListCellRendererComponent(final JList list,
                                                final Object value,
                                                final int index,
                                                final boolean isSelected,
                                                final boolean cellHasFocus)
  {
    if (value instanceof Query)
    {
      Query q = (Query) value;
      return super.getListCellRendererComponent(list, q.getName(), index, isSelected, cellHasFocus);
    }

    return super.getListCellRendererComponent(list, null, index, isSelected, cellHasFocus);
  }
}
