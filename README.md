
# Todo TeamCity plugin

This [TeamCity] plugin provides a new build runner, which will scan the
files in your project and build a report with 'todo's based on that.


## Getting started

### Installing a release

Installing the plugin is done the same way as installing another
TeamCity plugin.  Everything needed by the plugin is contained in the
zip package that is built for every release.  This zip must be
installed on the TeamCity server in the 'plugins' folder under the
'TeamCity Data Directory'.

More information can be found on the [TeamCity Documentation Site]
from [JetBrains].


### Build your own

Building your own version of the plugin is not difficult if you have
experience with Maven.  Using maven, the plugin can be built as
follows:

    mvn clean package


This will compile all submodules and package everything in a zip,
which is placed in the 'target' folder.


## Configuration

To use the plugin, an extra build step must be added in your TeamCity
project.  This build step has the type 'Todo Build Runner' and has
several settings that must be configured.  These settings can be
configured from inside the TeamCity web interface.

### Source filter

* Include patterns: a list of globbing patterns to indicate the files
  that must be scanned for todo items
  
* Exclude patterns: a list of globbing patterns to indicate files that
  cannot be scanned (first the include pattern is evaluated, and
  afterwards the exclude pattern)

### Todo level filter 

* Minor level: regular expression to find todo items for level 'minor'
* Major level: regular expression to find todo items for level 'major'
* Critical level:  regular expression to find todo items for level 'critical'


## Report

The report with the todo items in a project is visible in the TeamCity
web interface as part of the build page.  On this page, an extra tab
is provided with the name 'Todo Build Runner'.  This tab shows the
files that contain todo items, together with the importance level of
the todo and the line number where the todo was found in the source
file.

Clicking on the file name will reveal or hide extra lines before and
after the todo items to provide context.


## License and Copyright

The MIT License

Copyright (c) 2015  Ruben Vandeginste (ruben.vandeginste@gmail.com)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.


[TeamCity]: https://www.jetbrains.com/teamcity
[TeamCity Documentation Site]: https://confluence.jetbrains.com/display/TCD9/Installing+Additional+Plugins
[JetBrains]: https://www.jetbrains.com
