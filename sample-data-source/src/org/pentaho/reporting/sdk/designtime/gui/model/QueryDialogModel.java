package org.pentaho.reporting.sdk.designtime.gui.model;

public interface QueryDialogModel<T> extends Iterable<Query<T>>
{
  QueryDialogComboBoxModel<T> getQueries();
  boolean isQuerySelected();

  void setSelectedQuery(Query<T> query);
  Query<T> getSelectedQuery();

  void addQuery(Query<T> query);
  void removeQuery(Query<T> query);
  void updateQuery(int index, Query<T> query);

  int getQueryCount();
  Query<T> getQuery(int index);

  void addQueryDialogModelListener(QueryDialogModelListener<T> listener);
  void removeQueryDialogModelListener(QueryDialogModelListener<T> listener);

  void updateSelectedQuery(Query<T> newQuery);

  void setGlobalScripting(final String lang, final String script);
  String getGlobalScriptLanguage();
  String getGlobalScript();

}
