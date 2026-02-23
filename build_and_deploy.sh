# Build phase
mvn clean package

# copy
scp target/helloworld-service.jar "${OCI_VM_LOGIN}:/home/opc/artifacts"

# login
ssh $OCI_VM_LOGIN

# Reload daemon and start service
ssh $OCI_VM_LOGIN << 'EOF'
sudo mv /home/opc/artifacts/helloworld-service.jar /home/labuser/artifacts
sudo systemctl restart helloworld.service
exit
EOF
