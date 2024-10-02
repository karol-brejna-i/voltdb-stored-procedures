package org.fbc.voltdb;

import org.voltdb.*;

public class Echo extends VoltProcedure {
    public VoltTable[] run(String message) throws VoltAbortException {
        VoltTable result = new VoltTable(new VoltTable.ColumnInfo("message", VoltType.STRING));
        result.addRow("I got: " + message);
        return new VoltTable[] { result };
    }
}
