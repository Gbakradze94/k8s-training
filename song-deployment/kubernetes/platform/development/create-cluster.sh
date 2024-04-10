#!/bin/sh

echo "\n📦 Initializing Kubernetes cluster...\n"

minikube start --cpus 2 --memory 4g --driver docker --profile song

echo "\n🔌 Enabling NGINX Ingress Controller...\n"

minikube addons enable ingress --profile song

sleep 30

#echo "\n📦 Deploying Keycloak..."
#
#kubectl apply -f services/keycloak-config.yml
#kubectl apply -f services/keycloak.yml
#
#sleep 5
#
#echo "\n⌛ Waiting for Keycloak to be deployed..."
#
#while [ $(kubectl get pod -l app=polar-keycloak | wc -l) -eq 0 ] ; do
#  sleep 5
#done

#echo "\n⌛ Waiting for Keycloak to be ready..."
#
#kubectl wait \
#  --for=condition=ready pod \
#  --selector=app=polar-keycloak \
#  --timeout=300s
#
#echo "\n⌛ Ensuring Keycloak Ingress is created..."
#
#kubectl apply -f services/keycloak.yml

echo "\n📦 Deploying PostgreSQL..."

kubectl apply -f services/resource-postgresql.yml

sleep 5

echo "\n⌛ Waiting for PostgreSQL to be deployed..."

while [ $(kubectl get pod -l db=resource-postgres | wc -l) -eq 0 ] ; do
  sleep 5
done

echo "\n⌛ Waiting for PostgreSQL to be ready..."

kubectl wait \
  --for=condition=ready pod \
  --selector=db=resource-postgres \
  --timeout=180s


