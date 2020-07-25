package spaceweare.tracking4d.Security.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {

	private String secretKey = "tenth6385revelations";
	private String secretKeyForEmail = "alkwisdlekksiuipa";
	//validity in milliseconds
	private long validityInMs = 86400000; // 24h
	private long validityInMsForRestorePassword = 1800000; // 24h
	//private long validityInMs = 60000; // 24h
}