package org.pentaho.reporting.sdk.designtime.gui.model;

public interface QueryDialogModelListener<T>
{
  void queryAdded(QueryDialogModelEvent<T> event);
  void queryRemoved(QueryDialogModelEvent<T> event);
  void queryUpdated(QueryDialogModelEvent<T> event);

  void queryDataChanged(QueryDialogModelEvent<T> event);

  void selectionChanged(QueryDialogModelEvent<T> event);
  void globalScriptChanged(QueryDialogModelEvent<T> event);
}
