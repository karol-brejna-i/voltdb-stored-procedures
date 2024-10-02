In order to use the stored procedure you wrote and packaged into a JAR, you need to:
- load the JAR into VoltDB
- create a stored procedure (and show which class to use for it)


This description assumes the following example procedure:

```java
package org.fbc.voltdb;
import org.voltdb.*;
public class MyProcedure extends VoltProcedure {
    public VoltTable[] run(String message) throws VoltAbortException {
        VoltTable result = new VoltTable(new VoltTable.ColumnInfo("message", VoltType.STRING));
        result.addRow("I got: " + message);
        return new VoltTable[] { result };
    }
}
```

is developed and packaged into `voltdb-procedure-1.0-SNAPSHOT.jar`.

## Load the JAR into VoltDB
If you want to load the stored procedure dynamically after the database is running, you can do so using `sqlcmd`.

1. Open the `sqlcmd` tool:
    ```bash
    sqlcmd
    ```

2. Load the JAR into VoltDB using the `load` command:

```sql
LOAD CLASSES voltdb-procedure-1.0-SNAPSHOT.jar;
```

## Register the Stored Procedure (Optional)
To expose the procedure, you can register it with the following SQL code:


```sql
CREATE PROCEDURE FROM CLASS org.fbc.voltdb.MyProcedure;
```

Now the procedure will be accessible by the name `MyProcedure` in your SQL queries.

> Please note, that you if have more stored procedures in a jar file, you need to register every single one of them (that you are planning to use).

## Execute the Stored Procedure
Once the procedure is loaded into VoltDB, you can execute it via `sqlcmd`, the VoltDB client, or any application that interfaces with VoltDB.

For example, in `sqlcmd`:

```sql
EXEC MyProcedure 'Hello!';
```
