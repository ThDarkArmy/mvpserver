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
		server_name 44.203.45.70;
		location / {
				proxy_pass http://127.0.0.1:8080;
        }
	}

	server {
		listen 80;
		server_name 44.203.45.70;
		location / {
				proxy_pass http://127.0.0.1:8080;
        }
	}
*/

	/*
server {
    listen 80;
    server_name first-buy.in www.first-buy.in;
    return 301 https://$host$request_uri;
}

server {
    listen 443 ssl;
    server_name first-buy.in www.first-buy.in;

    ssl_certificate /etc/letsencrypt/live/first-buy.in/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/first-buy.in/privkey.pem;

    location / {
        proxy_pass http://localhost:8080;

        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
	* */

}
