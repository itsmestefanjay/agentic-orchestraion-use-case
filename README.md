# Agentic Orchestration Use Case

## Description

This use case shows how an ai connector orchestrates a set of tools to detect an order status

## Tools

- AWS Bedrock with Anthropic Claude 3 Haiku
- Custom Java Workers for Order information
- Camunda Saas (8.8)

## AWS Todos

- AWS Bedrock -> Request Model Access
- AWS IAM -> With Role `AmazonBedrockFullAccess`
- Security Credentials (Access Key, Secret Access Key)
- Pro Tip: Deactivate Credentials when not using them (`IAM` > `User` > `Security Credentials`)
- Pro Tip: Set a budget for your AWS account to keep costs under control (`Billing and Cost Management` > `Budget`)
