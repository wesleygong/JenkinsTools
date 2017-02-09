# Jenkins Tools

[![Build Status](https://travis-ci.org/kevin-yen/JenkinsTools.svg?branch=master)](https://travis-ci.org/kevin-yen/JenkinsTools)

List build information from Jenkins servers. Here is the [todo list](TODO.md).

## Installing
The easiest way to install is to clone the git repository

```
git clone git@github.com:kevin-yen/JenkinsTools.git
```

Create a file called servers.list containing the URLs to the Jenkins servers. If you are running the Jenkins server locally, it should contain http://localhost/.

## Usage
The easiest way is to invoke 'gradle run' and pass command line properties into a gradle project property called 'execArgs'

```
./gradlew run -PexecArgs='list [filter options]'
```

## Filter Options
* --building: true, any, false
* --name: [regular expression matching job name]
* --result: aborted, failure, success
* --parameters: [parameter name]=[parameter value]

## Example
List all the currently running builds URLs

```
./gradlew run -PexecArgs='list'
```

List all the currently running builds with additional information

```
./gradlew run -PexecArgs='list --info'
```

List all builds that ran with 

```
./gradlew run -PexecArgs='list --info --building any'
```

List all builds that containing 'maintainance' in the job name

```
./gradlew run -PexecArgs='list --info --building any --name maintainance'
```