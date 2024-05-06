#!/bin/bash

echo "APPLICATION_CONTEXT_ROOT=${APPLICATION_CONTEXT_ROOT}"
echo "GLASSFISH_ADMIN_PASSWORD=${GLASSFISH_ADMIN_PASSWORD}"
echo "DB_HOST=${DB_HOST}"
echo "DB_PORT=${DB_PORT}"
echo "DB_USER=${DB_USER}"
echo "DB_PASSWORD=${DB_PASSWORD}"
echo "DB_NAME=${DB_NAME}"

echo "AS_ADMIN_PASSWORD=${GLASSFISH_ADMIN_PASSWORD}" > admin-password

wget -P /opt/glassfish7/glassfish/domains/domain1/lib https://jdbc.postgresql.org/download/postgresql-42.7.2.jar

asadmin start-domain

asadmin --passwordfile admin-password --interactive=false create-jdbc-connection-pool \
    --datasourceclassname org.postgresql.ds.PGConnectionPoolDataSource \
    --driverclassname org.postgresql.Driver \
    --restype javax.sql.ConnectionPoolDataSource \
    --property "user=${DB_USER}:password=${DB_PASSWORD}:databaseName=${DB_NAME}:serverName=${DB_HOST}:port=${DB_PORT}" \
    jdbc/qbConnPool

asadmin --passwordfile admin-password --interactive=false create-jdbc-resource \
  --connectionpoolid jdbc/qbConnPool \
  jdbc/qbDS

asadmin --passwordfile admin-password --interactive=false deploy \
    --contextroot=${APPLICATION_CONTEXT_ROOT}  \
    questionbank.war

asadmin stop-domain
DEBUG_MODE=${GLASSFISH_DEBUG_MODE:-false}
asadmin start-domain --debug=${DEBUG_MODE} --watchdog