package fr.adrackiin.hydranium.api.management.redis;

public class RedisCredentials {

    private final String ip; //Adresse ip Redis
    private final String password; //Mot de passe Redis
    private final int port; //Port de Redis
    private final String clientName; //Nom(s) du(des) client(s) connecté(s)

    RedisCredentials(String ip, String password, int port, String clientName) { //Constructeur
        this.ip = ip;
        this.password = password;
        this.port = port;
        this.clientName = clientName;
    }

    public String getIp() { //Getter
        return ip;
    }

    public String getPassword() { //Getter
        return password;
    }

    public int getPort() { //Getter
        return port;
    }

    public String getClientName() { //Getter
        return clientName;
    }

    String getRedisURL() { //Adresse Redis
        return ip + ":" + port;
    }

}
