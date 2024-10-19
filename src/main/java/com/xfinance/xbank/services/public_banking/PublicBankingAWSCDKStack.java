package com.xfinance.xbank.services.public_banking;

import software.constructs.Construct;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.apigateway.*;

// AWS DynamoDB
import software.amazon.awscdk.services.dynamodb.*;

// AWS Lambda
import software.amazon.awscdk.services.lambda.*;

// EC2 Services
import software.amazon.awscdk.services.ec2.*;
import software.amazon.awscdk.services.ecs.*;
import software.amazon.awscdk.services.ecs.patterns.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PublicBankingAWSCDKStack extends Stack {

	public PublicBankingAWSCDKStack(final Construct parent, final String id, final StackProps props) {
		super(parent, id, props);

		// Create the VPC with a AZ limit of 3
		Vpv vpc = Vpc.Builder.create(this, "com_xfinance_xbank_services_publicbanking_110_vpc").maxAzs(3).build();
		// Create the ECS service
		Cluster cluster = Cluster.Builder.create(this, "com_xfinance_xbank_services_publicbanking_110_ecs_cluster").vpc(vpc).build();

		// Use the ECS network load balanced fargate service construct to create a ECS service
		ApplicationLoadBalancedFargateService auth_Service = ApplicationLoadBalancedFargateService.Builder.create(
			this,
			"com_xfinance_xbank_services_publicbanking_110_balanced_fargate_service")
				.cluster(cluster)
				.cpu(512)
				.desiredCount(6)
				.taskImageOptions(
					ApplicationLoadBalancedTaskImageOptions.builder()
						.image(ContainerImage.fromRegistery("xfinance/xbank-default"))
						.build())
				.memoryLimitMiB(2048)
				.publicLoadBalancer(true)
				.redirectHTTP(true)
				.listenerPort(443)
				.protocol(ApplicationProtocol.HTTPS)
				.certificate(Certificate.fromCertificateArn(this, "ALB-Certificate", certificateArn))
				.domainName("com_xfinance_xbank")
				.domainZone(route53.HostedZone.fromHostedZoneAttributes(this, "com_xfinance_local_zone"))
				.build();



		fargateService.getService()
			.getConnections()
			.getSecurityGroups()
			.get(0)
			.addIngressRule(Peer.ipv4(vpc.getVpcCidrBlock()), Port.tcp(80), "allow http inbound from vpc");


		TableProps tableProps;
		Attribute partitionKey = Attribute.builder()
			.name("itemId")
			.type(AttributeType.STRING)
			.build();
		tableProps = TableProps.builder()
			.tableName("items")
			.partitionKey(partitionKey)
			.removalPolicy(RemovalPolicy.DESTROY)
			.build();
		Table dynamodbTable = new Table(this, "items", tableProps);


		RestApi auth_api = RestApi.Builder.create(this, "pb_auth_api")
			.restApiName("PublicBanking Auth Service")
			.description("This API exposed XBank's Public Banking Auth Services")
			.build();

		RestApi defi_api = RestApi.Builder.create(this, "defi_pb_api")
			.restApiName("PublicBanking DeFi Services")
			.description("This API exposed XBank's Public Banking DeFi Services")
			.build();

		RestApi third_party_api = RestApi.Builder.create(this, "third_party_api")
			.restApiName("PublicBanking Third Party Services")
			.description("This API exposed XBank's Public Banking ThirdParty apps and services")
			.build();

		RestApi app_api = RestApi.Builder.create(this, "app_api")
			.restApiName("PublicBanking Application API Services")
			.description("This API exposed XBank's Public Banking Integeration Applications and Services")
			.build();

		Map<String, String> auth_api_env_map = new HashMap<>();
		auth_api_env_map.put("JWKS_ENDPOINT", System.getenv("OIDC_JWKS_ENDPOINT"));
		auth_api_env_map.put("API_ID", System.getenv("REST_API_ID"));
		auth_api_env_map.put("ACCOUNT_ID", System.getenv("CDK_DEFAULT_ACCOUNT"));
		auth_api_env_map.put("SM_JWKS_SECRET_NAME", System.getenv("SM_JWKS_SECRET_NAME"));

		Function jwtAuthFunc = Function.Builder.create(this, "jwt_auth_function")
			.code(Code.fromAsset("resources/lambda"))
			.handler("lambda-auth.handler")
			.role(LambdaAuthorizeRole)
			.environment(auth_api_env_map)
			.build();

	}

}