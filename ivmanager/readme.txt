设置库ip

http://172.16.170.55:8080/list?ip=172.16.190.176

http://172.16.190.176:8080/agent.antieye?action=set&type=SWITCH&beanName=antiMaliciousDiamondSwitch&fieldName=driverComplaintThreshold&newValue=100&persist=true

curl "http://10.161.232.79:8080/agent.antieye?action=list&type=SWITCH"


mvn deploy:deploy-file -Dfile=edaijia-antieye-web/WEB-INF/lib/json-lib-2.2.3.jar -DgroupId=net.sf.json-lib -DartifactId=json-lib -Dversion=2.2.3 -Dpackaging=jar -Durl=http://115.29.11.157:8081/nexus/content/repositories/thirdparty/ -DrepositoryId=thirdparty




