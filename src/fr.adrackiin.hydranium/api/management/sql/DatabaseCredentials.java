package fr.adrackiin.hydranium.api.management.sql;

public class DatabaseCredentials {

    private String host;
    private String user;
    private String password;
    private String database;
    private int port;

    DatabaseCredentials(String host, String user, String password, String database, int port) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.database = database;
        this.port = port;
    }

    public String toURL() {
        return "jdbc:mysql://" +
                host +
                ":" +
                port +
                "/" +
                database +
                "?useSSL=false";
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }




}
