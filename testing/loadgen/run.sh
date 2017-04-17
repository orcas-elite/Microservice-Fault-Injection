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
sed -i "s|###TARGET_URL###|${TARGET_URL}|g" /lgen/cfg.yml
for i in {1..${RUNS}}; do
  exec bzt /lgen/cfg.yml
done
read -rsp $'Press any key to continue...\n' -n1 key
