Elements
========

In Pentaho Reporting, Elements take care of transforming raw data
from the data-sources into presentation data. The reporting engine
supports two main classes of Elements.

Content elements produce new printable items, but cannot have child
elements of their own. Examples of content elements are Labels,
Number-fields, or Charts.

Structural elements, on the other hand, do not produce content. They
control the layout and flow of their child elements. Think of them
as containers, where you can more elements to them. The most
simple form on an content element is the "Band". A band is used
to group elements and to define how child elements will be laid out.
Control elements, which are part of the structural element group,
like Groups or SubReports also provide additional data-processing
rules for the reporting engine.

Adding new structural elements requires changes to the reporting
engine's data-processing and layout core. This work is beyond the
scope of this SDK. We will concentrate on the more useful Content
elements instead.


Defining Elements
-----------------

Content elements hold all styles and attributes in an instance of
org.pentaho.reporting.engine.classic.core.Element. The behaviour
of the element, how it transforms content, and the metadata for
the element are held in an "ElementType" implementation.

The element type provides the code that takes the Element's state
(the attributes and styles) and produces either a string, if the
resulting content is text, or an Drawable object (if the content
is graphical).

The element metadata declares what styles and attributes an element
uses. This metadata information is used when loading and saving
elements, and additionally provides the report designer with the
design-time information needed to successfully edit the element.


Content processing flow
-----------------------

The Pentaho Reporting Engine computes content on the fly. The
ElementType implementation is only called if an element is actually
going to be printed.

When the ElementType is called, the reporting engine evaluates all
attribute- and style-expressions and resolved the style information
using the style-sheet rules that are defined on the MasterReport
object.

When the reporting engine calls your ElementType, treat the
element as read-only. It is generally a bad idea to change properties
of the element itself, as it may break caching assumptions the
reporting engine made earlier. The only element properties that are
safe to touch are private attributes you declared for yourself.

At runtime, the method ElementType#getValue is called. Compute a
value that is printable. If you need to cache computationally
expensive objects, store them as attribute on the element. (Don't
forget to declare the attribute in the metadata and to mark the
attribute as 'computed' and 'transient', to prevent it from getting
saved to disk.)

At design-time the reporting engine calls ElementType#getDesignValue
instead. At design-time the engine has no data available for your
computation, so use suitable hardcoded values. Make your designtime
calculation as inexpensive as possible, as this method may be called
frequently during editing.


Element metadata
----------------




Writing and parsing elements as part of the PRPT bundle
-------------------------------------------------------



