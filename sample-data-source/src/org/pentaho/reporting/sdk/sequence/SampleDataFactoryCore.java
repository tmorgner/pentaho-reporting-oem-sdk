package org.pentaho.reporting.sdk.sequence;

import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.DataRow;
import org.pentaho.reporting.engine.classic.core.metadata.DataFactoryCore;
import org.pentaho.reporting.engine.classic.core.metadata.DataFactoryMetaData;
import org.pentaho.reporting.engine.classic.core.metadata.ResourceReference;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;

public class SampleDataFactoryCore implements DataFactoryCore
{
  public SampleDataFactoryCore()
  {
  }

  public String getDisplayConnectionName(final DataFactoryMetaData dataFactoryMetaData, final DataFactory dataFactory)
  {
    SampleDataFactory sampleDataFactory = (SampleDataFactory) dataFactory;
    return sampleDataFactory.getDisplayConnectionName();
  }

  public String[] getReferencedFields(final DataFactoryMetaData dataFactoryMetaData,
                                      final DataFactory dataFactory,
                                      final String query,
                                      final DataRow dataRow)
  {
    SampleDataFactory sampleDataFactory = (SampleDataFactory) dataFactory;
    return sampleDataFactory.getReferencedFields(query, dataRow);
  }

  public ResourceReference[] getReferencedResources(final DataFactoryMetaData dataFactoryMetaData,
                                                    final DataFactory dataFactory,
                                                    final ResourceManager resourceManager,
                                                    final String query,
                                                    final DataRow dataRow)
  {
    return new ResourceReference[0];
  }

  public Object getQueryHash(final DataFactoryMetaData dataFactoryMetaData,
                             final DataFactory dataFactory,
                             final String query,
                             final DataRow dataRow)
  {
    SampleDataFactory sampleDataFactory = (SampleDataFactory) dataFactory;
    return sampleDataFactory.getQueryHash(query, dataRow);
  }
}
