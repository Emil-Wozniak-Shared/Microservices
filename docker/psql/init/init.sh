#!/bin/bash
echo
echo ">> Tworzenie db"
psql --username "$POSTGRES_USER" -d "$POSTGRES_USER" -a -f /opt/data/init.sql
echo "<< Tworzenie db"
echo "<< Inicjalizacja bazy danymi testowymi"
echo "<< initdb.sh end"
