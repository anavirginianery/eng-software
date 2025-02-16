#!/bin/bash

if ! command -v psql &> /dev/null
then
    echo "PostgreSQL não está instalado. Instalando agora..."
    sudo apt update
    sudo apt install -y postgresql
else
    echo "PostgreSQL já está instalado."
fi

echo "Alterando a senha do usuário postgres para 'senha123'..."
sudo -u postgres psql -c "ALTER USER postgres WITH PASSWORD 'senha123';"

echo "Criando a base de dados 'diabetter'..."
sudo -u postgres psql -c "CREATE DATABASE diabetter;"

echo "Processo concluído com sucesso!"

