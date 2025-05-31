package tda.darkarmy.mvpserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MvpserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(MvpserverApplication.class, args);
	}

	/*
	server {
		listen 80;
		listen 443 ssl;
		ssl on;
		ssl_certificate /etc/nginx/certificates/server.crt;
		ssl_certificate_key /etc/nginx/certificates/server.key;
		server_name 35.174.200.157;
		location / {
				proxy_pass http://127.0.0.1:8080;
        }
	}

	server {
		listen 80;
		server_name 35.174.200.157;
		location / {
				proxy_pass http://127.0.0.1:5173;
        }
	}
*/

}
