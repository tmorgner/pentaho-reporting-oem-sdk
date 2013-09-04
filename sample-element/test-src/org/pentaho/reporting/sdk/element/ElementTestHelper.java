package org.pentaho.reporting.sdk.element;

import org.pentaho.reporting.engine.classic.core.layout.model.RenderBox;
import org.pentaho.reporting.engine.classic.core.layout.model.RenderNode;
import org.pentaho.reporting.engine.classic.core.layout.model.RenderableText;
import org.pentaho.reporting.engine.classic.core.layout.model.SpacerRenderNode;

public class ElementTestHelper
{
  private ElementTestHelper()
  {
  }

  public static String computePrintedText(RenderBox renderBox)
  {
    StringBuilder b = new StringBuilder();
    RenderNode lineChild = renderBox.getFirstChild();

    while (lineChild != null)
    {
      if (lineChild instanceof RenderableText)
      {
        RenderableText text = (RenderableText) lineChild;
        b.append(text.getRawText());
      }
      else if (lineChild instanceof SpacerRenderNode)
      {
        SpacerRenderNode spacer = (SpacerRenderNode) lineChild;
        for (int i = 0; i < spacer.getSpaceCount(); i+= 1)
        b.append(' ');
      }
      else if (lineChild instanceof RenderBox)
      {
        b.append(computePrintedText((RenderBox) lineChild));
      }

      lineChild = lineChild.getNext();
    }
    return b.toString();
  }

}
