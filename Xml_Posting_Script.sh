while true
do
curl -s --data "@Test_Documents\TestResults.xml" http://localhost:7000/upload -w "%{time_total}\n"
echo $?
sleep 1
done