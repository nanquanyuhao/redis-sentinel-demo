package net.nanquanyuhao.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.util.List;

@Configuration
public class RedisConfig {

    @Autowired
    private RedisProperties redisProperties;

    /**
     * Lettuce
     */
    @Bean
    public RedisConnectionFactory lettuceConnectionFactory() {

        RedisProperties.Sentinel sentinel = redisProperties.getSentinel();
        List<String> nodes = sentinel.getNodes();

        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
                .master(sentinel.getMaster());

        for (int i = 0; i < nodes.size(); i++) {
            String[] node = nodes.get(i).split(":");
            sentinelConfig.sentinel(node[0], Integer.parseInt(node[1]));
        }

        sentinelConfig.setSentinelPassword(sentinel.getPassword());
        sentinelConfig.setDatabase(redisProperties.getDatabase());
        sentinelConfig.setPassword(redisProperties.getPassword());

        return new LettuceConnectionFactory(sentinelConfig);
    }
}
