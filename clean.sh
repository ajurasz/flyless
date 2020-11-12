#!/bin/bash

export AWS_CLI_PROFILE=devops
export STACK_NAME=flyless-stack

aws cloudformation delete-stack \
  --stack-name $STACK_NAME \
  --profile $AWS_CLI_PROFILE
