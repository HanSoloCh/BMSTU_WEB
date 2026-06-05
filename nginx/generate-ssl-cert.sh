#!/bin/bash

mkdir -p ssl

openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
    -keyout ssl/nginx-selfsigned.key \
    -out ssl/nginx-selfsigned.crt \
    -subj "/C=RU/ST=Moscow/L=Moscow/O=BMSTU/OU=WebLab/CN=localhost"

echo "SSL сертификат успешно создан в директории ssl/"




