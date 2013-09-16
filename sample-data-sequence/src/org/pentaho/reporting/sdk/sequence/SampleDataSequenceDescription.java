package org.pentaho.reporting.sdk.sequence;

import org.pentaho.reporting.engine.classic.core.modules.misc.datafactory.sequence.AbstractSequenceDescription;

public class SampleDataSequenceDescription extends AbstractSequenceDescription
{
  public SampleDataSequenceDescription()
  {
    super("org.pentaho.reporting.sdk.sequence.SampleDataSequenceBundle", SampleDataSequence.class);
  }


}
