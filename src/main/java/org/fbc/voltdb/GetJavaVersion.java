package org.fbc.voltdb;

import org.voltdb.*;

public class GetJavaVersion extends VoltProcedure {

    public VoltTable[] run() throws VoltAbortException {
        // Get the Java runtime version
        String javaVersion = System.getProperty("java.version");

        // Create a VoltTable with a single column to store the result
        VoltTable result = new VoltTable(
                new VoltTable.ColumnInfo("JavaVersion", VoltType.STRING));

        // Add the Java version to the VoltTable
        result.addRow(javaVersion);

        // Return the result as an array of VoltTables
        return new VoltTable[] { result };
    }
}
