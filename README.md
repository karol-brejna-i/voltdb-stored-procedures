# VoltDB Java stored procedures experiments

This project contains a simple example of VoltDB's stored procedure in Java.
The goal is not to present all the features provided by VoltDB's stored procedure,
but rather the worflow for development of stored procedure.

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

## Building the procedures

This project uses Maven for managing the dependencies and build process.

The configuration file ([pom.xml](./pom.xml)) contains:
- maven artifact information (`groupId`, `artifactId`, `name`), which correspond to the Java package name for the project
- `version` of the project, which is reflected in the resulting JAR file name
- target Java version (`maven.compiler.source`, `maven.compiler.target`)
- project dependencies, meaning all libraries required by the project

For your project, update artifact and version information according to your needs.

You should **update Java version** of your stored procedures, to make sure it is supported by your VoltDB installation. ATTOW, [versions supported by VoltDB Server](https://docs.voltdb.com/UsingVoltDB/ChapGetStarted.php) are: Java 11, 17, or 21. (In therory, Java is backwards compatible, so staying with version 11 should be safe enough.)

To build and package the jar, use the following command:
```bash
mvn package
```

TBD

## Loading and declaring the procedures
TBD

## Invoking the procedures
TBD