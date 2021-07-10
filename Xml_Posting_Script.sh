while true
do
curl -s --data "@C:\Test Folder\current.xml" livetiming.tecktrack.com:7000/upload -w "%{time_total}\n"
echo $?
sleep 1
done