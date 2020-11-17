## flyless

[![Actions Status](https://github.com/ajurasz/flyless/workflows/CI/badge.svg)](https://github.com/ajurasz/flyless/actions)

Sample setup that demonstrate how to manage schema evolution for relational databases from within lambda function.

### Prerequisites

- java 11
- docker (tested with `19.03.6`)
- sam (tested with `1.7.0`)
- aws cli


### Deployment

SAM template is ready to be deployed with the help of `deploy.sh` bash script.

> **NOTE:** It will be required to adjust following variables `AWS_S3_BUCKET` and `AWS_CLI_PROFILE`  
