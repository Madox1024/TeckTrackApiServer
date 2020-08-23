while true
do
curl -s --data "@C:\Test Folder\current.xml" http://localhost:7000/upload -w "%{time_total}\n"
echo $?
sleep .1
done