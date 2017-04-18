#!/bin/bash
[ -z "$TARGET_URL" ] && echo "Need to set TARGET_URL" && exit 1;
[ -z "$RUNS" ] && echo "Need to set RUNS" && exit 1;
while true; do
	curl --output /dev/null --silent --head --fail "${TARGET_URL}/login"
	if [ $? -eq "0" ] ; then
		break
	fi
	echo "failed to reach ${TARGET_URL}"
    sleep 5
done
echo "reached target URL, beginning tests.."
sed -i "s|###TARGET_URL###|${TARGET_URL}|g" /lgen/cfg.yml
mkdir /lgen/results && cd /lgen/results
for i in $(seq 1 $RUNS); do
  bzt /lgen/cfg.yml
done
echo "serving result dir now"
python -m SimpleHTTPServer
