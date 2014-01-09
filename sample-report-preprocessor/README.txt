Report-PreProcessor
===================

A report pre-processor is a customization component that can be added to a master-
or sub-report. This component provides a method of automatically modifying an existing
report definition on each report run. Using this method, you have the greater degree
of freedom to modify the structure of reports that run on your system.

Pre-Processors are registered during the reporting engine's boot process. You cannot
use pre-processor implementations that are not registered in the reporting engine's
metadata system.

To learn about modules and how to use them to extend the reporting engine, please
read the documentation and code in the "sample-module" section of this SDK.


Common use cases for report pre-processors
------------------------------------------

Pre-processors are useful for injecting content into reports. Examples of such content
are style-sheet definitions, disclaimer pages or common header and footer sections on
reports.

You could add that content could be manually, but for even a small number of reports,
the cost of ensuring consistency or even changing the content on all reports easily
skyrockets.

With pre-processors your report only contains a reference to the pre-processor, but
not to any of the actual content. Thus, changing the pre-processor will change your
report.

The second major use-case for pre-processors is the creation of dynamic reports that
have a varying number of columns, depending on either user preferences or business
rules. As a pre-processor can modify all aspects of the report, removing or adding
elements is trivial.


The report-wizard is implemented as a report pre-processor. However, you can also
add your own pre-processor implementations to an existing wizard-specification to
further customize reports created in either the Pentaho Report Wizard or in
Pentaho Interactive Reporting on the Pentaho Business Analytics Server.


Limitations of uses
-------------------

Any pre-processor implementation must have a parameterless public constructor, or
the reporting engine will not be able to use this implementation.

The pre-processor is called after parameter have been validated, so that any
data used to modify the report is properly validated. Thus a pre-processor cannot
modify parameter definitions of an master-report.

A pre-processor cannot directly produce new data for the current report or sub-report,
but pre-processors that use the 'performPreDataProcessing' method can modify the
data-factory that will be used to process the report. Thus you can control the data
processing indirectly, if needed.

Any pre-processor can safely modify all report elements, including sub-reports, as
long as these elements are a child element of the current report.

Be careful when adding new elements to the _current_ report from an system- or
auto-pre-processor. The execution order of system pre-processors is not defined and
they can execute in any order.


How to create a pre-processor
-----------------------------

A report-pre-processor is an implementation of the interface

   org.pentaho.reporting.engine.classic.core.ReportPreProcessor

In Pentaho Reporting 5.0 or later, you can use the AbstractReportPreProcessor class
as base class to simplify your implementation.

Use the "performPreDataProcessing" to modify the data-factories of the current
report. Use the "performPreProcessing" method to modify the report structure and
elements.

Each of these methods exists in two variations, one for Master-Reports, and one
for subreports. The methods are called once per report processing run.



Report-Local and System-Level Pre-Processors
--------------------------------------------

Pentaho Reporting has two ways of activating a report pre-processor, a local definition
that applies only to the current report or sub-report, and a set of global definitions,
which the reporting engine applies to all reports that run on that particular system.

Unless defined otherwise, all report-pre-processors are local processors and you have
to add these to the report manually.

To add a pre-processor to a report in Pentaho Report Designer, select the Master-
or SubReport object. You will find the attribute named "pre-processor" in the advanced
grouping in the attributes table. As pre-processors are an advanced feature, make sure
you have "Show Expert Features" enabled in the Report-Designer's preferences.


To promote a report pre-processor to a system level component, set the attribute
"auto-process" to "true" in your meta-report-preprocessor.xml file.

    Note: In version 3.9 of the reporting engine, you would have to set a custom,
          unique configuration key to the name of your implementation. See the
          "configuration.properties" file in this module for details.


