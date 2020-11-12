#!/bin/bash
set -e
export AWS_S3_BUCKET=aj-lambda-examples
export AWS_S3_PREFIX=flyless
export AWS_CLI_PROFILE=devops
export AWS_REGION=us-east-1
export STACK_NAME=flyless-stack

export DB_NAME=test_db
export DB_USERNAME=user
export DB_PASSWORD=password
export DEPLOY_TIMESTAMP=$(date +%s)

function cleanup() {
  print_green "CLEANING UP ..."
  docker stop sam_flyless_mysql || true
  docker network rm sam_flyless_newtork
}
trap cleanup EXIT

function print_green() {
  echo -e '\n'
  echo -e "\e[32m"$1"\e[0m"
  echo -e '\n'
}

print_green "BUILDING ..."
sam build

print_green "STARTING CONTAINERS ..."
docker network create --driver bridge sam_flyless_newtork
docker run --rm -d --name sam_flyless_mysql --network sam_flyless_newtork -e MYSQL_RANDOM_ROOT_PASSWORD=yes -e MYSQL_DATABASE=$DB_NAME -e MYSQL_USER=$DB_USERNAME -e MYSQL_PASSWORD=$DB_PASSWORD mysql:5.7.31
sleep 10

print_green "INVOKING ..."
sam local invoke -e events/create_event.json --docker-network sam_flyless_newtork --env-vars env.json

print_green "DEPLOING ..."
sam deploy \
  --stack-name $STACK_NAME \
  --region $AWS_REGION \
  --s3-bucket $AWS_S3_BUCKET \
  --s3-prefix $AWS_S3_PREFIX \
  --capabilities CAPABILITY_IAM \
  --parameter-overrides DBName=$DB_NAME DBStage=test DeployTimestamp=$DEPLOY_TIMESTAMP \
  --profile $AWS_CLI_PROFILE
