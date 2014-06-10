# Modules

Pentaho Reporting uses modules as a plugin-system. The engine initializes
modules in a controlled way when the initialization method
ClassicEngineBoot#start() is called.

Modules can declare dependencies. Dependencies tell the initialization
routine in which order to call all modules. When you declare a dependency,
the reporting engine guarantees that all dependent modules are called before
your module is started. If a dependency is not available or failed to start,
your module will not start either. This guarantees that your module code is
only called if all pre-requisites for running correctly are met by the system.


## Registering Modules

The reporting engine needs to know that your module exists, before it can
work with it. Therefore, your module must register itself in the global
configuration using a global "/classic-engine.properties" file in the module
root.

All modules that are not created by Pentaho should be registered in the
configuration namespace of "org.pentaho.reporting.engine.classic.extensions.modules."

The registration entry has the format:

  org.pentaho.reporting.engine.classic.extensions.modules.<vendor-specific-id>.Module=<classname of the module>

The module definition we use in this SDK-section is defined as class
"org.pentaho.reporting.sdk.module.SampleModule". We choose the vendor-specific id
as "sdk.sample-module", and therefore the configuration key we enter in the
global configuration will be:

  org.pentaho.reporting.engine.classic.extensions.modules.sdk.sample-module.Module=org.pentaho.reporting.sdk.module.SampleModule


Now that the reporting engine calls your module during the boot-up process,
you can add any module specific initialization code into the module's
'initialize' method. If you need to indicate an error to the boot-process,
throw an ModuleInitializeException.

## Booting

Before Pentaho Reporting can be used for the first time, it must be configured
and all subsystems need to be initialized. This needs to be done once in the
lifetime of the JVM.

The preferred way to do this, is to call "ClassicEngineBoot.start()". Calling
this method will manually start the report system initialization. Any attempt
to load or run a report before the reporting engine has been initialized will
result in errors.

During the boot process, the system loads the report configuration and loads
and initializes the known Report modules. The boot process runs in a
deterministic order:

### Step 1: Loading report configuration

The report configuration can be separated into a local, a global and a
defaults section. Entries from the local section are used by the report system
to configure a specific report instance. The values for these keys are defined
in the report and do not affect other reports.

The local report configuration can be reached via

  MasterReport.getReportConfiguration()

The global configuration is used to configure the report system itself and
provides default values for the local section. This configuration can be
reached via

  ClassicEngineBoot.getInstance().getGlobalConfig()

Specifying global keys in the local configuration has no effect, it is not
possible to reconfigure the subsystems this way, except the system explicitly
reads local configuration keys from the report object.

The report configuration is a multiple level data-source. Each level provides
defaults for the next higher level. The defined levels are:

  - Local configuration (MasterReport.getConfiguration())
  - Global configuration (ClassicEngineBoot.getInstance().getGlobalConfig())
  - System-Properties
  - PropertyFile(s): "/classic-engine.properties"
  - Package configuration
  - PropertyFile: "/org/pentaho/reporting/engine/classic/core/configuration.properties"
  - Hardcoded defaults

The general contract of the report configuration is that values from a local
configuration override values from the global configuration, which overrides
any built-in values.

Do not directly edit the built-in configuration unless you are a developer
who needs to change the built-in values for all future releases. If you are
deploying Pentaho Reporting and want to change the system wide configuration,
place a "/classic-engine.properties" into your classpath and provide the
override values there instead.

The specified property files are searched in the classpath using the JDK-API
call:

  Class.getResource()

This means, that you will have to place these files into the root of the jar
files or that you will have to include the directory, which contains the file,
as first element in the classpath.

The preferred way of supplying configuration values for the report system is
to create a "/classic-engine.properties" file. This file must be loadable
with the same classloader as the one that was used to load the Pentaho
Reporting classes, or a parent of that classloader.

As user libraries should never be placed into the JDK's lib/ext directory. We
assume that the default application classloader was used to load both the
JFreeReport classes and the application or applet.

The user properties should be placed into the root of the classpath (or in
java-speak: into the "default package")

If you place this file into a jar-file, you should add it to the root of that
jar-file.

### Step 2: Initializing the modules

Once the report configuration is completely discovered and loaded, the
modules will be loaded and initialized.

The package manager will load all modules and resolves all dependencies. If
a modules dependencies cannot be satisfied, the loading of that module will
fail. If the dependencies contain circular references, the whole loading of
these modules will fail.

The list of initial module is fed to the package manager from the Boot class.

For every given module, the package manager will load the module class with
Class.newInstance(). The module implementation must have a public default
constructor, or loading will fail.

After that, the package manager tries to resolve all required and optional
modules and loads these modules. If the loading fails for one of the required
modules, the whole loading process for the module (and all module requiring
this module) will fail. Loader errors for optional modules are ignored.

Once all modules all loaded, the modules will be configured. This will load
the report configuration into the package managers configuration set
(level 4 from above). This step does activate the content or perform any not
undoable work - this is done in the next step.

The last step is to actually initialize the module. If the module is part of
a larger subsystem, it may register itself into that system or perform other
initialization steps (like registering the available fonts).

If a module is part of a larger subsystem, it will be guaranteed, that all
subsystem modules are initialized and configured before any dependent modules
get configured.

## Writing modules

### Module specification basics

Modules define all metadata necessary to successfully load and use the classes
of the module. The modules define a set of core attributes, so that the module
management can be automated.

These core attributes are

    Name: The name of the module
    Producer: Who wrote that stuff (ie. how to flame for bugs )
    Description: A short text describing the purpose of the module
    Subsystem: The name of the subsystem to which this module belongs to.
    Major-, Minor- and Patchlevel version: used by the dependency tracking.

Additionally a module may contain a set of required and optional modules. The
package manager will use this information to resolve all dependencies and to
load and initialize these base packages. Modules with a version less than the
required version are considered non-existent and will not be used. Missing or
invalid required modules will cause the loading process to fail, missing
optional modules are ignored.

