while true
do
curl --data "@C:\Test Folder\TestResults.xml" http://localhost:7000/upload
echo $?
sleep 1
done