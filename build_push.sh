mvn clean install -Dmaven.test.skip=true

version=$1
echo "Start build with version $version ..."

echo "docker build . -t 172.16.71.92/yn01/openwaf:fileUpload."$version
docker build . \
  -t 172.16.71.92/yn01/openwaf:fileUpload."$version"

echo "docker push 172.16.71.92/yn01/openwaf:fileUpload."$version
docker push 172.16.71.92/yn01/openwaf:fileUpload."$version"
