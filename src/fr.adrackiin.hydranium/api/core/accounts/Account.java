package fr.adrackiin.hydranium.api.core.accounts;

import fr.adrackiin.hydranium.commons.Rank;

import java.util.UUID;

public class Account {

    private String server;
    private int id; //ID du compte
    private UUID uuid; //UUID du joueur
    private String pseudo; //Pseudo du joueur
    private String ip; //Ip du joueur
    private Rank rank; //Grade du joueur
    private int whitelist; //Nombre de whitelist du joueur
    private int hydras; //Argent du joueur
    private String stats;
    private String settings;
    private String history;
    private String recidivism;
    private String otherip;

    public Account() {} //Constructeur par défaut (Redisson)

    public Account(int id, UUID uuid, String pseudo, String ip, Rank rank, int whitelist, int hydras, String stats, String settings, String history,
                   String recidivism, String otherip, String server) { //Constructeur
        this.id = id;
        this.uuid = uuid;
        this.pseudo = pseudo;
        this.ip = ip;
        this.rank = rank;
        this.whitelist = whitelist;
        this.hydras = hydras;
        this.stats = stats;
        this.settings = settings;
        this.history = history;
        this.recidivism = recidivism;
        this.otherip = otherip;
        this.server = server;
    }

    public String getServer(){
        return server;
    }

    public void setServer(String server){
        this.server = server;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public UUID getUuid(){
        return uuid;
    }

    public void setUuid(UUID uuid){
        this.uuid = uuid;
    }

    public String getPseudo(){
        return pseudo;
    }

    public void setPseudo(String pseudo){
        this.pseudo = pseudo;
    }

    public String getIp(){
        return ip;
    }

    public void setIp(String ip){
        this.ip = ip;
    }

    public Rank getRank(){
        return rank;
    }

    public void setRank(Rank rank){
        this.rank = rank;
    }

    public int getWhitelist(){
        return whitelist;
    }

    public void setWhitelist(int whitelist){
        this.whitelist = whitelist;
    }

    public int getHydras(){
        return hydras;
    }

    public void setHydras(int hydras){
        this.hydras = hydras;
    }

    public String getStats(){
        return stats;
    }

    public void setStats(String stats){
        this.stats = stats;
    }

    public String getSettings(){
        return settings;
    }

    public void setSettings(String settings){
        this.settings = settings;
    }

    public String getHistory(){
        return history;
    }

    public void setHistory(String history){
        this.history = history;
    }

    public String getRecidivism(){
        return recidivism;
    }

    public void setRecidivism(String recidivism){
        this.recidivism = recidivism;
    }

    public String getOtherip(){
        return otherip;
    }

    public void setOtherip(String otherip){
        this.otherip = otherip;
    }
}