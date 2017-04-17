#!/bin/bash
set -e
[ -z "$TARGET_URL" ] && echo "Need to set TARGET_URL" && exit 1;
sed -i "s|###TARGET_URL###|${TARGET_URL}|g" /lgen/cfg.yml
exec bzt /lgen/cfg.yml