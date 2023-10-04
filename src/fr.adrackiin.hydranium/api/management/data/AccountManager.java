package fr.adrackiin.hydranium.api.management.data;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.core.accounts.Account;
import fr.adrackiin.hydranium.api.core.sanctions.BannedPlayer;
import fr.adrackiin.hydranium.api.core.sanctions.MutedPlayer;
import fr.adrackiin.hydranium.api.exceptions.CanTakeTimeException;
import fr.adrackiin.hydranium.api.management.redis.RedisAccess;
import fr.adrackiin.hydranium.api.management.sql.DatabaseManager;
import fr.adrackiin.hydranium.api.utils.CanTakeTime;
import fr.adrackiin.hydranium.api.utils.statics.PubSub;
import fr.adrackiin.hydranium.commons.Rank;
import org.bukkit.Bukkit;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class AccountManager { //Récupérer le compte

    private static final String REDIS_KEY = "account:"; //Clé de base pour Redis

    private final RedissonClient redissonClient = RedisAccess.instance.getRedissonClient();

    public Object getDataRedis(String data, UUID uuid) throws CanTakeTimeException {
        RMap<String, Object> account = redissonClient.getMap(REDIS_KEY + uuid);
        return account.get(data);
    }

    public void setDataRedis(String data, Object value, UUID uuid) {
        Bukkit.getScheduler().runTaskAsynchronously(APICore.getPlugin(), ()-> {
            RMap<String, Object> account = redissonClient.getMap(REDIS_KEY + uuid);
            account.fastPut(data, value);
        });
    }

    public void setDataRedis(Map<String, Object> map, UUID uuid){
        Bukkit.getScheduler().runTaskAsynchronously(APICore.getPlugin(), ()-> {
            RMap<String, Object> account = redissonClient.getMap(REDIS_KEY + uuid);
            account.putAll(map);
        });
    }

    public boolean isAccountOnRedis(UUID uuid) throws CanTakeTimeException {
        return this.getDataRedis("name", uuid) != null;
    }

    public boolean isAccountOnDB(UUID uuid) throws CanTakeTimeException {
        return this.getDataDB("uuid", uuid) != null;
    }

    public void sendAccountRedis(Account account){
        sendAccountRedis(account.getUuid(), account.getPseudo(), account.getRank(), account.getWhitelist(), account.getHydras(), account.getStats(), account.getSettings(), account.getHistory(), account.getRecidivism(), account.getOtherip(), account.getServer());
    }

    public void sendAccountRedis(UUID uuid, String name, Rank rank, int whitelist, int hydras, String stats, String settings, String history, String recidivism, String otherIp, String server){
        Bukkit.getScheduler().runTaskAsynchronously(APICore.getPlugin(), ()-> {
            RMap<String, Object> account = redissonClient.getMap(REDIS_KEY + uuid);
            account.fastPut("name", name);
            account.fastPut("rank", rank.toString());
            account.fastPut("whitelist", whitelist);
            account.fastPut("hydras", hydras);
            account.fastPut("stats", stats);
            account.fastPut("settings", settings);
            account.fastPut("history", history);
            account.fastPut("recidivism", recidivism);
            account.fastPut("otherip", otherIp);
            account.fastPut("server", server);
        });
    }


    @CanTakeTime
    public Account getAccount(UUID uuid) throws CanTakeTimeException {
        if(isAccountOnRedis(uuid)){
            return getAccountRedis(uuid);
        }
        return getAccountDB(uuid);
    }

    public void sendAccountDatabase(UUID uuid, String name, Rank rank, int whitelist, int hydras, String stats, String settings, String history, String recidivism, String otherIp){ //Envoyer compte SQL
        Bukkit.getScheduler().runTaskAsynchronously(APICore.getPlugin(), ()-> {
            try {
                Connection connection = DatabaseManager.HYDRANIUM.getDataBaseAccess().getConnection();
                final PreparedStatement query = connection.prepareStatement("UPDATE accounts SET pseudo = ?, rank = ?, whitelist = ?," +
                        " hydras = ?, stats = ?, settings = ?, history = ?, recidivism = ?, otherip = ? WHERE uuid = ?");
                query.setString(1, name);
                query.setString(2, rank.toString());
                query.setInt(3, whitelist);
                query.setInt(4, hydras);
                query.setString(5, stats);
                query.setString(6, settings);
                query.setString(7, history);
                query.setString(8, recidivism);
                query.setString(9, otherIp);
                query.setString(10, uuid.toString());
                query.executeUpdate();
                query.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public Account getAccountDB(UUID uuid) throws CanTakeTimeException {
        Account account = null;
        try {
            Connection connection = DatabaseManager.HYDRANIUM.getDataBaseAccess().getConnection(); //Connexion
            final PreparedStatement query = connection.prepareStatement("SELECT * FROM accounts WHERE uuid = ?"); //Préparation
            query.setString(1, uuid.toString()); //Envoie
            final ResultSet results = query.executeQuery(); //Exécution + résultats
            if(results.next()){
                final int id = results.getInt("id");
                final String pseudo = results.getString("pseudo");
                final String ip = results.getString("ip");
                final Rank rank = Rank.valueOf(results.getString("rank"));
                final int whitelist = results.getInt("whitelist");
                final int hydras = results.getInt("hydras");
                final String stats = results.getString("stats");
                final String settings = results.getString("settings");
                final String history = results.getString("history");
                final String recidivism = results.getString("recidivism");
                final String otherip = results.getString("otherip");
                account = new Account(id, uuid, pseudo, ip, rank, whitelist, hydras, stats, settings, history, recidivism, otherip, "lobby");
            }
            query.close();
            connection.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
        return account;
    }

    public Object getDataDB(String data, UUID uuid) throws CanTakeTimeException { //Obtenir info compte SQL
        Object value = null;
        try {
            Connection connection = DatabaseManager.HYDRANIUM.getDataBaseAccess().getConnection();
            PreparedStatement query = connection.prepareStatement("SELECT " + data + " FROM accounts WHERE uuid = ?");
            query.setString(1, uuid.toString());
            ResultSet resultSet = query.executeQuery();
            if(resultSet.next()) {
                value = resultSet.getObject(data);
            }
            query.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
    }

    public void setDataDB(String type, Object value, UUID uuid){
        Bukkit.getScheduler().runTaskAsynchronously(APICore.getPlugin(), ()-> {
            try {
                Connection connection = DatabaseManager.HYDRANIUM.getDataBaseAccess().getConnection();
                final PreparedStatement query = connection.prepareStatement("UPDATE accounts SET " + type + " = ? WHERE uuid = ?");
                if(value instanceof Integer){
                    query.setInt(1, (Integer) value);
                } else {
                    query.setString(1, value.toString());
                }
                query.setString(2, uuid.toString());
                query.executeUpdate();
                query.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void banPlayer(BannedPlayer bannedPlayer){
        Bukkit.getScheduler().runTaskAsynchronously(APICore.getPlugin(), ()-> {
            try {
                Connection connection = DatabaseManager.HYDRANIUM.getDataBaseAccess().getConnection();
                PreparedStatement query = connection.prepareStatement("UPDATE accounts set ban = ? where uuid = ?");
                query.setString(1, bannedPlayer.serialize());
                query.setString(2, bannedPlayer.get("uuid").toString());
                query.execute();
                query.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            PubSub.refreshBan();
        });
    }

    public void unbanPlayer(UUID uuid){
        Bukkit.getScheduler().runTaskAsynchronously(APICore.getPlugin(), ()-> {
            try {
                Connection connection = DatabaseManager.HYDRANIUM.getDataBaseAccess().getConnection();
                PreparedStatement query = connection.prepareStatement("UPDATE accounts set ban = ? WHERE uuid = ?");
                query.setString(1, "");
                query.setString(2, uuid.toString());
                query.execute();
                query.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            APICore.getPlugin().getPlayerBanned().remove(uuid);
            PubSub.refreshBan();
        });
    }

    public void mutePlayer(MutedPlayer mutedPlayer){
        Bukkit.getScheduler().runTaskAsynchronously(APICore.getPlugin(), ()-> {
            try {
                Connection connection = DatabaseManager.HYDRANIUM.getDataBaseAccess().getConnection();
                PreparedStatement query = connection.prepareStatement("UPDATE accounts SET mute = ? where uuid = ?");
                query.setString(1, mutedPlayer.serialize());
                query.setString(2, mutedPlayer.get("uuid").toString());
                query.execute();
                query.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            PubSub.refreshMute();
        });
    }

    public void unmutePlayer(UUID uuid){
        Bukkit.getScheduler().runTaskAsynchronously(APICore.getPlugin(), ()-> {
            try {
                Connection connection = DatabaseManager.HYDRANIUM.getDataBaseAccess().getConnection();
                PreparedStatement query = connection.prepareStatement("UPDATE accounts SET mute = ? WHERE uuid = ?");
                query.setString(1, "");
                query.setString(2, uuid.toString());
                query.execute();
                query.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            APICore.getPlugin().getPlayerMuted().remove(uuid);
            APPlayer target = APICore.getPlugin().getAPPlayer(uuid);
            if(target != null){
                target.setMute(false);
            }
            PubSub.refreshMute();
        });
    }

    public void loadBannedPlayers(){
        Bukkit.getScheduler().runTaskAsynchronously(APICore.getPlugin(), ()-> {
            try {
                Connection connection = DatabaseManager.HYDRANIUM.getDataBaseAccess().getConnection();
                PreparedStatement query = connection.prepareStatement("SELECT ban FROM accounts");
                ResultSet resultSet = query.executeQuery();
                while(resultSet.next()){
                    if(!resultSet.getString("ban").equals("")){
                        BannedPlayer bannedPlayer = new BannedPlayer();
                        bannedPlayer.deserialize(resultSet.getString("ban"));
                        APICore.getPlugin().getPlayerBanned().put(UUID.fromString((String) bannedPlayer.get("uuid")), bannedPlayer);
                    }
                }
                query.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void loadMutedPlayers(){
        Bukkit.getScheduler().runTaskAsynchronously(APICore.getPlugin(), ()-> {
            try {
                Connection connection = DatabaseManager.HYDRANIUM.getDataBaseAccess().getConnection();
                PreparedStatement query = connection.prepareStatement("SELECT mute FROM accounts");
                ResultSet resultSet = query.executeQuery();
                UUID uuid;
                while(resultSet.next()){
                    if(!resultSet.getString("mute").equals("")){
                        MutedPlayer mutedPlayer = new MutedPlayer();
                        mutedPlayer.deserialize(resultSet.getString("mute"));
                        uuid = UUID.fromString((String) mutedPlayer.get("uuid"));
                        APICore.getPlugin().getPlayerMuted().put(uuid, mutedPlayer);
                        if(APICore.getPlugin().doesAPPlayerExists(uuid)){
                            APICore.getPlugin().getAPPlayer(uuid).setMute(true);
                        }
                    }
                }
                query.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private Account getAccountRedis(UUID uuid) throws CanTakeTimeException {
        RMap<String, Object> account = redissonClient.getMap(REDIS_KEY + uuid);
        Account playerAccount = new Account();
        playerAccount.setPseudo((String) account.get("name"));
        playerAccount.setRank(Rank.valueOf((String) account.get("rank")));
        playerAccount.setWhitelist((Integer) account.get("whitelist"));
        playerAccount.setHydras((Integer) account.get("hydras"));
        playerAccount.setStats((String) account.get("stats"));
        playerAccount.setSettings((String) account.get("settings"));
        playerAccount.setHistory((String) account.get("history"));
        playerAccount.setRecidivism((String) account.get("recidivism"));
        playerAccount.setOtherip((String) account.get("otherip"));
        playerAccount.setServer((String) account.get("server"));
        return playerAccount;
    }


}