FROM redis/redis-stack-server:latest

COPY ./init-redis.sh /usr/local/bin/init-redis.sh

RUN chmod u+x /usr/local/bin/init-redis.sh

CMD ["sh", "-c", "/usr/local/bin/init-redis.sh"]