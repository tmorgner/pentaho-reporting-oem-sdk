package org.pentaho.reporting.sdk.designtime.gui.model;

import java.util.EventObject;

public class QueryDialogModelEvent<T> extends EventObject
{
  private final QueryDialogModel<T> eventSource;
  private final int newIndex;
  private final Query<T> newQuery;
  private final int oldIndex;
  private final Query<T> oldQuery;

  public QueryDialogModelEvent(final QueryDialogModel<T> source)
  {
    this(source, -1, null);
  }

  public QueryDialogModelEvent(final QueryDialogModel<T> source,
                               final int index, final Query<T> query)
  {
    this(source, index, query, index, query);
  }

  public QueryDialogModelEvent(final QueryDialogModel<T> source,
                               final int newIndex, final Query<T> newQuery,
                               final int oldIndex, final Query<T> oldQuery)
  {
    super(source);
    this.eventSource = source;
    this.newIndex = newIndex;
    this.newQuery = newQuery;
    this.oldIndex = oldIndex;
    this.oldQuery = oldQuery;
  }

  public QueryDialogModel<T> getEventSource()
  {
    return eventSource;
  }

  public int getNewIndex()
  {
    return newIndex;
  }

  public Query<T> getNewQuery()
  {
    return newQuery;
  }

  public int getOldIndex()
  {
    return oldIndex;
  }

  public Query<T> getOldQuery()
  {
    return oldQuery;
  }
}
