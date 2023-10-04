package fr.adrackiin.hydranium.api.management.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

public class RedisAccess {

    public static RedisAccess instance; //Instance classe, Singleton
    private final RedissonClient redissonClient; //Communication avec Redis

    private RedisAccess(RedisCredentials redisCrendentials) { //Constructeur
        instance = this;
        this.redissonClient = initRedisson(redisCrendentials);
    }

    public RedissonClient getRedissonClient() { //Getter
        return redissonClient;
    }

    private RedissonClient initRedisson(RedisCredentials redisCredentials) { //Initialisation connexion Redisson Redis
        final Config config = new Config(); //Configuration Redisson
        config.setCodec(new JsonJacksonCodec()); //Manière sauvegarder données Redis, JSON
        config.setUseLinuxNativeEpoll(true); //Uniquement sur Linux
        config.setThreads(4); //Threads alloués (nombre de coeur * 2)
        config.setNettyThreads(4); //Pareil
        config.useSingleServer()
                .setAddress(redisCredentials.getIp() + ":" + redisCredentials.getPort())
                .setPassword(redisCredentials.getPassword())
                .setDatabase(1)
                .setClientName(redisCredentials.getClientName());

        return Redisson.create(config);
    }

    public static void init() { //Ouvrir l'accès Redis
        new RedisAccess(new RedisCredentials("127.0.0.1", "CdiXGlHATDKfb2YyFnWq", 12846, "hydranium_api"));
    }

    public static void close() { //Fermer l'accès Redis
        RedisAccess.instance.getRedissonClient().shutdown();
    }

}
