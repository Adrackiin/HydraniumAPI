package fr.adrackiin.hydranium.api.management.sql;

public enum DatabaseManager {

    HYDRANIUM(new DatabaseCredentials("localhost","root","ZRqSdlTptOAl2paXWp9U","hydranium",3306));

    private final DatabaseAccess dataBaseAccess;

    DatabaseManager(DatabaseCredentials credentials){
        this.dataBaseAccess = new DatabaseAccess(credentials);
    }

    public DatabaseAccess getDataBaseAccess() {
        return dataBaseAccess;
    }

    public static void initAllDatabaseConnection() {
        for(DatabaseManager databaseManager : values()) {
            databaseManager.dataBaseAccess.initPool();
        }
    }

    public static void closeAllDatabaseConnection() {
        for(DatabaseManager databaseManager : values()) {
            databaseManager.dataBaseAccess.closePool();
        }
    }

}
