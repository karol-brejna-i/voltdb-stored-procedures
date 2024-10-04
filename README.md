# VoltDB Java stored procedures experiments

Stored Procedures in VoltDB are great way to extend capabilities of this in-memory database and to build fast data applications.

This project contains a simple example of VoltDB's stored procedure in Java.

The goal is not to present all the features provided by VoltDB's stored procedure,
but rather the worflow for development of stored procedure and allow to quickly start experimenting.

## Typical developent workflow
Usually, the process goes like this:

1. **Define** the procedure as a Java class that extends [`VoltProcedure`](https://docs.voltactivedata.com/javadoc/server-api/org/voltdb/VoltProcedure.html) (or [VoltCompoundProcedure](https://docs.voltactivedata.com/javadoc/server-api/org/voltdb/VoltCompoundProcedure.html)).
2. **Build** the procedure by compiling the Java source file, packaging it in a JAR file.
3. **Load** the JAR into your database (using the **sqlcmd** LOAD CLASSES directive).
4. **Declare** the procedure in your schema using the [`CREATE PROCEDURE FROM CLASS`](https://docs.voltactivedata.com/UsingVoltDB/ddlref_createprocjava.php) statement (or `CREATE COMPOUND PROCEDURE FROM CLASS`, `CREATE DIRECTED PROCEDURE`).
5. **Invoke** the procedure -- by associating it with an inbound topic in the configuration, or the procedure can be called from a client application.

## Defining stored procedures

Please, visit the documentation <https://docs.voltactivedata.com/UsingVoltDB/DesignProc.php> to learn the anatomy of VoltDB Stored Procedures, how they work and how to use them.

For Java stored procedures, you need to encapsulate the required logic (data access and manipulation) into a Java class that inherits from [VoltProcedure](https://docs.voltactivedata.com/javadoc/server-api/org/voltdb/VoltProcedure.html) (or [VoltCompoundProcedure](https://docs.voltactivedata.com/javadoc/server-api/org/voltdb/VoltCompoundProcedure.html)).

> In this project, we'll be focusing on "standard" stored procedures (not the compound ones).

The template for a stored procedure is:

```java
package mypackage;

import org.voltdb.*;

public class MyProcedure extends VoltProcedure {

    public <<datatype>> run(<<arguments>>) throws VoltAbortException {
        // logic
    }
}
```

You put required logic into `run` method. This method can accept arguments, that can be scalar or array of object of following types:

| Category               | Java Datatypes                                                                 |
|------------------------|--------------------------------------------------------------------------------|
| Integer types          | byte, short, int, long, Byte, Short, Integer, Long                             |
| Floating point types   | float, double, Float, Double                                                   |
| Fixed decimal types    | BigDecimal                                                                     |
| String and binary types| String, byte[]                                                                 |
| Timestamp types        | org.voltdb.types.TimestampType, java.util.Date, java.sql.Date, java.sql.Timestamp |
| VoltDB type            | VoltTable                                                                      |

`run()` can return long integer, [VoltTable](https://docs.voltactivedata.com/javadoc/server-api/org/voltdb/VoltTable.html), array of VoltTable.

This project introduces the following example:

```java
package org.fbc.voltdb;
import org.voltdb.*;
public class Echo extends VoltProcedure {
    public VoltTable[] run(String message) throws VoltAbortException {
        VoltTable result = new VoltTable(new VoltTable.ColumnInfo("message", VoltType.STRING));
        result.addRow("I got: " + message);
        return new VoltTable[] { result };
    }
}
```
This creates `Echo` procedure that accepts a string as parameter and echoes this strings as a result.

## Building the procedures

This project uses Maven for managing the dependencies and build process.

The configuration file ([pom.xml](./pom.xml)) contains:
- maven artifact information (`groupId`, `artifactId`, `name`), which correspond to the Java package name for the project
- `version` of the project, which is reflected in the resulting JAR file name
- target Java version (`maven.compiler.source`, `maven.compiler.target`)
- project dependencies, meaning all libraries required by the project

For your project, update artifact and version information according to your needs.

You should **set Java version** of your stored procedures, to make sure it is supported by your VoltDB installation. ATTOW, [versions supported by VoltDB Server](https://docs.voltdb.com/UsingVoltDB/ChapGetStarted.php) are: Java 11, 17, or 21. (In therory, Java is backwards compatible, so staying with version 11 should be safe enough.)

In this example, the pom.xml has a single dependency on [VoltDB server library](https://central.sonatype.com/artifact/org.voltdb/voltdb). Currently, the latest version available on Maven Central is 10.1.1 (see [notes about newer voltdb.jar versions](./docs/voltdb-server-lib.md)). If your Java class requires **more dependencies**, update pom.xml accordingly.

Now, that the project is configured, build and package the jar using the following command:

```bash
mvn package
```

This will produce stored-procedures-test-1.0-SNAPSHOT.jar (name generated from `artifactId`-`version`) in `target` directory.


## Loading and declaring the procedures

In order to use the stored procedure you wrote and packaged into a JAR, you need to:
- load the JAR into VoltDB
- declare a stored procedure (and show which class to use for it) in you schema

`LOAD CLASSES` command is used load the JAR into VoltDB using the `load` command:

```sql
LOAD CLASSES stored-procedures-test-1.0-SNAPSHOT.jar;
```

Where `stored-procedures-test-1.0-SNAPSHOT.jar` is the path to the JAR file.

The following SQL code declares a procedure that will be available as `Echo`:

```sql
CREATE PROCEDURE FROM CLASS org.fbc.voltdb.Echo;
```

## Invoking the procedures

You can invoke the procedure as a SQL command (using `sqlcmd` CLI):

```shell
ubuntu@ip-172-16-0-37:~$ sqlcmd
1> exec Echo "Hello!";
message
----------------
I got: "Hello!"
```

For information, how to call the procedure from a Java client app, see [Invoking stored procedures](./docs/invoking-stored-procedures.md).
