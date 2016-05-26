# To-do List

## Major Features
* Ability to re-invoke builds
* Ability to stop builds
* UI / Front-end
* Track runs
* Track queue

## Minor Features
* Have matchers/filters for jobs
* Have a subclass for regular-expression matchers
* Ability to display summary instead of individual builds
* Allow parameters that are not just strings

## Changes
* Rename matchers to filters
* Separate logging from display output (e.g. command line options should set only one or the other and not both)

## Bugs
* If build parameter differs from job parameter definitions the it won't show up. It should just rely on build parameters