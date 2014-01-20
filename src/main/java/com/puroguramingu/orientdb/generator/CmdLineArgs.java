package com.puroguramingu.orientdb.generator;

import com.beust.jcommander.Parameter;

public class CmdLineArgs {
  @Parameter(names = "-delete", description = "Delete the DB if it exist", required = false)
  private boolean delete = false;

  @Parameter(names = "-dbName", description = "DB dbName", required = false)
  private String dbName = "db";

  @Parameter(names = "-init", description = "File containing initial data to be inserted in the DB", required = false)
  private String initDataFile = null;

  @Parameter(names = "-package", description = "Package to be scanned for entity classes. By default all packages.", required = false)
  private String packageName = "";

  public boolean isDelete() {
    return delete;
  }

  public String getDbName() {
    return dbName;
  }

  public String getInitDataFile() {
    return initDataFile;
  }

  public String getPackageName() { return packageName; }
}