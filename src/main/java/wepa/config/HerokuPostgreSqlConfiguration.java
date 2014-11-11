package wepa.config;

import java.net.URI;
import java.net.URISyntaxException;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("herokuPostgres")
public class HerokuPostgreSqlConfiguration {
	
	private static final String HEROKU_DATABASE_URL_KEY = "DATABASE_URL";
	private static final String HEROKU_POSTGRE_URL_PREFIX = "jdbc:postgresql://";
	
	@Bean
	public DataSource dataSource() throws URISyntaxException {
		URI dbUri = new URI(System.getenv(HEROKU_DATABASE_URL_KEY));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl =  HEROKU_POSTGRE_URL_PREFIX + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
        
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
	}
}
